package de.zib.gndms.logic.model.gorfx.c3grid;

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



import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.dspace.ChownSliceConfiglet;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.dspace.DeleteSliceAction;
import de.zib.gndms.logic.model.dspace.TransformSliceAction;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.stuff.Sleeper;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
@SuppressWarnings({ "FeatureEnvy" })
public abstract class AbstractProviderStageInAction extends ORQTaskAction<ProviderStageInORQ> {

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
	            getScriptFileByParam(config, "stagingCommand");
                createNewSlice(model);
            }
            catch (MandatoryOptionMissingException e1) {
                failFrom(e1);
            }
            throw e; // accept state transition decision from super
        }
    }


	@SuppressWarnings({ "ThrowableInstanceNeverThrown" })
	protected @NotNull File getScriptFileByParam(final MapConfig configParam, String scriptParam) throws MandatoryOptionMissingException {
		final @NotNull File scriptFile = configParam.getFileOption(scriptParam);
		if (! isValidScriptFile(scriptFile))
		    failFrom(new IllegalArgumentException("Invalid " + scriptParam + " script: " + scriptFile.getPath()));
		return scriptFile;
	}


	@SuppressWarnings({ "ThrowableInstanceNeverThrown"})
    @Override
    protected void onInProgress(final @NotNull AbstractTask model) {
        final Slice slice = findSlice(model);
        setSliceId( slice.getId() );
        try {
            doStaging(getOfferTypeConfig(), getOrq(), slice);
            changeSliceOwner( slice ) ;
            try{
            } catch ( Exception e ) {
                failFrom( new RuntimeException( "Failed while creating result slice." ,e ) );
            }
            finish( new ProviderStageInResult( getSliceId()) );
        }
        catch (RuntimeException e) {
            honorOngoingTransit(e);
            failFrom( e );
        }
    }


    protected  void changeSliceOwner( Slice slice ) {

        ChownSliceConfiglet csc = getConfigletProvider().getConfiglet( ChownSliceConfiglet.class, "sliceChown" );

        if( csc == null )
            throw new IllegalStateException( "chown configlet is null!");

        String dn = getOrq().getActContext().get( "DN" );
        getLog().debug( "cso DN: "+ dn );
        getLog().debug( "changing owner of " + slice.getId() + " to " + getOrq().getLocalUser() );
        ProcessBuilderAction chownAct = csc.createChownSliceAction( getOrq().getLocalUser(),
            slice.getSubspace().getPath() + File.separator + slice.getKind().getSliceDirectory(),
            slice.getDirectoryId() );
        chownAct.call();
    }



    protected void transformToResultSlice( Slice slice ) {

        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            String slicekindKey = "http://www.c3grid.de/G2/SliceKind/Result";
            SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);

            TransformSliceAction tsa =  new TransformSliceAction(
                getUUIDGen().nextUUID(),
                getOrq().getLocalUser(),
                slice.getTerminationTime(),
                kind,
                slice.getSubspace(),
                slice.getTotalStorageSize(),
                getUUIDGen()
            );

            tsa.setParent( this );
            tsa.setModel( slice );
            tsa.initialize();
            tsa.setClosingEntityManagerOnCleanup( false );
            final Slice tgt_slice = tsa.execute( em );

            setSliceId( tgt_slice.getId() );

            DeleteSliceAction dsa = new DeleteSliceAction( slice );
            dsa.setParent( this );
            dsa.setClosingEntityManagerOnCleanup( false );
            dsa.setModel( slice.getSubspace() );
            dsa.execute( em );
            txf.commit();
        }
        finally { txf.finish();  }

    }


    protected abstract void doStaging(
        final MapConfig offerTypeConfigParam, final ProviderStageInORQ orqParam,
        final Slice sliceParam );


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
            // uid should be the id of hte container
            csa.setTerminationTime(getModel().getContract().getResultValidity());
            csa.setClosingEntityManagerOnCleanup(false);
            csa.setUUIDGen(getUUIDGen());
            csa.setId(getUUIDGen().nextUUID());
            csa.setModel(subspace);
            csa.setSliceKind(kind);
            final Slice slice = csa.execute(getEntityManager());
            setSliceId(slice.getId());
            // to provoke nasty test condition uncomment the following line
            //throw new NullPointerException( );
            txf.commit();
        }
        finally { txf.finish();  }
	    getLog().info("createNewSlice() = " + getSliceId());
    }


    private Slice findSlice(final AbstractTask model) {
	    final String sliceId = getSliceId();
	    getLog().info("findSlice(" + (sliceId == null ? "null" : '"' + sliceId + '"') + ')');
	    if (sliceId == null)
		    return null;


	    final EntityManager em = getEntityManager();
	    final TxFrame txf = new TxFrame(em);
	    try {
		    final Slice slice = em.find(Slice.class, sliceId);
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
		catch(RuntimeException e) {
			getLog().warn(e);
		}
		finally {
			// wrappend in try-finally to ensure we go to failed even if slice
			// deletion goes wrong
			killSlice(model);
		}
	}


	protected void cancelStaging(final AbstractTask model) {
		final Slice slice;
		final File sliceDir;

		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			slice = findSlice(model);
			if (slice == null)
				// MAYBE log this somewhere?
				return;
			sliceDir = new File(slice.getSubspace().getPathForSlice(slice));
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
		// Implement in subclass
	}


	private void killSlice(final AbstractTask model) {
		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			final Slice slice = findSlice(model);
			DeleteSliceAction.deleteSlice( slice, this );
			em.remove(slice);
			txf.commit();
		}
		catch (RuntimeException e) { getLog().warn(e); throw e; }
		finally {
            try{
                txf.finish();
            } catch ( Exception e ) { // no exception must leave this method
                trace( "Exception on commit or rollback", e );
            }
        }
	}


	protected ProcessBuilder createProcessBuilder(String name, File dir) {
       try {
           MapConfig opts = new MapConfig(getKey().getConfigMap());
		   if (opts.getOption(name, "").trim().length() == 0)
			   return null;
		   final File fileOption = getScriptFileByParam(opts, name);
		   ProcessBuilder builder = new ProcessBuilder();
			   builder.directory(dir);
		   builder.command(fileOption.getPath());
			   return builder;
       }
       catch (MandatoryOptionMissingException e) {
           throw new RuntimeException("createProcessBuilder " + (name==null?"null":name) + " " + (dir==null?"(null)":dir), e);
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
        getOrq().setActSliceId(sliceId);
    }

    
    protected String getSliceId( ) {
        return getOrq().getActSliceId();
    }

	@SuppressWarnings({ "MethodWithMoreThanThreeNegations" })
	public static boolean isValidScriptFile(final File scriptFile) {
		// do we need "r"?
		return scriptFile != null &&
			   scriptFile.exists() && scriptFile.canRead() && scriptFile.isFile();
	}
}

