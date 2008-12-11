package de.zib.gndms.logic.model.gorfx.c3grid;

import com.google.inject.Inject;
import de.zib.gndms.infra.system.SystemInfo;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedORQException;
import de.zib.gndms.logic.model.gorfx.UnfulfillableORQException;
import de.zib.gndms.model.common.types.TransientContract;
import org.jetbrains.annotations.NotNull;

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
    public static final int EXIT_CODE_UNFULFILLABLE = 255;
    public static final int EXIT_CODE_PERMISSION_DENIED = 254;

    private ParmFormatAux parmAux;
	private static final int INITIAL_STRING_BUILDER_CAPACITY = 4096;


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
            if (! estCommandFile.exists() || ! estCommandFile.canRead() || ! estCommandFile.isFile())
                throw new IllegalArgumentException("Invalid estimationCommand script: " + estCommandFile.getPath());
			return createOfferViaEstScript(estCommandFile, cont);
        }
        else
            /* Use plain copy if no estimation script has been specified */ 
            result = cont.clone();
        return result;
    }


    @SuppressWarnings({ "HardcodedLineSeparator" })
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
        action = parmAux.createPBAction( getORQArguments(), contParam );
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
