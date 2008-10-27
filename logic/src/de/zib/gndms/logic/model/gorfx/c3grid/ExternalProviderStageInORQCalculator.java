package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.logic.model.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.ByteArrayInputStream;
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

    @Override
    public Contract createOffer() throws Exception {
        final @NotNull Contract cont = getPerferredOfferExecution();
        final @NotNull Contract result;

        MapConfig config = new MapConfig(getKey().getConfigMap());
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
        if (exitCode != 0)
            throw new IllegalStateException("Estimation script failed: " + action.toString());
        else {
            Properties props = new Properties();
            try {
                props.load(new ByteArrayInputStream(recv.toString().getBytes()));
            }
            catch (IOException e) {
                throw new IllegalStateException("Estimation script failed", e);
            }
            ContractPropertyReader reader = new ContractPropertyReader(props);
            reader.performReading();
            return reader.getProduct();
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
        final Properties moreProps = new Properties();
        ContractPropertyWriter writer = new ContractPropertyWriter(moreProps);
        ContractConverter conv = new ContractConverter(writer, contParam);
        conv.convert();
        final @NotNull ProcessBuilderAction action = ProviderStageInTools.createPBAction(getORQArguments(), moreProps);
        action.setProcessBuilder(pb);
        action.setOutputReceiver(new StringBuilder(8));
        return action;
    }
}
