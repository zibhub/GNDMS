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
 * @date 02.03.11  12:32
 * @brief Enum type for control options of a task.
 *
 * If an option is valid depends on the features of the task and its current state.
 */
public enum TaskControl {
    PAUSE,   ///< Pauses the task execution
    RESUME,  ///< Resumes the task execution
    RESTART, ///< Restarts the current task
    ABORT    ///< Requests abort of the task execution
}


