package de.zib.gndms.GORFX.context.service.globus.resource;

import de.zib.gndms.GORFX.common.GORFXClientTools;
import de.zib.gndms.GORFX.common.GORFXTools;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.context.stubs.TaskResourceProperties;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import org.apache.axis.types.URI;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.InvalidResourceKeyException;
import types.DynamicOfferDataSeqT;
import types.ProviderStageInResultT;
import types.TaskExecutionFailure;
import types.TaskExecutionState;

import javax.persistence.EntityManager;
import java.util.concurrent.Future;


/**
 * The implementation of this TaskResource type.
 *
 * @created by Introduce Toolkit version 1.2
 */
public class TaskResource extends TaskResourceBase
    implements ReloadablePersistentResource<Task, ExtTaskResourceHome> {


    private ExtTaskResourceHome home;
    private TaskAction<? extends Task> taskAction;
    private GridResourceModelHandler<Task, ExtTaskResourceHome, TaskResource> mH;
    private Future<?> future;


    /**
     * If the task action isn't finished or failed it will be executed using the
     * GNDMSystem.
     */
    public void executeTask() {
        
        Task tsk = taskAction.getModel( );
        if(! tsk.getState().equals( TaskState.FINISHED ) || ! tsk.getState().equals( TaskState.FAILED ) )
            future = home.getSystem( ).submitAction( taskAction );
    }


    public TaskExecutionState getTaskExecutionState() {
        return GORFXTools.getStateOfTask( taskAction.getModel( ) );
    }


    public TaskExecutionFailure getTaskExecutionFailure() {

        TaskExecutionFailure fail;
        if( taskAction.getModel( ).getState().equals( TaskState.FAILED ) ) {
            fail = (TaskExecutionFailure) taskAction.getModel().getData();
        } else {
            fail = new TaskExecutionFailure( );
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
        URI uri = GORFXTools.scopedNameToURI( taskAction.getModel().getOfferType().getOfferResultType( ) );
        TaskState stat = taskAction.getModel().getState();
        if( uri.equals( GORFXClientTools.getProviderStageInURI() ) ) {
            if( stat.equals( TaskState.FINISHED ) )
                // maybe convert result from some result model type
                res = ( ProviderStageInResultT ) taskAction.getModel().getData( );
            else
                res = new ProviderStageInResultT();
        } else if( uri.equals( GORFXClientTools.getFileTransferURI() ) ) {

            if( stat.equals( TaskState.FINISHED ) )
                res = ( ProviderStageInResultT ) taskAction.getModel().getData( );
            else
                res = new ProviderStageInResultT();
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


    public void setTaskAction( TaskAction taskAction ) {
        this.taskAction = taskAction;
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
        taskAction = new TaskAction( em, tsk );
        executeTask( );
        return tsk;
    }


    public void loadViaModelId( @NotNull String id ) throws ResourceException {
        // Not required here
        // cause we override the getters.
        throw new UnsupportedOperationException( "task resource is readonly" );
    }


    public void loadFromModel( @NotNull Task model ) throws ResourceException {
        // Not required here
        // cause we override the getters.
        throw new UnsupportedOperationException( "task resource is readonly" );
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
            Task sl = loadModelById( id );
            setResourceKey( resourceKey );
            initialize( new TaskResourceProperties(),
                TaskConstants.RESOURCE_PROPERTY_SET, id );
        }
        else
            throw new InvalidResourceKeyException("Invalid resourceKey name");
    }


    public void store() throws ResourceException {
        // Not required here
        // task persists itself
    }


    public String getID( ) {
        return (String) super.getID();
    }
}
