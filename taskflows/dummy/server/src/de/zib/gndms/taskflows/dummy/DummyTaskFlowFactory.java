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
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.dummy.DummyOrder;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:10
 * @brief Provider of dummy taskflows things.
 */
public class DummyTaskFlowFactory extends DefaultTaskFlowFactory<DummyOrder,DummyQuoteCalculator> {

    private HashMap<String, TaskFlow<DummyOrder>> taskFlows = new HashMap<String, TaskFlow<DummyOrder>>( 10 );
    private TaskStatistics stats = new TaskStatistics();


    public DummyTaskFlowFactory() {
        stats.setType( "DummyOrder" );
    }


    @Override
    public String getTaskFlowKey() {
        return null;  // not required here
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
        TaskFlow<DummyOrder> tf = new CreatableTaskFlow<DummyOrder>();
        String uuid = UUID.randomUUID().toString();
        taskFlows.put( uuid, tf );
       // tf.setId( uuid );
        stats.setActive( stats.getActive() + 1 );
        return tf;
    }


    @Override
    public TaskFlow<DummyOrder> createOrphan() {
        return null;  // not required here
    }


    @Override
    protected TaskFlow<DummyOrder> prepare( TaskFlow<DummyOrder> dummyOrderTaskFlow ) {
        return null;  // not required here
    }


    public boolean adopt( TaskFlow<DummyOrder> taskflow ) {
        return false;  // not required here
    }


    public TaskFlow<DummyOrder> find( String id ) {
        return taskFlows.get( id );
    }


    public void delete( String id ) {
        TaskFlow tf = taskFlows.remove( id );
        /*
        Task t = tf.getTask();
        if( t != null )
            switch ( t.getStatus().getStatus() ) {
                case FINISHED:
                    stats.setFinished( stats.getFinished() + 1 );
                case FAILED:
                    stats.setFailed( stats.getFailed() +1 );
                    break;
                case RUNNING:
                    break;
                case WAITING:
                    break;
                case PAUSED:
                    break;
            }
            */

        stats.setActive( stats.getActive() - 1 );
    }


    public Class getOrderClass() {
        return DummyOrder.class;
    }


    @Override
    public TaskAction createAction() {
        return null;  // not required here
    }


    @Override
    public Iterable<String> depends() {
        return null;  // not required here
    }


    @Override
    public DelegatingOrder<DummyOrder> getOrderDelegate( DummyOrder orq ) {
        return null;  // not required here
    }


    public DelegatingOrder<DummyOrder> getOrderDelegate( TaskFlow<DummyOrder> orq ) {
        return null;  // not required here
    }


}
