package de.zib.gndms.gndmc.gorfx;
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

import de.zib.gndms.common.model.gorfx.types.TaskFailure;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 04.04.12  16:30
 * @brief
 */
public interface TaskStatusHandler {

    /**
     * @brief Handler method for the current task status
     *
     * This method can be used to delegate the current task progress to
     * the user.
     *
     * @param stat The current task status.
     */
    void handleStatus( TaskStatus stat );

    void handleResult( TaskResult body );

    void handleFailure( TaskFailure body );
}
