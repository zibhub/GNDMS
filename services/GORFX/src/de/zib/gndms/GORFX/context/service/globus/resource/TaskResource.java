package de.zib.gndms.GORFX.context.service.globus.resource;

import de.zib.gndms.GORFX.common.GORFXClientTools;
import de.zib.gndms.GORFX.common.GORFXTools;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.apache.axis.types.URI;
import types.DynamicOfferDataSeqT;
import types.ProviderStageInResultT;
import types.TaskExecutionFailure;
import types.TaskExecutionState;


/**
 * The implementation of this TaskResource type.
 *
 * @created by Introduce Toolkit version 1.2
 */
public class TaskResource extends TaskResourceBase {

    private TaskAction<? extends Task> taskAction;


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
        URI uri = GORFXTools.scopedNameToURI( taskAction.getModel().getType().getOfferResultType( ) );
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
}
