package de.zib.gndms.logic.model.gorfx.c3grid;

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



import com.google.inject.Inject;
import de.zib.gndms.kit.system.SystemInfo;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedORQException;
import de.zib.gndms.logic.model.gorfx.UnfulfillableORQException;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.stuff.Sleeper;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 13:29:13
 */
public class ExternalProviderStageInORQCalculator extends AbstractProviderStageInORQCalculator {
	public static final long GLOBUS_DEATH_DURATION = 60000L;
    public static final int EXIT_CODE_UNFULFILLABLE = 127;
    public static final int EXIT_CODE_PERMISSION_DENIED = 126;

	private static final int INITIAL_STRING_BUILDER_CAPACITY = 4096;

	private @NotNull final Log logger = LogFactory.getLog(ExternalProviderStageInORQCalculator.class);

	private ParmFormatAux parmAux;
	private SystemInfo sysInfo;


	public ExternalProviderStageInORQCalculator( ) {
        super( );
		parmAux = new ParmFormatAux();
    }


    @Override
    public TransientContract createOffer() throws Exception {
        final @NotNull TransientContract cont = getPreferredOfferExecution();
        final @NotNull TransientContract result;

        MapConfig config = new MapConfig(getKey().getConfigMap());
        
        parmAux.formatFromMap( config );

        if (config.hasOption("estimationCommand")) {
            File estCommandFile = config.getFileOption("estimationCommand");
            if (! AbstractProviderStageInAction.isValidScriptFile(estCommandFile))
                throw new IllegalArgumentException("Invalid estimationCommand script: " + estCommandFile.getPath());
			return createOfferViaEstScript(estCommandFile, cont);
        }
        else
            /* Use plain copy if no estimation script has been specified */ 
            result = cont.clone();
        return result;
    }


    @SuppressWarnings({ "HardcodedLineSeparator", "MagicNumber" })
    private TransientContract createOfferViaEstScript(
            final File estCommandFileParam, final TransientContract contParam) {
        ProcessBuilderAction action = createEstAction(estCommandFileParam, contParam);
        StringBuilder outRecv = action.getOutputReceiver();
        StringBuilder errRecv = action.getErrorReceiver();
        int exitCode = action.call();
        switch (exitCode) {
            case EXIT_CODE_UNFULFILLABLE:
                throw new UnfulfillableORQException("Estimation scipt failed (Unfulfillable): " + errRecv.toString());
            case EXIT_CODE_PERMISSION_DENIED:
                throw new PermissionDeniedORQException("Estimation scipt failed (Permission Denied): " + errRecv.toString());
            case 0:
                try {
                    return parmAux.getResult( outRecv );
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


    private ProcessBuilderAction createEstAction(final File estCommandFileParam, final TransientContract contParam) {
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
        action = parmAux.createPBAction( getORQArguments(), contParam, null );
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
