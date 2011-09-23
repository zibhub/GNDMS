package de.zib.gndms.logic.model.gorfx.taskflow;
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

import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:20
 * @brief Maps taskflow type ids, to their type factories.
 */
public interface TaskFlowProvider {

    boolean exists( String taskFlow );
    List<String> listTaskFlows( );
    TaskFlowInfo getTaskFlowInfo( String taskFlow );
    TaskFlowFactory<?,?> getFactoryForTaskFlow( String taskFlow );

}
