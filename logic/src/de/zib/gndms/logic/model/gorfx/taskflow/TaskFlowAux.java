package de.zib.gndms.logic.model.gorfx.taskflow;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.common.model.gorfx.types.TaskFlowStatus;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;


public class TaskFlowAux {


    /**
     * Constructs a taskflow status object from a task flow.
     *
     * @param tf The taskflow.
     * @return The newly created status object.
     *         <p/>
     *         \note this one never sets the TaskSpecifier.
     */
    public static TaskFlowStatus statusFromTaskFlow( Session session, TaskFlow tf ) {

        TaskFlowStatus state = new TaskFlowStatus();

        if ( tf.getOrder() == null )
            state.setState( TaskFlowStatus.State.NO_ORDER );
        else if ( tf.isUnfulfillableOrder() )
            state.setState( TaskFlowStatus.State.ORDER_UNFULFILLABLE );
        else if ( tf.getQuotes() == null )
            state.setState( TaskFlowStatus.State.NO_QUOTES );
        else if ( tf.getTaskling() == null )
            state.setState( TaskFlowStatus.State.TASK_PREPARED ); // all precautions to start the task have been performed
                                                                  // thus the task is prepared.
        else {
            Taskling taskling = tf.getTaskling();
            Task task = session.findTask( taskling.getId() );
            TaskStatus remoteStatus = TaskTypeConverter.statusFormTask( task );
            switch ( remoteStatus.getStatus() ) {
                case FINISHED:
                case FAILED:
                    state.setState( TaskFlowStatus.State.TASK_DONE );
                    break;
                case RUNNING:
                    state.setState( TaskFlowStatus.State.TASK_RUNNING );
                    break;
                case WAITING:
                    state.setState( TaskFlowStatus.State.TASK_PREPARED );
                    break;
                case PAUSED:
                    state.setState( TaskFlowStatus.State.TASK_PAUSED );
                    break;
            }
            state.setTaskStatus( remoteStatus );
        }

        return state;
    }


    public static TaskFlowStatus statusFromTaskFlow( Dao dao, TaskFlow tf ) {

        Session ses = dao.beginSession();
        TaskFlowStatus state = null;
        try {
            state = statusFromTaskFlow( ses, tf );
            ses.success();
        } finally {
            ses.finish();
        }

        return state;
    }
}