package de.zib.gndms.model.gorfx.types;
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

import de.zib.gndms.rest.Specifier;

/**
 * @author try ma ik jo rr a zib
 * @date 01.03.11  13:57
 * @brief Status of a task flow.
 *
 * The taskflow status contains the current state of the taskflow execution chain, e.g. is valid order available,
 * were quotes computed and the like. Additionally it is able to box the task status, if a task is available an running.
 */
public class TaskFlowStatus {

    /**
     * The state enumerator for the taskflow. NOT the task!
     */
    enum State {
        NO_ORDER, ///< Taskflow without order.
        NO_QUOTES, ///< Taskflow without order.
        TASK_STARTED, ///< Taskflow has a running taskSpecifier.
        TASK_PREPARED, ///< Taskflow's taskSpecifier can be executed.
        TASK_PAUSED, ///< Taskflow's taskSpecifier can be executed.
        TASK_DONE, ///< Taskflow's taskSpecifier has finished or failed.
        ORDER_UNFULFILLABLE ///< Task order is unfulfillable
    };

    private State state; ///< The state of the taskflow itself;
    private TaskStatus taskStatus; ///< The status of the taskSpecifier associated with the taskSpecifier flow.
    private Specifier<Void> taskSpecifier; ///< The id of the taskSpecifier associated with the taskSpecifier flow.


    /**
     * @brief Delivers the value of ::state.
     * 
     * @return The value of ::state.
     */
    public State getState() {
        return state;
    }


    /**
     * @brief Sets the value of ::state to \e state.
     * 
     * @param state The new value of ::state.
     */
    public void setState( State state ) {
        this.state = state;
    }


    /**
     * @brief Delivers the value of ::taskStatus.
     * 
     * @return The value of ::taskStatus.
     */
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }


    /**
     * @brief Sets the value of ::taskStatus to \e taskStatus.
     * 
     * @param taskStatus The new value of ::taskStatus.
     */
    public void setTaskStatus( TaskStatus taskStatus ) {
        this.taskStatus = taskStatus;
    }


    /**
     * @brief Delivers the value of ::taskSpecifier.
     * 
     * @return The value of ::taskSpecifier.
     */
    public Specifier<Void> getTaskSpecifier() {
        return taskSpecifier;
    }


    /**
     * @brief Sets the value of ::taskSpecifier to \e taskSpecifier.
     * 
     * @param taskSpecifier The new value of ::taskSpecifier.
     */
    public void setTaskSpecifier( Specifier<Void> taskSpecifier ) {
        this.taskSpecifier = taskSpecifier;
    }


    /**
     * Constructs a taskflow status object from a task flow.
     *
     * @param tf The taskflow.
     * @return The newly created status object.
     *
     * \note this one never sets the TaskSpecifier.
     */
    public static TaskFlowStatus fromTaskFlow( TaskFlow tf ) {

        TaskFlowStatus state = new TaskFlowStatus();

        if( tf.getOrder() == null )
            state.setState( State.NO_ORDER );
        else if( tf.isUnfulfillableOrder() )
            state.setState( State.ORDER_UNFULFILLABLE );
        else if( tf.getQuotes() == null )
            state.setState( State.NO_QUOTES );
        else if ( tf.getTask() == null )
            state.setState( State.TASK_PREPARED );
        else {
            Task t = tf.getTask( );
            TaskStatus ts = t.getStatus();
            switch ( ts.getStatus() ) {
                case FINISHED:
                case FAILED:
                    state.setState( TaskFlowStatus.State.TASK_DONE );
                    break;
                case RUNNING:
                    state.setState( TaskFlowStatus.State.TASK_STARTED );
                    break;
                case WAITING:
                    state.setState( State.TASK_PREPARED );
                    break;
                case PAUSED:
                    state.setState( State.TASK_PAUSED );
                    break;
            }
            state.setTaskStatus( ts );
        }

        return state;
    }
}
