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



import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.dspace.ChownSliceConfiglet;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.dspace.DeleteSliceAction;
import de.zib.gndms.logic.model.dspace.TransformSliceAction;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
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
public abstract class AbstractProviderStageInAction extends TaskFlowAction<ProviderStageInOrder> {

    public static final String PROXY_FILE_NAME = "/x509_proxy.pem";

	protected ParmFormatAux parmAux = new ParmFormatAux();

    protected AbstractProviderStageInAction( String offerTypeId ) {
        super( offerTypeId );
    }


    public AbstractProviderStageInAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }


    @Override
    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        MapConfig config = getOfferTypeConfig();
	    getScriptFileByParam(config, "stagingCommand");
        createNewSlice();
        super.onCreated(wid, state, isRestartedTask, altTaskState);
    }


    protected void prepareProxy( ) {
        final Slice slice = findSlice();
        File sd = new File( slice.getSubspace().getPathForSlice( slice ) + PROXY_FILE_NAME );
        getCredentialProvider().installCredentials( sd );
    }


	@SuppressWarnings({ "ThrowableInstanceNeverThrown" })
	protected @NotNull File getScriptFileByParam(final MapConfig configParam, String scriptParam)
            throws MandatoryOptionMissingException {

		final @NotNull File scriptFile = configParam.getFileOption(scriptParam);
		if (! isValidScriptFile(scriptFile))
		    throw new IllegalArgumentException("Invalid " + scriptParam + " script: " + scriptFile.getPath());
		return scriptFile;
	}


    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        final Slice slice = findSlice();
        setSliceId(slice.getId());
        doStaging(getOfferTypeConfig(), getOrderBean(), slice);
        changeSliceOwner( slice ) ;
        super.onInProgress(wid, state, isRestartedTask, altTaskState);
    }

    protected  void changeSliceOwner( Slice slice ) {

        ChownSliceConfiglet csc = getConfigletProvider().getConfiglet( ChownSliceConfiglet.class, "sliceChown" );

        if( csc == null )
            throw new IllegalStateException( "chown configlet is null!");

        final DelegatingOrder<?> order = getOrder();
        String dn = order.getActContext().get( "DN" );
        getLogger().debug( "cso DN: " + dn );
        getLogger().debug( "changing owner of " + slice.getId() + " to " + order.getLocalUser() );
        ProcessBuilderAction chownAct = csc.createChownSliceAction( order.getLocalUser(),
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
                getOrder().getLocalUser(),
                slice.getTerminationTime(),
                kind,
                slice.getSubspace(),
                slice.getTotalStorageSize()
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
        final MapConfig offerTypeConfigParam, final ProviderStageInOrder orderParam,
        final Slice sliceParam );


    private void createNewSlice() throws MandatoryOptionMissingException {

        final ConfigProvider config = getOfferTypeConfig();

        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            final String scopedName = config.getOption("subspace");
            final @NotNull Subspace subspace = getEntityManager().find(
                    Subspace.class,
                    scopedName);
            String slicekindKey = config.getOption("sliceKind");
            SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);

            CreateSliceAction csa = new CreateSliceAction();
            csa.setParent(this);
            csa.setTerminationTime(getContract().getResultValidity());
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
	    getLogger().info( "createNewSlice() = " + getSliceId() );
    }


    private Slice findSlice() {
	    final String sliceId = getSliceId();
	    getLogger().info( "findSlice(" + ( sliceId == null ? "null" : '"' + sliceId + '"' ) + ')' );
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
    protected void onFailed(@NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        try {
            cancelStaging();
        }
        finally {
            killSlice();
            super.onFailed(wid, state, isRestartedTask, altTaskState);
        }
    }

	protected void cancelStaging() {
		final Slice slice;
		final File sliceDir;

		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			slice = findSlice();
			if (slice == null)
				// MAYBE log this somewhere?
				return;
			sliceDir = new File(slice.getSubspace().getPathForSlice(slice));
			txf.commit();
		}
		finally { txf.finish(); }
		// potentially unsafe but do not want to keep the transaction open
		// we just dont change the slice dir after creation
		callCancel(getOfferTypeConfig(), getOrderBean(), sliceDir);
	}


	@SuppressWarnings({ "NoopMethodInAbstractClass" })
	protected void callCancel(final MapConfig offerTypeConfigParam, final ProviderStageInOrder orderParam,
            final File sliceParam) {
		// Implement in subclass
	}


	private void killSlice() {
		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			final Slice slice = findSlice();
			DeleteSliceAction.deleteSlice( slice, this );
			em.remove(slice);
			txf.commit();
		}
		catch (RuntimeException e) { getLogger().warn( "", e ); throw e; }
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
           MapConfig opts = new MapConfig(getOfferTypeConfigMapData());
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
    public Class<ProviderStageInOrder> getOrderBeanClass( ) {
        return ProviderStageInOrder.class;
    }


    protected @NotNull
    MapConfig getOfferTypeConfig() {
        return new MapConfig(getOfferTypeConfigMapData());
    }



    protected void setSliceId( String sliceId ) {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            ProviderStageInOrder order = (ProviderStageInOrder ) task.getOrder( );
            order.setActSliceId( sliceId );
            task.setORQ( sliceId );
            session.finish();
        }
        finally {
            session.success();
        }
    }

    
    protected String getSliceId( ) {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            ProviderStageInOrder order = (ProviderStageInOrder ) task.getOrder( );
            final String ret = order.getActSliceId();
            session.finish();
            return ret;
        }
        finally {
            session.success();
        }
    }

	@SuppressWarnings({ "MethodWithMoreThanThreeNegations" })
	public static boolean isValidScriptFile(final File scriptFile) {
		// do we need "r"?
		return scriptFile != null &&
			   scriptFile.exists() && scriptFile.canRead() && scriptFile.isFile();
	}

    public PersistentContract getContract() {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            final PersistentContract ret = task.getContract();
            session.finish();
            return ret;
        }
        finally {
            session.success();
        }
    }
}

