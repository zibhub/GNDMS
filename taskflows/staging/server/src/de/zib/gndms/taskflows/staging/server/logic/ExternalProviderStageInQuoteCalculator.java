package de.zib.gndms.taskflows.staging.server.logic;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import javax.inject.Inject;

import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.kit.system.SystemInfo;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedTaskFlowException;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.stuff.Sleeper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 13:29:13
 */
public class ExternalProviderStageInQuoteCalculator extends
    AbstractProviderStageInQuoteCalculator {
	public static final long GLOBUS_DEATH_DURATION = 60000L;
    public static final int EXIT_CODE_UNFULFILLABLE = 127;
    public static final int EXIT_CODE_PERMISSION_DENIED = 126;

	private static final int INITIAL_STRING_BUILDER_CAPACITY = 4096;

	private @NotNull final Logger logger = LoggerFactory.getLogger(ExternalProviderStageInQuoteCalculator.class);

	private final StagingIOFormatHelper stagingIOHelper;
	private SystemInfo sysInfo;


	public ExternalProviderStageInQuoteCalculator() {
        super( );
		stagingIOHelper = new StagingIOFormatHelper();
    }


    @Override
    public List<Quote> createQuotes() throws Exception {
        final @NotNull Quote cont = getPreferredQuote();
        final @NotNull Quote result;


        MapConfig config = new MapConfig( getDao().getTaskFlowTypeConfig( getOrderBean().getTaskFlowType() ) );
        
        stagingIOHelper.formatFromMap( config );

        if (config.hasOption("estimationCommand")) {
            File estCommandFile = config.getFileOption("estimationCommand");
            if (! AbstractProviderStageInAction.isValidScriptFile(estCommandFile))
                throw new IllegalArgumentException("Invalid estimationCommand script: " + estCommandFile.getPath());
			result = createOfferViaEstScript(estCommandFile, cont);
        }
        else
            /* Use plain copy if no estimation script has been specified */ 
            result = cont.clone();
        return Arrays.asList( result );
    }


    @SuppressWarnings({ "HardcodedLineSeparator", "MagicNumber" })
    private Quote createOfferViaEstScript(
            final File estCommandFileParam, final Quote contParam) {
        ProcessBuilderAction action = createEstAction(estCommandFileParam, contParam);
        StringBuilder outRecv = action.getOutputReceiver();
        StringBuilder errRecv = action.getErrorReceiver();
        int exitCode = action.call();
        switch (exitCode) {
            case EXIT_CODE_UNFULFILLABLE:
                throw new UnsatisfiableOrderException("Estimation script failed (Unfulfillable): " + errRecv.toString());
            case EXIT_CODE_PERMISSION_DENIED:
                throw new PermissionDeniedTaskFlowException("Estimation script failed (Permission Denied): " + errRecv.toString());
            case 0:
                try {
                    return stagingIOHelper.getResult( outRecv );
                } catch (Exception e) {
                    throw new IllegalStateException("Estimation script failed: " + errRecv.toString(),
                                                    e);
                }
            default:
	            if (exitCode > 127) {
					logger.debug("Waiting for potential death of container...");     
		            Sleeper.sleepUninterruptible(GLOBUS_DEATH_DURATION);
	            }
                throw new IllegalStateException(
                    "Estimation script returned unexpected exit code: " + exitCode +
                        "\nScript output was:\n" + errRecv.toString() );
        }
    }


    private ProcessBuilderAction createEstAction(final File estCommandFileParam, final Quote contParam) {
        final @NotNull ProcessBuilder pb = new ProcessBuilder();
        try {
            pb.command(estCommandFileParam.getCanonicalPath());
	        pb.directory(new File(getSysInfo().getSystemTempDirName()));
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        ProcessBuilderAction action;
        // todo add permissions here when delegation is implemented
        action = stagingIOHelper.createPBAction( getOrderBean(), contParam, null );
        action.setProcessBuilder(pb);
        action.setOutputReceiver(new StringBuilder(INITIAL_STRING_BUILDER_CAPACITY));
        action.setErrorReceiver(new StringBuilder(INITIAL_STRING_BUILDER_CAPACITY));
        return action;
    }


	public SystemInfo getSysInfo() {
		return sysInfo;
	}


	@Inject
	public void setSysInfo(final @NotNull SystemInfo sysInfoParam) {
		sysInfo = sysInfoParam;
	}
}
