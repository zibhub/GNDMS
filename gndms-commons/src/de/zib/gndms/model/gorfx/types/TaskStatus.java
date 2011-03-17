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
 * @date: 09.02.11, Time: 17:44
 *
 * @brief Interface for task status.
 */
public interface TaskStatus {

    /**
     * @brief Enum for the state of a task;
     */
    enum Status{ FINISHED,  ///< Task has finished (successfully)
                 FAILED, ///< Some exception has occurred and the task is failed
                 RUNNING, ///< Task is still running
                 WAITING, ///< Task hasn't been started yet.
                 PAUSED ///< Task execution was paused.
        }

    Status getStatus();
    int getProgress( );
    int getMaxProgress( );
}
