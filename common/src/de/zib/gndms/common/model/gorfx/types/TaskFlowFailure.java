package de.zib.gndms.common.model.gorfx.types;
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
 * @date 02.03.11  11:08
 * @brief Interface for task flow failures.
 *
 * Taskflow failures are less technically than TaskFailures. They are
 * commonly showed to a user and thus should be human readable.
 *
 * However they might contain TaskFailures
 */
public interface TaskFlowFailure {

    /** 
     * @brief Checks if an taskflow failed.
     * 
     * @return \c true if the task flow has failed.
     */
    boolean hasFailed();

    /** 
     * @brief A human readable error message.
     * 
     * Its likely that there exist some problems with the Order at this
     * point, thus the user have to correct it.
     *
     * @return A human readable error message. 
     */
    String getFailureMessage();

    /** 
     * @brief Checks if an task failure occurred
     * 
     * @return \c true fi an task failure occurred.
     */
    boolean hasTaskFailure();

    /** 
     * @brief Delivers the task failure object if existing.
     * 
     * @return The task failure.
     */
    TaskFailure getTaskFailure();

}
