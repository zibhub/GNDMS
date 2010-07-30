/*
/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



 * Portions of this file Copyright 1999-2005 University of Chicago
 * Portions of this file Copyright 1999-2005 The University of Southern California.
 *
 * This file or a portion of this file is licensed under the
 * terms of the Globus Toolkit Public License, found at
 * http://www.globus.org/toolkit/download/license.html.
 * If you redistribute this file, with or without
 * modifications, you must include this notice in the file.
 */
package de.zib.gndms.infra.grams;

import org.apache.log4j.Logger;
import org.globus.common.ChainedIOException;
import org.globus.exec.generated.FilePairType;
import org.globus.exec.generated.ScriptCommandEnumeration;
import org.globus.exec.generated.StateEnumeration;
import org.globus.exec.utils.Resources;
import org.globus.gram.internal.GRAMConstants;
import org.globus.util.I18n;
import org.globus.util.Util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Job Manager Perl script processor.
 *
 * Used to execute a GRAM Perl script to process some part of a job request.
 * Callbacks will be issued when a file is completely staged by the script
 * or when the script terminates.
 *
 * With some minor changes to work without WS-context.
 */
public class GNDMSJobManagerScript {

    protected static final String JOB_STATE  = "GRAM_SCRIPT_JOB_STATE";
    protected static final String ERROR  = "GRAM_SCRIPT_ERROR";
    protected static final String JOB_ID  = "GRAM_SCRIPT_JOB_ID";
    protected static final String SCRATCH_DIR  = "GRAM_SCRIPT_SCRATCH_DIR";
    protected static final String STAGED_IN  = "GRAM_SCRIPT_STAGED_IN";
    protected static final String STAGED_IN_SHARED
        = "GRAM_SCRIPT_STAGED_IN_SHARED";
    protected static final String STAGED_OUT  = "GRAM_SCRIPT_STAGED_OUT";
    protected static final String X509_USER_PROXY
        = "GRAM_SCRIPT_X509_USER_PROXY";
    protected static final String REMOTE_IO_FILE = "GRAM_SCRIPT_REMOTE_IO_FILE";
    protected static final String FAILURE_TYPE = "GRAM_SCRIPT_GT3_FAILURE_TYPE";
    protected static final String FAILURE_MESSAGE
        = "GRAM_SCRIPT_GT3_FAILURE_MESSAGE";
    protected static final String FAILURE_SOURCE
        = "GRAM_SCRIPT_GT3_FAILURE_SOURCE";
    protected static final String FAILURE_DESTINATION
            = "GRAM_SCRIPT_GT3_FAILURE_DESTINATION";

    protected static final int CUSTOM_ERROR_MISCONFIGURED_SUDO = 200;
    protected static final int CUSTOM_ERROR_UNHANDLED_STDERR = 201;
    protected static final int CUSTOM_ERROR_EXEC_FAILED = 202;

    static final int COMMAND_SIZE_WITHOUT_SUDO = 9;
    static final int COMMAND_SIZE_WITH_SUDO = COMMAND_SIZE_WITHOUT_SUDO + 4;

    private static final                String SCRIPT =
            "/libexec/globus-job-manager-script.pl";
    private static final                String GLOBUS_GRIDMAP_AND_EXECUTE =
            "/libexec/globus-gridmap-and-execute";

    private String username;
    private File descriptionFile;
    private ScriptCommandEnumeration command;
    private String [] commandWithArgs;
    private Process p;
    private int jobState;
    private int error;
    private String jobId;
    private String scratchDir;
    private String x509UserProxy;
    private String remoteIoFile;
    private List stageInList;
    private List stageOutList;
    private String failureType;
    private String failureMessage;
    private String failureSource;
    private String failureDestination;
    private boolean done;
    private String [] environment;
    private String scriptStderr;

    public GNDMSJobManagerScript(
        String                              username,
        String                              globusLocation,
        String                              type,
        ScriptCommandEnumeration            command,
        String                              perlJobDescription,
        String[]                            environment)
        throws                              IOException
    {
        super();
        this.username = username;
        this.command = command;
        this.descriptionFile = File.createTempFile(
            "gram_job_mgr",
            null,
            new File(globusLocation + File.separator + "tmp"));

        Util.setFilePermissions(this.descriptionFile.toString(), 644);
        FileOutputStream out = new FileOutputStream(this.descriptionFile);
        out.write(perlJobDescription.getBytes());
        out.close();

        //GlobusShToolsProperties toolPathProps
        //    = GlobusShToolsProperties.getInstance();

        String gridMapAndExecute = new File(
            globusLocation + GLOBUS_GRIDMAP_AND_EXECUTE).toString();
        String gridMapFile = null;
        try
        {
            gridMapFile = "/etc/grid-security/grid-mapfile";
                // = ContainerSecurityConfig.getConfig().getSecurityDescriptor()
                // .getGridMapFile();
        }
        catch (Exception e)
        {
            throw new ChainedIOException(i18n.getMessage(
                Resources.GRIDMAP_CONFIGURATION_ERROR), e);
        }
        // boolean authzGridmap = AuthorizationHelper.isAuthorizationGridmap();
        boolean authzGridmap = true;

        String sudo = "/usr/bin/sudo";
        if ((sudo == null) || (sudo.length() == 0))
        {
            throw new IOException(i18n.getMessage(Resources.MISSING_SUDO));
        }

        Vector commandVector = new Vector();
        if (!System.getProperty("user.name").equals(this.username)) {
            commandVector.add(sudo);
            commandVector.add("-H");
            commandVector.add("-u");
            commandVector.add(this.username);
            commandVector.add("-S");
            if (authzGridmap)
            {
                commandVector.add(gridMapAndExecute);
                if (gridMapFile != null)
                {
                    commandVector.add("-g");
                    commandVector.add(gridMapFile);
                }
            }
        }
        commandVector.add(new File(globusLocation + SCRIPT).toString());
        commandVector.add("-m");
        commandVector.add(type.toLowerCase());
        commandVector.add("-f");
        commandVector.add(this.descriptionFile.toString());
        commandVector.add("-c");
        commandVector.add(command.toString());

        this.commandWithArgs = (String[]) commandVector.toArray(new String[0]);

        this.done = false;
        this.environment = new String[environment.length];
        System.arraycopy(
            environment, 0,
            this.environment, 0,
            environment.length);

        stageInList = new LinkedList();
        stageOutList = new LinkedList();
    }

    public void run() {
        String line;
        String from, to;

        if (logger.isDebugEnabled()) {
            StringBuffer commandBuffer
                = new StringBuffer(commandWithArgs.length*2);
            for (int index=0; index<commandWithArgs.length; index++) {
                if (index > 0) {
                    commandBuffer.append(" ");
                }
                commandBuffer.append(commandWithArgs[index]);
            }
            logger.debug("Executing command:\n" + commandBuffer.toString());
        }

        BufferedReader scriptOutputReader = null;

        try {

            this.p = Runtime.getRuntime().exec(
                commandWithArgs, this.environment);

            //avoid possible password prompt hang from sudo (with -S option)
            this.p.getOutputStream().close();

            scriptOutputReader = new BufferedReader(
                    new InputStreamReader(this.p.getInputStream()));

            line = scriptOutputReader.readLine();
            
            if (logger.isDebugEnabled()) {
                logger.debug("first line: " + line);
            }
            
            while (line != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Read line: " + line);
                }

                if (line.startsWith(JOB_STATE)) {
                    line = line.substring(JOB_STATE.length() + 1).trim();
                    this.jobState = Integer.parseInt(line);
                } else if (line.startsWith(ERROR)) {
                    line = line.substring(ERROR.length() + 1).trim();
                    this.error = Integer.parseInt(line);
                } else if (line.startsWith(JOB_ID)) {
                    line = line.substring(JOB_ID.length() + 1).trim();
                    this.jobId = line;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Received local job ID " + this.jobId);
                    }
                } else if (line.startsWith(SCRATCH_DIR)) {
                    line = line.substring(SCRATCH_DIR.length() + 1).trim();
                    this.scratchDir = line;
                } else if (line.startsWith(STAGED_IN)) {
                    line = line.substring(STAGED_IN.length() + 1).trim();
                    from = line.substring(0, line.indexOf(' '));
                    to = line.substring(line.indexOf(' ') + 1);
                    FilePairType stageInDatum = new FilePairType();
                    stageInDatum.setSourceFile(from);
                    stageInDatum.setDestinationFile(to);
                    addStageInDatum(stageInDatum);
                } else if (line.startsWith(STAGED_OUT)) {
                    line = line.substring(STAGED_OUT.length() + 1).trim();
                    from = line.substring(0, line.indexOf(' '));
                    to = line.substring(line.indexOf(' ') + 1);
                    FilePairType stageOutDatum = new FilePairType();
                    stageOutDatum.setSourceFile(from);
                    stageOutDatum.setDestinationFile(to);
                    addStageOutDatum(stageOutDatum);
                } else if (line.startsWith(X509_USER_PROXY)) {
                    line = line.substring(X509_USER_PROXY.length() + 1).trim();
                    this.x509UserProxy = line;
                } else if (line.startsWith(REMOTE_IO_FILE)) {
                    line = line.substring(REMOTE_IO_FILE.length() + 1).trim();
                    this.remoteIoFile = line;
                } else if (line.startsWith(FAILURE_TYPE)) {
                    line = line.substring(FAILURE_TYPE.length() + 1).trim();
                    this.failureType = line;
                } else if (line.startsWith(FAILURE_MESSAGE)) {
                    line = line.substring(FAILURE_MESSAGE.length() + 1).trim();
                    this.failureMessage = line.replaceAll("\\\\n", "\n");
                } else if (line.startsWith(FAILURE_SOURCE)) {
                    line = line.substring(FAILURE_SOURCE.length() + 1).trim();
                    this.failureSource = line;
                } else if (line.startsWith(FAILURE_DESTINATION)) {
                    line = line.substring(FAILURE_DESTINATION.length() + 1).trim();
                    this.failureDestination = line;
                }

                line = scriptOutputReader.readLine();
            }

        }
        catch (Exception e)
        {
            this.error = CUSTOM_ERROR_EXEC_FAILED;
            this.failureMessage = e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("adapter script execution failed", e);
            }
        }
        finally
        {
            if (scriptOutputReader != null) {
                try { scriptOutputReader.close(); } catch (IOException e) {}
            }
            if (this.p != null) {
                try {this.p.getOutputStream().close();} catch (IOException e) {}

                BufferedReader stderr = null;
                stderr = new BufferedReader(new InputStreamReader(
                    this.p.getErrorStream()));
                StringBuffer stderrBuffer = new StringBuffer();
                String stderrLine = null;
                try {
                    while ((stderrLine=stderr.readLine()) != null) {
                        stderrBuffer.append(stderrLine);
                    }
                } catch (IOException ioe) {
                    String errorMessage =
                        i18n.getMessage(Resources.SCRIPT_STDERR_READ_ERROR);
                    logger.error(errorMessage);
                }
                if (stderrBuffer.length() > 0) {
                    this.scriptStderr = stderrBuffer.toString();
                    if (this.scriptStderr.trim().indexOf("Password:") == 0) {
                        this.error = CUSTOM_ERROR_MISCONFIGURED_SUDO;
                        this.failureMessage = i18n.getMessage(
                            Resources.MISCONFIGURED_SUDO,
                            new String[] { "globus-job-manager-script.pl",
                                           this.username });
                    } else {
                        this.error = CUSTOM_ERROR_UNHANDLED_STDERR;
                        this.failureMessage = i18n.getMessage(
                            Resources.JOB_STDERR_EXISTS, this.scriptStderr);
                    }
                }
                try { stderr.close(); } catch (IOException e) { }

                if (logger.isDebugEnabled()) {
                    logger.debug("failure message: " + this.failureMessage);
                }
            }
        }

        this.descriptionFile.delete();
        if (logger.isDebugEnabled()) {
            logger.debug("script is done, setting done flag");
        }
        done = true;
    }

    protected void addStageInDatum(FilePairType data) {
        synchronized (this) {
            stageInList.add(data);
        }
    }

    protected void addStageOutDatum(FilePairType data) {
        synchronized (this) {
            stageOutList.add(data);
        }
    }

    /**
     * Returns a FilePairType returned by the Perl script.
     *
     * @return The FilePairType returned by the Perl script, or null if
     *         none was returned yet. This should to be called multiple
     *         times until null is returned and the isDone() returns
     *         true.
     */
    public synchronized FilePairType getNextStageInDatum() {
        if (this.stageInList.isEmpty()) {
            return null;
        } else {
            return (FilePairType) this.stageInList.remove(0);
        }
    }

    public synchronized FilePairType getNextStageOutDatum() {
        if (this.stageOutList.isEmpty()) {
            return null;
        } else {
            return (FilePairType) this.stageOutList.remove(0);
        }
    }

    /**
     * Returns whether the Perl script has finished execution.
     *
     * @return true if the script has completed, false otherwise.
     */
    public synchronized boolean isDone() {
        return this.done;
    }

    /**
     * Returns the jobState returned by the Perl script
     * as a StateEnumeration.
     *
     * @return The jobState returned by the Perl script
     *         as a StateEnumeration, or null if
     *         none was returned.
     */
    public synchronized StateEnumeration getJobState() {
        StateEnumeration result = null;

        if (this.jobState == GRAMConstants.STATUS_UNSUBMITTED) {
            result = StateEnumeration.Unsubmitted;
        /*
        } else if (this.jobState == GRAMConstants.STATUS_STAGE_IN) {
            result = StateEnumeration.StageIn;
            */
        } else if (this.jobState == GRAMConstants.STATUS_PENDING) {
            result = StateEnumeration.Pending;
        } else if (this.jobState == GRAMConstants.STATUS_ACTIVE) {
            result = StateEnumeration.Active;
        /*
        } else if (this.jobState == GRAMConstants.STATUS_STAGE_OUT) {
            result = StateEnumeration.StageOut;
            */
        } else if (this.jobState == GRAMConstants.STATUS_DONE) {
            result = StateEnumeration.Done;
        } else if (this.jobState == GRAMConstants.STATUS_FAILED) {
            result = StateEnumeration.Failed;
        }
        return result;
    }

    /**
     * Returns the jobId returned by the Perl script.
     *
     * @return The jobId returned by the Perl script, or null if none was
     *         returned.
     */
    public synchronized String getJobId() {
        return this.jobId;
    }

    public synchronized ScriptCommandEnumeration getCommand() {
        return this.command;
    }

    /**
     * Returns the error returned by the Perl script.
     *
     * @return The error returned by the Perl script, or null if
     *         none was returned.
     */
    public synchronized int getError() {
        return this.error;
    }

    /**
     * Returns the scratchDirectory returned by the Perl script.
     *
     * @return The scratchDirectory returned by the Perl script, or null if
     *         none was returned.
     */
    public synchronized String getScratchDirectory() {
        return this.scratchDir;
    }

    public synchronized String getX509UserProxy() {
        return this.x509UserProxy;
    }

    public synchronized String getRemoteIoFile() {
        return this.remoteIoFile;
    }
    public synchronized String getFailureType() {
        return this.failureType;
    }

    public synchronized String getFailureMessage() {
        return this.failureMessage;
    }
    public synchronized String getFailureSource() {
        return this.failureSource;
    }
    public synchronized String getFailureDestination() {
        return this.failureDestination;
    }

    /**
     * Returns printable information about the Perl script execution.
     *
     * Returns a string containing the printable infromation about the
     * thread that the script output reader is running in as well as the
     * values which may have been returned by the script.
     */
    public String toString() {
        return super.toString() +
               "descriptionFile = " + this.descriptionFile + "\n" +
               "process = " + this.p + "\n" +
               "jobState = " + new Integer(this.jobState) + "\n" +
               "error = " + new Integer(this.error) + "\n" +
               "jobId = " + this.jobId + "\n" +
               "scratch_dir = " + this.scratchDir + "\n" +
               "done = " + (this.done ? "true" : "false") + "\n";
    }

    static Logger logger;
    static {
         logger = Logger.getLogger(GNDMSJobManagerScript.class);
        //logger = LogAux.stdSetupLogger(GNDMSJobManagerScript.class);
    }

    private static I18n i18n = I18n.getI18n(Resources.class.getName());
}
