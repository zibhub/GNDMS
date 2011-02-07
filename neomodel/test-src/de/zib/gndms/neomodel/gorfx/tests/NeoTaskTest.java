package de.zib.gndms.neomodel.gorfx.tests;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 07.02.11
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class NeoTaskTest extends NeoTest {

    private static final String TASK_ID = "ca:fe:ba:be";

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

    private void newTask(NeoOfferType ot, String id) {
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
        task.setTerminationTime(Calendar.getInstance());
        task.setSerializedCredential(new byte[] { 1, 2, 3 });
        PermissionInfo permInfo = new PermissionInfo();
        permInfo.setUserName("fonzi");
        task.setPermissionInfo(permInfo);
        PersistentContract contract = new PersistentContract();
        contract.setExpectedSize(15L);
        task.setContract(contract);
        task.setORQ("fufu");
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
    public void deleteTask() {
        NeoTask task = session.findTask(TASK_ID);
        task.delete();

        NeoOfferType ot = session.findOfferType("offerTypeNr1");
        ot.delete();
        session.success();
    }
}
