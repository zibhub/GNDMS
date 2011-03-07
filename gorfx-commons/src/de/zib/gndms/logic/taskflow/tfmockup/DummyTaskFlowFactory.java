package de.zib.gndms.logic.taskflow.tfmockup;
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

import de.zib.gndms.logic.taskflow.AbstractQuoteCalculator;
import de.zib.gndms.logic.taskflow.TaskFlowFactory;
import de.zib.gndms.model.gorfx.types.Task;
import de.zib.gndms.model.gorfx.types.TaskFlow;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.model.gorfx.types.TaskStatistics;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:10
 * @brief
 */
public class DummyTaskFlowFactory implements TaskFlowFactory<TaskFlow<DummyTF>> {

    private HashMap<String, TaskFlow<DummyTF>> taskFlows = new HashMap<String, TaskFlow<DummyTF>>( 10 );
    private TaskStatistics stats = new TaskStatistics();


    public DummyTaskFlowFactory() {
        stats.setType( "DummyTF" );
    }


    public AbstractQuoteCalculator getQuoteCalculator() {
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


    public TaskFlow<DummyTF> create() {
        TaskFlow<DummyTF> tf = new TaskFlow<DummyTF>();
        String uuid = UUID.randomUUID().toString();
        taskFlows.put( uuid, tf );
        tf.setId( uuid );
        stats.setActive( stats.getActive() + 1 );
        return tf;
    }


    public TaskFlow<DummyTF> find( String id ) {
        return taskFlows.get( id );
    }


    public void delete( String id ) {
        TaskFlow tf = taskFlows.remove( id );
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

        stats.setActive( stats.getActive() - 1 );
    }


    public Class getOrderClass() {
        return DummyTF.class;
    }
}
