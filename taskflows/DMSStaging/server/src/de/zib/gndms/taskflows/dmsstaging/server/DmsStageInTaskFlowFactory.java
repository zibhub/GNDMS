/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.dmsstaging.server;

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInMeta;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.dmsstaging.server.logic.DmsStageInQuoteCalculator;
import de.zib.gndms.taskflows.dmsstaging.server.logic.DmsStageInTaskAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @date: 19.06.12
 * @time: 10:48
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInTaskFlowFactory
        extends DefaultTaskFlowFactory< DmsStageInOrder, DmsStageInQuoteCalculator >
{
    protected DmsStageInTaskFlowFactory( ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY,
                DmsStageInQuoteCalculator.class,
                DmsStageInOrder.class );
    }


    @Override
    protected TaskFlow< DmsStageInOrder > prepare( TaskFlow< DmsStageInOrder > dmsStageInOrderTaskFlow ) {
        return dmsStageInOrderTaskFlow;
    }


    @Override
    protected Map< String, String > getDefaultConfig( ) {
        HashMap< String, String > config = new HashMap< String, String >( 0 );

        return config;
    }


    @Override
    public TaskAction createAction( ) {
        DmsStageInTaskAction taskAction = new DmsStageInTaskAction();
        injectMembers( taskAction );
        return taskAction;
    }
}
