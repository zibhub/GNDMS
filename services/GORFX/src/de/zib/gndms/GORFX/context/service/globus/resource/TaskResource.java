package de.zib.gndms.GORFX.context.service.globus.resource;

import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.context.stubs.TaskResourceProperties;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.typecon.common.GORFXClientTools;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.FileTransferResultXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ProviderStageInResultXSDTypeWriter;
import de.zib.gndms.typecon.common.type.RePublishSliceResultXSDTypeWriter;
import de.zib.gndms.typecon.common.type.SliceStageInResultXSDTypeWriter;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import types.*;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.concurrent.Future;


/**
 * The implementation of this TaskResource type.
 *
 * @created by Introduce Toolkit version 1.2
 */
public class TaskResource extends TaskResourceBase
    implements ReloadablePersistentResource<Task, ExtTaskResourceHome> {


    private ExtTaskResourceHome home;
    private TaskAction taskAction;
    private GridResourceModelHandler<Task, ExtTaskResourceHome, TaskResource> mH;
    private Future<?> future;


    /**
     * If the task action isn't finished or failed it will be executed using the
     * GNDMSystem.
     */
    public void executeTask() {
        
        Task tsk = (Task) taskAction.getModel( );
        if(! tsk.getState().equals( TaskState.FINISHED ) || ! tsk.getState().equals( TaskState.FAILED ) )
            future = home.getSystem( ).submitAction( taskAction, getResourceHome().getLog() );
        else
            taskAction.getEntityManager().close();
    }


    public TaskExecutionState getTaskExecutionState() {
        return GORFXTools.getStateOfTask( (Task) taskAction.getModel( ) );
    }


    public TaskExecutionFailure getTaskExecutionFailure() {

        TaskExecutionFailure fail = new TaskExecutionFailure( );
        if( taskAction.getModel( ).getState().equals( TaskState.FAILED ) ) {
            if( taskAction.getModel().getData() != null )
                fail = GORFXTools.failureFromException( (Exception) taskAction.getModel().getData() );
        } else {
            fail.setAllIsFine( new Object() );
        }

        return fail;
    }


    /**
     * Delivers the result of a successfully execudted task.
     *
     * If the task wasn't executed successfully an empty result object is returned.
     */
    public DynamicOfferDataSeqT getTaskExecutionResults() {

        DynamicOfferDataSeqT res;
        // URI uri = GORFXTools.scopedNameToURI( taskAction.getModel().getOfferType().getOfferResultType( ) );
        final String uri = taskAction.getModel().getOfferType( ).getOfferTypeKey();
        final TaskState stat = taskAction.getModel().getState();
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
            throw new IllegalStateException( "Illegal offer type occured" );
        }

        return res;
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
    public Task loadModelById( @NotNull String id ) throws ResourceException {
        if( taskAction != null )
            throw new ResourceException( "task action already loaded" );

        EntityManager em = home.getEntityManagerFactory().createEntityManager(  );
        Task tsk = (Task) mH.loadModelById( em, id );
        try {
            taskAction = getResourceHome().getSystem().getInstanceDir().newTaskAction( em, tsk.getOfferType().getOfferTypeKey() );
            taskAction.initFromModel(em, tsk);
            taskAction.setClosingEntityManagerOnCleanup(true);
            
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
        throw new UnsupportedOperationException( "task resource is readonly" );
    }


    public void loadFromModel( @NotNull Task model ) throws ResourceException {
        // Not required here
        // cause we override the getters.
        // throw new UnsupportedOperationException( "task resource is readonly" );

        // required getter overriding isn't enough
        setTaskExecutionState( getTaskExecutionState() );
        setTerminationTime( taskAction.getModel().getTerminationTime() );
        setTaskExecutionFailure( getTaskExecutionFailure() );
        setTaskExecutionResults( getTaskExecutionResults() );

    }


    @NotNull
    public ExtTaskResourceHome getResourceHome() {
        if( home == null )
            throw new IllegalStateException( "No task resource home set" );
        return home;
    }


    public void setResourceHome( @NotNull ExtTaskResourceHome resourceHomeParam ) {

        if ( home == null ) {
            mH = new GridResourceModelHandler<Task, ExtTaskResourceHome, TaskResource>
                ( Task.class, resourceHomeParam );
            home = resourceHomeParam;
        }
        else
            throw new IllegalStateException("Task resource home already set");
    }


    public void load( ResourceKey resourceKey ) throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {

        if ( getResourceHome().getKeyTypeName().equals( resourceKey.getName() ) ) {
            String id = ( String ) resourceKey.getValue();
            Task tsk = loadModelById( id );
            setResourceKey( resourceKey );
            initialize( new TaskResourceProperties(),
                TaskConstants.RESOURCE_PROPERTY_SET, id );
            loadFromModel( tsk );
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
}
