package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
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

	public ExternalProviderStageInAction() {
        super();
	    parmAux = new ParmFormatAux();
    }


    public ExternalProviderStageInAction(final @NotNull EntityManager em, final @NotNull AbstractTask model) {
        super(em, model);
	    parmAux = new ParmFormatAux();
    }


    public ExternalProviderStageInAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
	    parmAux = new ParmFormatAux();
    }


    @Override
    protected void doStaging(
            final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
            final Slice sliceParam) {

        parmAux.formatFromMap( getOfferTypeConfig() );

	    final File sliceDir = new File(sliceParam.getOwner().getPathForSlice(sliceParam));
        final ProcessBuilder procBuilder = createProcessBuilder("stagingCommand", sliceDir);
	    if (procBuilder == null)
	        fail(new IllegalStateException("No stagingCommand configured"));

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
}
