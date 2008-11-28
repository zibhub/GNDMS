package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MapConfig;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 13:13:09
 */
@SuppressWarnings({ "ThrowableInstanceNeverThrown" })
public class ExternalProviderStageInAction extends AbstractProviderStageInAction {

    private ParmFormatAux parmAux;

    public ExternalProviderStageInAction() {
        super();
        this.parmAux = new ParmFormatAux();
    }


    public ExternalProviderStageInAction(final @NotNull EntityManager em, final @NotNull Task model) {
        super(em, model);
        this.parmAux = new ParmFormatAux();
    }


    public ExternalProviderStageInAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
        this.parmAux = new ParmFormatAux();
    }


    @Override
    protected void doStaging(
            final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
            final Slice sliceParam) {

        parmAux.formatFromMap( getOfferTypeConfig() );

        final ProcessBuilder procBuilder = createProcessBuilder(sliceParam);
        final StringBuilder recv = new StringBuilder(8);

        try {
            final ProcessBuilderAction action = parmAux.createPBAction(orqParam, null);

            action.setProcessBuilder(procBuilder);
            action.setOutputReceiver(recv);
            int result = action.call();
            switch (result) {
                case 0:
                    finish( new ProviderStageInResult( sliceParam.getId() ) );                    
                default:
                    fail(new IllegalStateException("Staging script failed with non-zero exit code " + result));
            }
        }
        catch (RuntimeException e) {
            honorOngoingTransit(e);
            fail(new RuntimeException(recv.toString(), e));
        }
    }


    private ProcessBuilder createProcessBuilder(final Slice sliceParam) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            ConfigProvider opts = new MapConfig(getKey().getConfigMap());
            builder.directory(new File(sliceParam.getOwner().getPathForSlice(sliceParam)));
            builder.command(opts.getFileOption("stagingCommand").getPath());
            return builder;
        }
        catch (MandatoryOptionMissingException e) {
            throw new RuntimeException(e);
        }
    }
}
