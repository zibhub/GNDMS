package de.zib.gndms.neomodel.gorfx.tests;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import org.neo4j.graphdb.Node;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 07.02.11
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class NeoTaskTest extends NeoTest {

    private static final String TASK_ID = "ca:fe:ba:be";
    private static final Calendar CLASS_STARTUP_TIME = Calendar.getInstance();

    @Test( groups = { "neo" } )
    public void createTask() {
        NeoOfferType ot = session.createOfferType();
        ot.setId("offerTypeNr1");
        ot.setCalculatorFactoryClassName("cfn");
        ot.setTaskActionFactoryClassName("tfn");
        ot.setConfigMapData(new HashMap<String,String>());
        ot.setOfferArgumentType(new ImmutableScopedName("a", "b"));
        ot.setOfferResultType(new ImmutableScopedName("x", "z"));

        newTask(ot, TASK_ID);

        session.success();
    }

    private NeoTask newTask(NeoOfferType ot, String id) {
        NeoTask task = session.createTask();
        task.setBroken(true);
        task.setDone(true);
        task.setFaultString("Faulty Towers");
        task.setPostMortem(true);
        task.setMaxProgress(101);
        task.setProgress(5);
        task.setTaskState(TaskState.FAILED);
        task.setDescription("Will #fail");
        task.setId(id);
        task.setOfferType(ot);
        task.setPayload(1);
        task.setTerminationTime(CLASS_STARTUP_TIME);
        task.setSerializedCredential(new byte[] { 1, 2, 3 });
        PermissionInfo permInfo = new PermissionInfo();
        permInfo.setUserName("fonzi");
        task.setPermissionInfo(permInfo);
        PersistentContract contract = new PersistentContract();
        contract.setExpectedSize(15L);
        task.setContract(contract);
        task.setORQ("fufu");
        return task;
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createTask" } )
    public void checkTask() {
        NeoTask task = session.findTask(TASK_ID);
        assertEquals(task.getId(), TASK_ID);
        assertEquals(task.isBroken(), true);
        assertEquals(task.isDone(), true);
        assertEquals(task.isPostMortem(), true);
        assertEquals(task.getMaxProgress(), 101);
        assertEquals(task.getProgress(), 5);
        assertEquals(task.getTaskState(), TaskState.FAILED);
        assertEquals(task.getDescription(), "Will #fail");
        assertEquals(task.getFaultString(), "Faulty Towers");
        assertEquals(task.getPayload(), 1);

        NeoOfferType ot = session.findOfferType("offerTypeNr1");
        assertEquals(task.getOfferType().getId(), ot.getId());
        assertEquals(task.getOfferType().getOfferResultType().getLocalName(), "z");
        assertEquals((long) task.getContract().getExpectedSize(), 15L);
        assertEquals(task.getORQ(), "fufu");
        assertEquals(task.getPermissionInfo().getUserName(), "fonzi");
        assert(task.getTerminationTime().getTimeInMillis() <= System.currentTimeMillis());

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "checkTask" } )
    public void createSubTasks() {
        NeoOfferType ot = session.findOfferType("offerTypeNr1");

        NeoTask t1 = newTask(ot, "t1");
        NeoTask t2 = newTask(ot, "t2");
        NeoTask t3 = newTask(ot, "t3");
        NeoTask t4 = newTask(ot, "t4");
        NeoTask t5 = newTask(ot, "t5");
        NeoTask t6 = newTask(ot, "t6");

        t2.setParent(t1);
        t3.setParent(t4);
        t4.setParent(t2);
        t6.setParent(t2);

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createSubTasks" } )
    public void verifySubTasks() {
        NeoTask t1 = session.findTask("t1");
        NeoTask t2 = session.findTask("t2");
        NeoTask t3 = session.findTask("t3");
        NeoTask t4 = session.findTask("t4");
        NeoTask t5 = session.findTask("t5");
        NeoTask t6 = session.findTask("t6");

        assertEquals(t2.getParent(), t1);
        assertEquals(t3.getParent(), t4);
        assertEquals(t3.getParent(), t4);
        assertEquals(t4.getParent(), t2);
        assertEquals(t6.getParent(), t2);
        assertEquals(t6.getRootTask(), t1);

        assert(! t6.isRootTask());
        assert(t5.isRootTask());
        assert(t6.isBelowTask(t2));
        assert(t6.isBelowTask(t6));
        assert(t6.isBelowTask(t1));
        try {
            t1.setParent(t6);
            assertTrue(false, "Should have thrown");
        }
        catch (IllegalArgumentException iae) {}
        assert(! t6.isBelowTask(t3));

        final List<NeoTask> subTasks = new ArrayList<NeoTask>();
        for (NeoTask task : t2.getSubTasks())
            subTasks.add(task);

        assertEquals(subTasks.size(), 2);
        assert(subTasks.contains(t4));
        assert(subTasks.contains(t6));

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "verifySubTasks" } )
    public void deleteAndListSubTasks() {
        NeoTask t1 = session.findTask("t1");
        NeoTask t2 = session.findTask("t2");
        NeoTask t3 = session.findTask("t3");
        NeoTask t4 = session.findTask("t4");
        NeoTask t5 = session.findTask("t5");
        NeoTask t6 = session.findTask("t6");

        {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(System.currentTimeMillis());
            final Iterable<Node> iterable = session.listTasksDeadBeforeTimeout(instance);
            final List<Node> list = new LinkedList<Node>();
            for (Node node: iterable)
                list.add(node);
            assertEquals(list.size(), 6);
        }

        {
            final Iterable<Node> iterable = session.listTasksByState(TaskState.FAILED);
            final List<Node> list = new LinkedList<Node>();
            for (Node node: iterable)
                list.add(node);
            assertEquals(list.size(), 6);
        }

        t1.delete(session);
        t2.delete(session);
        t3.delete(session);
        t4.delete(session);
        t5.delete(session);
        t6.delete(session);

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "checkTask" } )
    public void deleteTask() {
        NeoTask task = session.findTask(TASK_ID);
        task.delete();

        NeoOfferType ot = session.findOfferType("offerTypeNr1");
        ot.delete();
        session.success();
    }
}
