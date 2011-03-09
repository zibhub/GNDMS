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

import de.zib.gndms.model.gorfx.types.AbstractTaskAction;
import de.zib.gndms.model.gorfx.types.DefaultTaskStatus;
import de.zib.gndms.model.gorfx.types.Task;
import de.zib.gndms.model.gorfx.types.TaskStatus;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:53
 * @brief
 */
public class DummyTFAction extends AbstractTaskAction<DummyTF> {

    public DummyTFAction() {
    }


    public DummyTFAction( Task<DummyTF> t ) {
        super( t );
    }


    public void onInit( ) throws Exception {

        DefaultTaskStatus stat = new DefaultTaskStatus();
        stat.setMaxProgress( getTask().getModel().getTimes() );
        stat.setProgress( 0 );
        stat.setStatus( TaskStatus.Status.WAITING );
    }


    public void onProgress( ) throws Exception {

        DefaultTaskStatus stat = DefaultTaskStatus.class.cast(  getTask().getStatus() );
        updateStatus( stat,  TaskStatus.Status.RUNNING  );

        StringBuffer sb = new StringBuffer();
        DummyTF tf = (DummyTF) getTask().getModel();
        stat.setMaxProgress( tf.getTimes() );
        for( int i = 0; i < tf.getTimes(); ++i ) {
            sb.append( tf.getMessage() );
            sb.append( '\n' );
            Thread.sleep( tf.getDelay() );
            if( tf.isFailIntentionally()
                && i > tf.getTimes( ) / 2 )
                throw new RuntimeException( "Halp -- I'm failing intentionally" );
            updateProgress( stat, i );
        }
        updateStatus( stat,  TaskStatus.Status.FINISHED  );
    }


    public void onFailed( Exception e )  {
        super.onFailed( e );
        DefaultTaskStatus stat = DefaultTaskStatus.class.cast(  getTask().getStatus() );
        stat.setStatus( TaskStatus.Status.FAILED );
        getTask().setStatus( stat );
    }


    private void updateStatus( DefaultTaskStatus stat, TaskStatus.Status s ) {
        stat.setStatus( s );
        getTask().setStatus( stat );
    }


    private void updateProgress( DefaultTaskStatus stat, int i ) {
        stat.setProgress( i );
        getTask().setStatus( stat );
    }
}
