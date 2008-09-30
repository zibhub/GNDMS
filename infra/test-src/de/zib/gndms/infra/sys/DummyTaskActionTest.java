package de.zib.gndms.infra.sys;

import de.zib.gndms.logic.model.DummyTaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:14:27
 */
public class DummyTaskActionTest extends SysTestBase {
    @Parameters({"gridName"})
    public DummyTaskActionTest(@Optional("c3grid") String gridName) {
        super(gridName);
    }


    @Test(groups = { "db", "sys", "action", "task"})
    public void runSuccesfulDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        eraseDatabase();
        runDatabase();
        final DummyTaskAction action = new DummyTaskAction();
        action.setSuccessRate(1.0d);
        final Future<Task> serializableFuture = getSys().submitAction(action);
        assert serializableFuture.get().getState().equals(TaskState.FINISHED);
        shutdownDatabase();
    }

    @Test(groups = { "db", "sys", "action", "task"})
    public void runFailedDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        eraseDatabase();
        runDatabase();
        final DummyTaskAction action = new DummyTaskAction();
        action.setSuccessRate(0.0d);
        final Future<Task> serializableFuture = getSys().submitAction(action);
        assert serializableFuture.get().getState().equals(TaskState.FAILED);
        shutdownDatabase();
    }


    @Test(groups = { "db", "sys", "action", "task"})
    @SuppressWarnings({ "FeatureEnvy" })
    public void runTwoDummyActions()
            throws ExecutionException, InterruptedException, ResourceException {
        eraseDatabase();
        runDatabase();
        final DummyTaskAction action = new DummyTaskAction();
        action.setSuccessRate(1.0d);
        action.setSleepInProgress(4000L);
        final Future<Task> serializableFuture = getSys().submitAction(action);
        final DummyTaskAction action2 = new DummyTaskAction();
        action2.setSleepInProgress(4000L);
        action2.setSuccessRate(0.0d);
        final Future<Task> serializableFuture2 = getSys().submitAction(action2);
        assert serializableFuture2.get().getState().equals(TaskState.FAILED);
        assert serializableFuture.get().getState().equals(TaskState.FINISHED);
        shutdownDatabase();
    }

    @Test(groups = { "db", "sys", "action", "task"})
    public void runInterruptedDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        eraseDatabase();
        DummyTaskAction action = preInterruption();
        postInterruption(action.getModel().getId());
    }


    @SuppressWarnings({ "MagicNumber" })
    private DummyTaskAction preInterruption() throws ResourceException {
        runDatabase();
        DummyTaskAction action = new DummyTaskAction();
        action.setSuccessRate(1.0d);
        action.setSleepInProgress(10000000L);
        final Future<Task> serializableFuture = getSys().submitAction(action);
        // make sure action is in_progress
        try {
            Thread.sleep(4000L);
        }
        catch(InterruptedException e) {
            // onward;
        }
        shutdownDatabase();
        return action;
    }

    private void postInterruption(final @NotNull Object pk)
            throws ResourceException, InterruptedException, ExecutionException {
        runDatabase();
        final EntityManager newEM = getSys().getEntityManagerFactory().createEntityManager();
        final DummyTaskAction action = new DummyTaskAction(newEM, pk);
        action.setSuccessRate(1.0d);
        final Future<Task> continuedFuture = getSys().submitAction(action);
        assert continuedFuture.get().getState().equals(TaskState.FINISHED);
        shutdownDatabase();
    }
}
