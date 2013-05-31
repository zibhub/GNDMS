package de.zib.gndms.neomodel.gorfx.tests;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * NeoTaskTest
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NeoTaskTest extends NeoTest {

    private static final String TASK_ID = "ca:fe:ba:be";
    private static final DateTime CLASS_STARTUP_TIME = new DateTime();

/**    @Test( groups = { "neo" } )
    public void createTask() {
        TaskFlowType ot = session.createTaskFlowType();
        ot.setId("offerTypeNr1");
        ot.setCalculatorFactoryClassName("cfn");
        ot.setTaskActionFactoryClassName("tfn");
        ot.setConfigMapData(new HashMap<String,String>());

        newTask(ot, TASK_ID);

        session.success();
    } 

    private Task newTask(TaskFlowType ot, String id) {
        Task task = session.createTask();
        task.setBroken(true);
        task.setDone(true);
        task.setFaultString("Faulty Towers");
        task.setMaxProgress(101);
        task.setProgress(5);
        task.setTaskState(TaskState.FAILED);
        task.setDescription("Will #fail");
        task.setId(id);
        task.setTaskFlowType(ot);
        task.setPayload(1);
        task.addCause(new RuntimeException("LOL"));
        task.setTerminationTime(CLASS_STARTUP_TIME);
        task.setSerializedCredential(new byte[] { 1, 2, 3 });
        PermissionInfo permInfo = new PermissionInfo();
        permInfo.setUserName("fonzi");
        task.setPermissionInfo(permInfo);
        PersistentContract contract = new PersistentContract();
        contract.setExpectedSize(15L);
        task.setContract(contract);
        task.setOrder("fufu");
        return task;
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createTask" } )
    public void checkTask() {
        Task task = session.findTask(TASK_ID);
        assertEquals(task.getId(), TASK_ID);
        assertEquals(task.isBroken(), true);
        assertEquals(task.isDone(), true);
        assertEquals(task.getMaxProgress(), 101);
        assertEquals(task.getProgress(), 5);
        assertEquals(task.getTaskState(), TaskState.FAILED);
        assertEquals(task.getDescription(), "Will #fail");
        assertEquals(task.getFaultString(), "Faulty Towers");
        assertEquals(task.getPayload(), 1);
        assertEquals(task.getCause().getLast().getMessage(), "LOL");


        TaskFlowType ot = session.findTaskFlowType("offerTypeNr1");
        assertEquals(task.getTaskFlowType().getId(), ot.getId());
        assertEquals((long) task.getContract().getExpectedSize(), 15L);
        assertEquals(task.getOrder( ), "fufu");
        assertEquals(task.getPermissionInfo().getUserName(), "fonzi");
        assert(task.getTerminationTime().getMillis() <= System.currentTimeMillis());

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "checkTask" } )
    public void createSubTasks() {
        TaskFlowType ot = session.findTaskFlowType("offerTypeNr1");

        Task t1 = newTask(ot, "t1");
        Task t2 = newTask(ot, "t2");
        Task t3 = newTask(ot, "t3");
        Task t4 = newTask(ot, "t4");
        Task t5 = newTask(ot, "t5");
        Task t6 = newTask(ot, "t6");

        t2.setParent(t1);
        t3.setParent(t4);
        t4.setParent(t2);
        t6.setParent(t2);

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createSubTasks" } )
    public void verifySubTasks() {
        Task t1 = session.findTask("t1");
        Task t2 = session.findTask("t2");
        Task t3 = session.findTask("t3");
        Task t4 = session.findTask("t4");
        Task t5 = session.findTask("t5");
        Task t6 = session.findTask("t6");

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

        final List<Task> subTasks = new ArrayList<Task>();
        for (Task task : t2.getSubTasks())
            subTasks.add(task);

        assertEquals(subTasks.size(), 2);
        assert(subTasks.contains(t4));
        assert(subTasks.contains(t6));

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "verifySubTasks" } )
    public void deleteAndListSubTasks() {
        Task t1 = session.findTask("t1");
        Task t2 = session.findTask("t2");
        Task t3 = session.findTask("t3");
        Task t4 = session.findTask("t4");
        Task t5 = session.findTask("t5");
        Task t6 = session.findTask("t6");

        {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(System.currentTimeMillis());
            final Iterable<Task> iterable = session.listTasksDeadBeforeTimeout(instance);
            final List<Task> list = new LinkedList<Task>();
            for (Task task : iterable)
                list.add(task);
            assertEquals(list.size(), 6);
        }

        {
            final Iterable<Task> iterable = session.listTasksByState(TaskState.FAILED);
            final List<Task> list = new LinkedList<Task>();
            for (Task task : iterable)
                list.add(task);
            assertEquals(list.size(), 6);
        }

        {
            final Iterable<Task> iterable = session.listTasks();
            final List<Task> list = new LinkedList<Task>();
            for (Task task : iterable)
                list.add(task);
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
        Task task = session.findTask(TASK_ID);
        task.delete();

        TaskFlowType ot = session.findTaskFlowType("offerTypeNr1");
        ot.delete();
        session.success();
    }*/
}
