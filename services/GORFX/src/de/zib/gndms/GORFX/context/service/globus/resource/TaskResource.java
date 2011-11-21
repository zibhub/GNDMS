package de.zib.gndms.GORFX.context.service.globus.resource;

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



import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.context.stubs.TaskResourceProperties;
import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.util.GlobusCredentialProviderImpl;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import de.zib.gndms.gritserv.typecon.GORFXTools;
import de.zib.gndms.gritserv.typecon.util.AxisTypeFromToXML;
import de.zib.gndms.gritserv.typecon.types.FileTransferResultXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInResultXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.*;
import de.zib.gndms.gritserv.typecon.types.SliceStageInResultXSDTypeWriter;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import types.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.concurrent.Future;


/**
 * The implementation of this TaskResource type.
 *
 * @created by Introduce Toolkit version 1.2
 */
public class TaskResource extends TaskResourceBase
    implements ReloadablePersistentResource<Taskling, ExtTaskResourceHome> {


    private static final Logger log = Logger.getLogger( TaskResource.class );
    private ExtTaskResourceHome home;
    private TaskAction taskAction;
//    private GridResourceModelHandler<Taskling, ExtTaskResourceHome, TaskResource> mH;
    private Future<?> future;


    /**
     * If the task action isn't finished or failed it will be executed using the
     * GNDMSystem.
     */
    public void executeTask() {
        Taskling ling = (Taskling) (Object) taskAction.getModel( );
        Session session = home.getDao().beginSession();
        try {
            final Task tsk = ling.getTask(session);
            if (! tsk.isDone() )
                future = home.getSystem( ).submitAction( taskAction, getResourceHome().getLog() );
            else
                taskAction.getEntityManager().close();
            session.success();
        }
        finally { session.finish(); }
    }


    public TaskExecutionState getTaskExecutionState() {
        Session session = home.getDao().beginSession();
        try {
            TaskExecutionState stateOfTask =
                    GORFXTools.getStateOfTask(((Taskling) (Object) taskAction.getModel()).getTask(session));
            session.success();
            return stateOfTask;
        }
        finally { session.success(); }
    }


    public TaskExecutionFailure getTaskExecutionFailure() {
        Session session = home.getDao().beginSession();
        try {
            TaskExecutionFailure fail = new TaskExecutionFailure( );
            Task task = ((Taskling) (Object) taskAction.getModel()).getTask(session);
            if( task.getTaskState().equals( TaskState.FAILED ) ) {
                Serializable payload = task.getPayload();
                if( payload != null )
                    fail = GORFXTools.failureFromException( (Exception) payload);
            } else {
                fail.setAllIsFine( new Object() );
            }
            session.success();
            return fail;
        }
        finally { session.success(); }

    }


    /**
     * Delivers the result of a successfully executed task.
     *
     * If the task wasn't executed successfully an empty result object is returned.
     */
    public DynamicOfferDataSeqT getTaskExecutionResults() {

        DynamicOfferDataSeqT res;
        // URI uri = GORFXTools.scopedNameToURI( taskAction.getModel().getOfferType().getOfferResultType( ) );
        final String uri = taskAction.getModel().getOfferType( ).getOfferTypeKey();
        final TaskState stat = taskAction.getModel().getState();

        //WidAux.initWid( taskAction.getModel().getWid() );
        log.debug( "type: " + uri );

        try{
            if( uri.equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) ) {
                if( stat.equals( TaskState.FINISHED ) ) {

                    final Serializable sr = taskAction.getModel().getData( );

                    if(! ( sr instanceof ProviderStageInResult ) )
                        throw new IllegalArgumentException( "task result is not of type "
                            + ProviderStageInResult.class.getName( ) );

                    final ProviderStageInResult r = (ProviderStageInResult) sr;
                    final ProviderStageInResultXSDTypeWriter writer = new ProviderStageInResultXSDTypeWriter();
                    writer.begin( );
                    writer.writeSliceReference( r.getSliceKey() );
                    res = writer.getProduct();
                } else
                    res = new ProviderStageInResultT();
            } else if( uri.equals( GORFXConstantURIs.FILE_TRANSFER_URI )
                || uri.equals( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI ) ) {

                if( stat.equals( TaskState.FINISHED ) ){
                    final Serializable sr = taskAction.getModel().getData( );
                    if(! ( sr instanceof FileTransferResult ) )
                        throw new IllegalArgumentException( "task result is not of type "
                            + FileTransferResult.class.getName( ) );

                    final FileTransferResult ftr = ( FileTransferResult ) sr;
                    final FileTransferResultXSDTypeWriter writer = new FileTransferResultXSDTypeWriter();
                    writer.begin();
                    writer.writeFiles( ftr.getFiles() );
                    res = writer.getProduct();
                }

                else
                    res = new FileTransferResultT();

                if( uri.equals( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI ) )
                    ( (FileTransferResultT) res).setOfferType( GORFXClientTools.getInterSliceTransferURI() );


            } else if( uri.equals( GORFXConstantURIs.RE_PUBLISH_SLICE_URI ) ) {

                if( stat.equals( TaskState.FINISHED ) ){
                    final Serializable sr = taskAction.getModel().getData( );
                    if(! ( sr instanceof RePublishSliceResult ) )
                        throw new IllegalArgumentException( "task result is not of type "
                            + RePublishSliceResult.class.getName( ) );

                    res = RePublishSliceResultXSDTypeWriter.writeResult( ( RePublishSliceResult ) sr );
                }

                else
                    res = new RePublishSliceResultT();

            } else if( uri.equals( GORFXConstantURIs.SLICE_STAGE_IN_URI ) ) {

                if( stat.equals( TaskState.FINISHED ) ){
                    final Serializable sr = taskAction.getModel().getData( );
                    if(! ( sr instanceof SliceStageInResult ) )
                        throw new IllegalArgumentException( "task result is not of type "
                            + SliceStageInResult.class.getName( ) );

                    res = SliceStageInResultXSDTypeWriter.writeResult( ( SliceStageInResult ) sr );
                }

                else
                    res = new SliceStageInResultT();

            } // etc
            // todo add new task results here

            else {
                //throw new RemoteException( "Illegal offer type occured" );
                log.debug( "Illegal offer type occured"+ uri );
                throw new IllegalStateException( "Illegal offer type occured" );
            }


            StringWriter wr = new StringWriter();
            AxisTypeFromToXML.toXML(wr, res, false, true);
            log.info("Result is: " + wr.toString());

            return res;

        } catch  ( RuntimeException e ) {
            log.error( "RuntimeException: ", e );
            throw e;
        } catch  ( Exception e ) {
            log.error( "Exception: ", e );
            throw new RuntimeException( e );
        } // finally  {
          //  WidAux.removeWid();
        //}
    }


    public TaskAction getTaskAction() {
        return taskAction;
    }


    /**
     * Loads a task state form the data base and executes the task, useing executeTask.
     *
     * @param id The id of the task to load.
     * @return The task object.
     * @throws ResourceException If this resource already go an task object.
     */
    @NotNull
    public Taskling loadModelById( @NotNull String id ) throws ResourceException {

        logger.debug( "task resource for: " + id );

        if( taskAction != null )
            throw new ResourceException( "task action already loaded" );

        Dao dao = home.getDao();
        Session session = dao.beginSession();
        Taskling tsk;
        byte[] bcred;
        try {
            Task task = session.findTask(id);
            tsk = task.getTaskling();
            bcred = task.getSerializedCredential();
            session.success();
        }
        finally { session.finish();}

        GlobusCredential gcred = null;
        if( bcred.length > 0 )
            gcred = DelegationAux.fromByteArray( bcred );

        try {
            taskAction = getResourceHome().getSystem().getInstanceDir().newTaskAction( em, tsk.getOfferType().getOfferTypeKey() );
            taskAction.initFromModel(em, dao, tsk);
            taskAction.setClosingEntityManagerOnCleanup(true);
            taskAction.setCredentialProvider( new GlobusCredentialProviderImpl(
                ( ( ORQTaskAction ) taskAction).getOrq().getOfferType(), gcred ) );
        }
        catch (IllegalAccessException e) {
            throw new ResourceException(e);
        }
        catch (InstantiationException e) {
            throw new ResourceException(e);
        }
        catch (ClassNotFoundException e) {
            throw new ResourceException(e);
        }
        executeTask( );
        return tsk;
    }


    public void loadViaModelId( @NotNull String id ) throws ResourceException {
        loadFromModel(loadModelById(id));
    }


    public void loadFromModel( @NotNull Taskling model ) throws ResourceException {
        Dao dao = home.getDao();
        Session session = dao.beginSession();
        try {
            Task task = model.getTask(session);
            // Not required here cause we override the getters.
            // getter overriding isn't enough
            setTaskExecutionState( getTaskExecutionState() );
            setTerminationTime( task.getTerminationTime() );
            setTaskExecutionFailure( getTaskExecutionFailure() );
            setTaskExecutionResults( getTaskExecutionResults() );
            session.success();
        }
        finally { session.finish();}
    }


    @NotNull
    public ExtTaskResourceHome getResourceHome() {
        if( home == null )
            throw new IllegalStateException( "No task resource home set" );
        return home;
    }


    public void setResourceHome( @NotNull ExtTaskResourceHome resourceHomeParam ) {

        if ( home == null ) {
            home = resourceHomeParam;
        }
        else
            throw new IllegalStateException("Task resource home already set");
    }


    public void load( ResourceKey resourceKey ) throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {

        if ( getResourceHome().getKeyTypeName().equals( resourceKey.getName() ) ) {
            String id = ( String ) resourceKey.getValue();
            Taskling ling = loadModelById( id );
            setResourceKey( resourceKey );
            initialize( new TaskResourceProperties(),
                TaskConstants.RESOURCE_PROPERTY_SET, id );
            loadFromModel( ling );
        }
        else
            throw new InvalidResourceKeyException("Invalid resourceKey name");
    }


    public void store() throws ResourceException {
        // Not required here
        // task persists itself
    }


    @Override
    public String getID( ) {
        return (String) super.getID();
    }

    @Override
    public void refreshRegistration(final boolean forceRefresh) {
        // nothing
    }


    @Override
    public void remove( ) {

        if( taskAction != null )  {
            Log log = taskAction.getLog();
            log.debug( "Removing task resource: " + getID() );
            AbstractTask tsk = taskAction.getModel();
            boolean cleanUp = false;
            if( tsk != null ) {
                if ( ! tsk.isDone() ) {
                    // task is still running cancel it and cleanup entity manager
                    taskAction.setCancelled( true );
                    log.debug( "cancel task " + tsk.getWid() );
                    cleanUp = true;
                    if( future != null ) {
                        future.cancel( true );
                        try {
                            // give cancel some time
                            Thread.sleep( 2000L );
                        } catch ( InterruptedException e ) {
                            logger.debug(  e );
                        }
                    }

                    try {
                        EntityManager em = taskAction.getEntityManager();
                        if( em != null &&  em.isOpen() ) {
                            try{
                                EntityTransaction tx = em.getTransaction();
                                if( tx.isActive() )
                                    tx.rollback();
                            } finally {
                                em.close( );
                            }
                        }
                    } catch( Exception e ) {
                        // don't bother with exceptions
                        log.debug( "Exception on task future cancel: " + e.toString(), e );
                    }
                }

                EntityManager em = home.getEntityManagerFactory().createEntityManager(  );
                TxFrame tx = new TxFrame( em );
                // cleanup if necessary
                try {
                    try {
                        Task t = em.find( Task.class, tsk.getId() );
                        t.setPostMortem( true );
                        tx.commit( );
                        
                        if( cleanUp ) {
                            log.debug( "Triggering task cleanup" );
                            try{
                                taskAction.setOwnEntityManager( em );
                                taskAction.cleanUpOnFail( t );
                            } catch ( Exception e ) {
                                log.debug( "Exception on cleanup: " + e.toString() );
                            }
                        }

                        // remove task from db
                        log.debug( "Removing task: " + t.getId() );
                        tx.begin( );
                        em.remove( t );
                        tx.commit();
                    } finally {
                        tx.finish();
                        if( em.isOpen() )
                            em.close();
                    }
                } catch ( Exception e ) {
                    log.debug( "Exception on task resource removal: " + e.toString() );
                    e.printStackTrace(  );
                }
            }
        }
    }


}
