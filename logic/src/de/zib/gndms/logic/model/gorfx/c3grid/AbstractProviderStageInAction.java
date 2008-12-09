package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.util.TxFrame;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
@SuppressWarnings({ "FeatureEnvy" })
public abstract class AbstractProviderStageInAction extends ORQTaskAction<ProviderStageInORQ> {

    private String sliceId;
	protected ParmFormatAux parmAux = new ParmFormatAux();

    public AbstractProviderStageInAction() {
        super();
    }


    public AbstractProviderStageInAction(final @NotNull EntityManager em, final @NotNull AbstractTask model) {
        super(em, model);
    }


    public AbstractProviderStageInAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onCreated(final AbstractTask model) {
        try {
            super.onCreated(model);    // Overridden method
        }
        catch (TransitException e) {
            if (e.isDemandingAbort()) throw e; // dont continue on failure

            try {
                MapConfig config = getOfferTypeConfig();
                File stagingCommandFile = config.getFileOption("stagingCommand");
                if (! stagingCommandFile.exists() || ! stagingCommandFile.canRead() || ! stagingCommandFile.isFile())
                    fail(new IllegalArgumentException("Invalid stagingCommand script: " + stagingCommandFile.getPath()));
                createNewSlice(model);
            }
            catch (MandatoryOptionMissingException e1) {
                fail(new RuntimeException(e1));
            }
            throw e; // accept state transition decision from super
        }
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown"})
    @Override
    protected void onInProgress(final @NotNull AbstractTask model) {
        final Slice slice = findNewSlice(model);
        setSliceId( slice.getId() );
        doStaging(getOfferTypeConfig(), getOrq(), slice);
    }


    protected abstract void doStaging(
            final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
            final Slice sliceParam);


    private void createNewSlice(final AbstractTask model) throws MandatoryOptionMissingException {

        final ConfigProvider config = getOfferTypeConfig();

        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            final ImmutableScopedName scopedName = config.getISNOption("subspace");
            final MetaSubspace metaSubspace = getEntityManager().find(
                    MetaSubspace.class,
                    scopedName);
            final @NotNull Subspace subspace = metaSubspace.getInstance();
            String slicekindKey = config.getOption("sliceKind");
            SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);
            CreateSliceAction csa = new CreateSliceAction();
            csa.setParent(this);
            csa.setTerminationTime(getModel().getContract().getResultValidity());
            csa.setClosingEntityManagerOnCleanup(false);
            csa.setDirectoryAux(DirectoryAux.getDirectoryAux());
            csa.setUUIDGen(getUUIDGen());
            csa.setId(getUUIDGen().nextUUID());
            csa.setModel(subspace);
            csa.setSliceKind(kind);
            final Slice slice = csa.execute(getEntityManager());
            model.setData(slice.getId());
            txf.commit();
        }
        finally { txf.finish();  }
    }


    private Slice findNewSlice(final AbstractTask model) {
	    final EntityManager em = getEntityManager();
	    final TxFrame txf = new TxFrame(em);
	    try {
		    //final Slice slice = em.find(Slice.class, model.getData());
            final Slice slice = em.find(Slice.class, getSliceId() );
	        txf.commit();
	        return slice;
	    }
	    finally { txf.finish();  }
    }


	@Override
    public void cleanUpOnFail(final @NotNull AbstractTask model) {
		try {
			cancelStaging(model);
		}
		catch(RuntimeException e) { getLog().warn(e); }
		finally {
			try {
				// wrappend in try-finally to ensure we go to failed even if slice
				// deletion goes wrong
				killSlice(model);
			}
			finally {
                // intentionally
            }
		}
	}


	private void cancelStaging(final AbstractTask model) {
		final Slice slice;
		final File sliceDir;
		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			slice = findNewSlice(model);
			sliceDir = new File(slice.getOwner().getPathForSlice(slice));
			txf.commit();
		}
		finally { txf.finish(); }
		// potentially unsafe but do not want to keep the transaction open
		// we just dont change the slice dir after creation
		callCancel(getOfferTypeConfig(), getOrq(), sliceDir);
	}


	@SuppressWarnings({ "NoopMethodInAbstractClass" })
	protected void callCancel(final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
            final File sliceParam) {
		/* potentially implement in subclass */
	}


	private void createNewSlice(final AbstractTask model) throws MandatoryOptionMissingException {
	    final ConfigProvider config = getOfferTypeConfig();

	    final EntityManager em = getEntityManager();
	    final TxFrame txf = new TxFrame(em);
	    try {
	        final ImmutableScopedName scopedName = config.getISNOption("subspace");
	        final MetaSubspace metaSubspace = getEntityManager().find(
	                MetaSubspace.class,
	                scopedName);
	        final @NotNull Subspace subspace = metaSubspace.getInstance();
	        String slicekindKey = config.getOption("sliceKind");
	        SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);
	        CreateSliceAction csa = new CreateSliceAction();
	        csa.setParent(this);
	        csa.setTerminationTime(getModel().getContract().getResultValidity());
	        csa.setClosingEntityManagerOnCleanup(false);
	        csa.setDirectoryAux(DirectoryAux.getDirectoryAux());
	        csa.setUUIDGen(getUUIDGen());
	        csa.setId(getUUIDGen().nextUUID());
	        csa.setModel(subspace);
	        csa.setSliceKind(kind);
	        final Slice slice = csa.execute(getEntityManager());
	        model.setData(slice.getId());
		    txf.commit();
	    }
	    finally { txf.finish();  }
	}


	private Slice findNewSlice(final AbstractTask model) {
		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			final Slice slice = em.find(Slice.class, model.getData());
		    txf.commit();
		    return slice;
		}
		finally { txf.finish();  }
	}
	private void killSlice(final AbstractTask model) {
		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			final Slice slice = findNewSlice(model);
			slice.getOwner().destroySlice(slice);
			em.remove(slice);
			txf.commit();
		}
		catch (RuntimeException e) { getLog().warn(e); throw e; }
		finally { txf.finish();  }
	}


	protected ProcessBuilder createProcessBuilder(String name, File dir) {
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

	@Override
    @NotNull
    protected Class<ProviderStageInORQ> getOrqClass() {
        return ProviderStageInORQ.class;
    }


    protected @NotNull MapConfig getOfferTypeConfig() {
        return new MapConfig(getKey().getConfigMap());
    }


    protected void setSliceId( String sliceId ) {
        this.sliceId = sliceId;
    }

    
    protected String getSliceId( ) {
        return sliceId;
    }
}
