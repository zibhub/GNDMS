package de.zib.gndms.taskflows.interslicetransfer.server.logic;
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



import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.filetransfer.server.logic.FileTransferTaskAction;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:45:47
 */
public class InterSliceTransferTaskAction extends TaskFlowAction<InterSliceTransferOrder> {


    public InterSliceTransferTaskAction() {
    }

    public InterSliceTransferTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state,
                                boolean isRestartedTask, boolean altTaskState) throws Exception {

        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            InterSliceTransferOrder orq = (InterSliceTransferOrder) task.getORQ();
            InterSliceTransferQuoteCalculator.checkURIs( orq );


            final Task st = task.createSubTask();
	        final EntityManager em = getEmf().createEntityManager();
            st.setId(getUUIDGen().nextUUID());

            st.setTerminationTime( task.getTerminationTime() );

	        FileTransferTaskAction fta = new FileTransferTaskAction(em, getDao(), new Taskling(getDao(), st.getId()));
            fta.setCredentialProvider( getCredentialProvider() );
            fta.setClosingEntityManagerOnCleanup( false );
            fta.setEmf( getEmf( ) );


            //fta.setLog( getLog() );
            fta.call( );
            if( st.getTaskState().equals( TaskState.FINISHED ) ){
                task.setPayload( st.getPayload() );
                task.setTaskState(TaskState.FINISHED);
                if (altTaskState)
                    task.setAltTaskState(null);
            }
            else
                throw (RuntimeException) st.getPayload();
            session.finish();
        }
        finally { session.success(); }
    }


    @NotNull
    public Class<InterSliceTransferOrder> getOrqClass() {
        return InterSliceTransferOrder.class;
    }
}
