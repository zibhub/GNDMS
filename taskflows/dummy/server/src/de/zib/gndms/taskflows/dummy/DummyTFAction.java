package de.zib.gndms.taskflows.dummy;


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


import de.zib.gndms.common.model.gorfx.types.DefaultTaskStatus;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:53
 * @brief The action of the dummy task flow.
 *
 * @see DummyOrder
 */
public class DummyTFAction extends TaskFlowAction<DummyOrder> {


    @Override
    public Class<DummyOrder> getOrqClass() {
        return DummyOrder.class;
    }


    public DummyTFAction() {
    }


    public DummyTFAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model ) {
        super( em, dao, model );
    }


    public void onInit( ) throws Exception {

            final Session session = getDao().beginSession();
            try {

                Task task = getTask( session );
                DefaultTaskStatus stat = new DefaultTaskStatus();
                stat.setMaxProgress( getOrderBean().getTimes() );
                stat.setProgress( 0 );
                stat.setStatus( TaskStatus.Status.WAITING );
                getTask().setTaskState( TaskState.IN_PROGRESS );
            }
            finally { session.finish(); }
            // super.onCreated(wid, state, isRestartedTask, altTaskState);
    }



    public void onProgress( ) throws Exception {

        DefaultTaskStatus stat = DefaultTaskStatus.class.cast(  getTask().getTaskState() );
        updateStatus( stat,  TaskStatus.Status.RUNNING  );

        StringBuffer sb = new StringBuffer();
        DummyOrder tf = (DummyOrder ) null; getTask().getORQ();
        for( int i = 0; i < tf.getTimes(); ++i ) {
            sb.append( tf.getMessage() );
            sb.append( '\n' );
            Thread.sleep( tf.getDelay() );
            if( tf.isFailIntentionally()
                && i > tf.getTimes( ) / 2 )
                throw new RuntimeException( "Halp -- I'm failing intentionally" );
            updateProgress( stat, i );
        }
        final Session session = getDao().beginSession();
        try {
            getTask( session ).setPayload( new DummyTaskFlowResult( sb.toString() ) );
        }
        finally { session.finish(); }

        updateStatus( stat,  TaskStatus.Status.FINISHED  );
    }


    public void onFailed( Exception e )  {
       // super.onFailed( e );
       // DefaultTaskStatus stat = DefaultTaskStatus.class.cast(  getTask().getStatus() );
       // stat.setStatus( TaskStatus.Status.FAILED );
       // getTask().setStatus( stat );
    }


    private void updateStatus( DefaultTaskStatus stat, TaskStatus.Status s ) {
        stat.setStatus( s );
       // getTask().setStatus( stat );
    }


    private void updateProgress( DefaultTaskStatus stat, int i ) {
        stat.setProgress( i );
        getTask().setTaskState( TaskState.IN_PROGRESS_UNKNOWN );
    }


    private Task getTask() {
        return null;  // Implement Me. Pretty Please!!!
    }
}
