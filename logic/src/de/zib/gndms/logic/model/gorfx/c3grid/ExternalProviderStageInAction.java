package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
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

    private ParmFormatAux parmAux;

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


	@Override
	protected void callCancel(final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
            final File sliceDir) {
		final ProcessBuilder procBuilder = createProcessBuilder("cancelCommand", sliceDir);
		if (procBuilder == null)
			return;

		final StringBuilder recv = new StringBuilder(8);
		try {
		    final ProcessBuilderAction action = parmAux.createPBAction(orqParam, null);
		    action.setProcessBuilder(procBuilder);
		    action.setOutputReceiver(recv);
		    int result = action.call();
		    switch (result) {
		        case 0:
		            getLog().debug("Finished calling cancel");
		        default:
		            getLog().info("Failure during cancel: " + recv.toString());
		    }
		}
		catch (RuntimeException e) {
			getLog().warn(e);
		}
	}


	private ProcessBuilder createProcessBuilder(String name, File dir) {
        try {
            ConfigProvider opts = new MapConfig(getKey().getConfigMap());
	        if (opts.getOption(name, "").trim().length() == 0)
	            return null;
	        final File fileOption = opts.getFileOption(name);
	        ProcessBuilder builder = new ProcessBuilder();
            builder.directory(dir);
	        builder.command(fileOption.getPath());
            return builder;
        }
        catch (MandatoryOptionMissingException e) {
            throw new RuntimeException(e);
        }
    }
}
