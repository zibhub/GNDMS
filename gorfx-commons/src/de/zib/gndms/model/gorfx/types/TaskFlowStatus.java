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

/**
 * @author try ma ik jo rr a zib
 * @date 01.03.11  13:57
 * @brief
 */
public class TaskFlowStatus {

    enum State {
        TASK_STARTED, ///< Taskflow has a running task.
        TASK_PREPARED, ///< Taskflow's task can be executed.
        TASK_DONE, ///< Taskflow's task has finished or failed.
        ORDER_UNFULFILLABLE ///< Task order is unfulfillable
    };

    private TaskStatus taskStatus; ///< The status of the task associated with the task flow.
    private String task; ///< The id of the task associated with the task flow.

}
