package de.zib.gndms.infra.system;

import de.zib.gndms.logic.model.DummyTaskAction;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
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
    public static final Object lock = new Object();

    private static final Log log = LogFactory.getLog(DummyTaskActionTest.class);


    @Parameters({"gridName"})
    public DummyTaskActionTest(@Optional("c3grid") String gridName) {
        super(gridName);
    }

    protected @NotNull Task createInitialTask(String id) {
        final Task task = new Task();
        task.setId(id);
        PersistentContract contract = new PersistentContract();
        DateTime dt = new DateTime().toDateTimeISO();
        contract.setAccepted(dt.toGregorianCalendar());
        contract.setDeadline(dt.plusYears(2).toGregorianCalendar());
        contract.setResultValidity(dt.plusYears(2).toGregorianCalendar());
        task.setDescription("Dummy");
        task.setTerminationTime(contract.getResultValidity());
        task.setOfferType(null);
        task.setOrq("null");
        task.setContract(contract);
        return task;
    }




    @Test(groups = { "db", "sys", "action", "task"})
    public void runSuccesfulDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        synchronized(lock) {
            eraseDatabase();
            runDatabase();
            final EntityManager em = getSys().getEntityManagerFactory().createEntityManager();
            final DummyTaskAction action = new DummyTaskAction(em, createInitialTask(nextUUID()));
            action.setSuccessRate(1.0d);
            final Future<AbstractTask> serializableFuture = getSys().submitAction(action, log);
            assert serializableFuture.get().getState().equals(TaskState.FINISHED);
            shutdownDatabase();
        }
    }


    private String nextUUID() {
        return getSys().nextUUID();
    }


    @Test(groups = { "db", "sys", "action", "task"})
    public void runFailedDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        synchronized (lock) {
            eraseDatabase();
            runDatabase();
            final EntityManager em = getSys().getEntityManagerFactory().createEntityManager();

            final DummyTaskAction action = new DummyTaskAction(em, createInitialTask(nextUUID()));
            action.setSuccessRate(0.0d);
            final Future<AbstractTask> serializableFuture = getSys().submitAction(action, log);
            assert serializableFuture.get().getState().equals(TaskState.FAILED);
            shutdownDatabase();
        }
    }


    @Test(groups = { "db", "sys", "action", "task"})
    @SuppressWarnings({ "FeatureEnvy", "MagicNumber" })
    public void runTwoDummyActions()
            throws ExecutionException, InterruptedException, ResourceException {
        synchronized (lock) {
            eraseDatabase();
            runDatabase();
            final EntityManager em = getSys().getEntityManagerFactory().createEntityManager();

            final DummyTaskAction action = new DummyTaskAction(em, createInitialTask(nextUUID()));
            // action.setClosingEntityManagerOnCleanup(false);
            action.setSuccessRate(1.0d);
            action.setSleepInProgress(4000L);
            final Future<AbstractTask> serializableFuture = getSys().submitAction(action, log);

            final EntityManager em2 = getSys().getEntityManagerFactory().createEntityManager();
            final DummyTaskAction action2 = new DummyTaskAction(em2, createInitialTask(nextUUID()));
            action2.setSleepInProgress(4000L);
            action2.setSuccessRate(0.0d);
            // action2.setClosingEntityManagerOnCleanup(false);
            final Future<AbstractTask> serializableFuture2 = getSys().submitAction(action2, log);
            assert serializableFuture2.get().getState().equals(TaskState.FAILED);
            assert serializableFuture.get().getState().equals(TaskState.FINISHED);
            shutdownDatabase();
        }
    }
    
    @Test(groups = { "db", "sys", "action", "task"})
    public void runInterruptedDummyAction()
            throws ExecutionException, InterruptedException, ResourceException {
        synchronized (lock) {
            eraseDatabase();
            DummyTaskAction action = preInterruption();
            postInterruption(action.getModel().getId());
        }
    }


    @SuppressWarnings({ "MagicNumber" })
    private DummyTaskAction preInterruption() throws ResourceException {
        runDatabase();
        final EntityManager em = getSys().getEntityManagerFactory().createEntityManager();
        
        DummyTaskAction action = new DummyTaskAction(em, createInitialTask(nextUUID()));
        action.setSuccessRate(1.0d);
        action.setSleepInProgress(10000000L);
        final Future<AbstractTask> serializableFuture = getSys().submitAction(action, log);
        // make sure action is in_progress
        while (! action.getModel().isDone() && ! action.getModel().getState().equals(TaskState.IN_PROGRESS))
            try {
                Thread.sleep(4000L);
            }
            catch(InterruptedException e) {
                // onward;
            }
        shutdownDatabase();
        return action;
    }

    private void postInterruption(final @NotNull String pk)
            throws ResourceException, InterruptedException, ExecutionException {
        runDatabase();
        final EntityManager newEM = getSys().getEntityManagerFactory().createEntityManager();
        final DummyTaskAction action = new DummyTaskAction(newEM, pk);
        action.setSuccessRate(1.0d);
        final Future<AbstractTask> continuedFuture = getSys().submitAction(action, log);
        assert continuedFuture.get().getState().equals(TaskState.FINISHED);
        shutdownDatabase();
    }
}
