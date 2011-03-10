package de.zib.gndms.GORFX.action;

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



import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.logic.model.gorfx.FileTransferTaskAction;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.SubTask;
import org.globus.gsi.GlobusCredential;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:45:47
 */
public class InterSliceTransferTaskAction extends ORQTaskAction<InterSliceTransferORQ> {


    public InterSliceTransferTaskAction() {
    }

    public InterSliceTransferTaskAction(@NotNull EntityManager em, @NotNull NeoDao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state,
                                boolean isRestartedTask, boolean altTaskState) throws Exception {

        final NeoSession session = getDao().beginSession();
        try {
            final NeoTask task = getTask(session);
            InterSliceTransferORQCalculator.checkURIs( getOrq( ), (GlobusCredential)
                getCredentialProvider().getCredential() );
            InterSliceTransferORQCalculator.checkURIs( orq );

	        final EntityManager em = getEmf().createEntityManager();
            final NeoTask st = task.createSubTask();

            st.setId( getUUIDGen().nextUUID() );
            st.setTerminationTime( task.getTerminationTime() );


	        FileTransferTaskAction fta = new FileTransferTaskAction(em, getDao(), new Taskling(getDao(), st.getId()));
            fta.setCredentialProvider( getCredentialProvider() );
			// todo verify closing
            fta.setClosingEntityManagerOnCleanup( true );
            fta.setEmf( getEmf( ) );

            fta.setLog( getLog() );
            fta.call( );
            if( st.getTaskState().equals( TaskState.FINISHED ) ){
                task.setPayload( st.getPayload() );
                task.setTaskState(TaskState.FINISHED);
                if (altTaskState)
                    task.setAltTaskState(null);
				// todo maybe delete st?
            }
            else
                throw (RuntimeException) st.getPayload();
            session.success();
        }
        finally { session.finish(); }
    }


    @NotNull
    public Class<InterSliceTransferORQ> getOrqClass() {
        return InterSliceTransferORQ.class;
    }
}
