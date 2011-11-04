package de.zib.gndms.taskflows.dummy;/*
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
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:10
 * @brief Provider of dummy taskflows things.
 */
public class DummyTaskFlowFactory extends DefaultTaskFlowFactory<DummyOrder,DummyQuoteCalculator> {

    private TaskStatistics stats = new TaskStatistics();


    public DummyTaskFlowFactory() {

        super( DummyTaskFlowMeta.TASK_FLOW_KEY, DummyQuoteCalculator.class, DummyOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }


    public DummyQuoteCalculator getQuoteCalculator() {
        return new DummyQuoteCalculator( );
    }


    public TaskFlowInfo getInfo() {
        return new TaskFlowInfo() {
            private TaskStatistics statistics = stats;
            public TaskStatistics getStatistics() {
                return statistics;
            }


            public String getDescription() {
                return null;  // not required here
            }
        };
    }


    public TaskFlow<DummyOrder> create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }


    @Override
    protected TaskFlow<DummyOrder> prepare( TaskFlow<DummyOrder> dummyOrderTaskFlow ) {
        return dummyOrderTaskFlow;
    }


    public void delete( String id ) {

        stats.setActive( stats.getActive() - 1 );
        super.delete( id );
    }



    @Override
    public TaskAction createAction() {
        return new DummyTFAction(  );
    }

}
