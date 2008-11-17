package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedORQException;
import de.zib.gndms.logic.model.gorfx.UnfulfillableORQException;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.xml.ProviderStageInXML;
import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


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

    @Override
    public Contract createOffer() throws Exception {
        final @NotNull Contract cont = getPerferredOfferExecution();
        final @NotNull Contract result;

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
            result = new Contract(cont);
        return result;
    }


    private Contract createOfferViaEstScript(
            final File estCommandFileParam, final Contract contParam) {
        ProcessBuilderAction action = createEstAction(estCommandFileParam, contParam);
        StringBuilder recv = action.getOutputReceiver();
        int exitCode = action.call();
        switch (exitCode) {
            case EXIT_CODE_UNFULFILLABLE:
                throw new UnfulfillableORQException();
            case EXIT_CODE_PERMISSION_DENIED:
                throw new PermissionDeniedORQException();
            case 0:
                try {
                    return parmAux.getResult( recv );
                } catch (Exception e) {
                    throw new IllegalStateException("Estimation script failed", e);
                }
            default:
                throw new IllegalStateException("Estimation script failed: " + action.toString());
        }
    }


    private ProcessBuilderAction createEstAction(final File estCommandFileParam, final Contract contParam) {
        final @NotNull ProcessBuilder pb = new ProcessBuilder();
        try {
            pb.command(estCommandFileParam.getCanonicalPath());
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        ProcessBuilderAction action;
        action = parmAux.createPBAction( getORQArguments(), contParam );
        action.setProcessBuilder(pb);
        action.setOutputReceiver(new StringBuilder(8));
        return action;
    }
}
