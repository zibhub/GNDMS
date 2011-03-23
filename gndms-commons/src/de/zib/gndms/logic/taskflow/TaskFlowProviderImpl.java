package de.zib.gndms.logic.taskflow;
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

import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.model.gorfx.types.TaskFlowMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 16.02.11  12:02
 *
 * @brief Mock-up implementation of a a taskflow provider.
 */
public class TaskFlowProviderImpl implements TaskFlowProvider {

    private Map<String, TaskFlowFactory> factories;

    public boolean exists( String taskFlow ) {
        return factories.containsKey( taskFlow );
    }


    public List<String> listTaskFlows() {
        return new ArrayList<String>( factories.keySet() );
    }


    public TaskFlowInfo getTaskFlowInfo( String taskFlow ) {
        return getFactoryForTaskFlow( taskFlow ).getInfo();
    }


    public TaskFlowFactory getFactoryForTaskFlow( String taskFlow ) {
        return factories.get( taskFlow );
    }


    public void setFactories( Map<String, TaskFlowFactory> factories ) {
        this.factories = factories;
    }
}
