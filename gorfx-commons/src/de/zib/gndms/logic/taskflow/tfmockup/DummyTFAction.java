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

import de.zib.gndms.model.gorfx.types.AbstractTaskFlowAction;
import de.zib.gndms.model.gorfx.types.DefaultTaskFlowStatus;
import de.zib.gndms.model.gorfx.types.TaskFlowStatus;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:53
 * @brief
 */
public class DummyTFAction extends AbstractTaskFlowAction<DummyTF> {

    public DummyTFAction() {
        setStatus( new DefaultTaskFlowStatus() );
    }


    public void run() {
        DefaultTaskFlowStatus stat = DefaultTaskFlowStatus.class.cast( getStatus() );

        try {
            StringBuffer sb = new StringBuffer();
            DummyTF tf = getTaskFlow().getOrder();
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
            updateStatus( stat,  TaskFlowStatus.Status.FINISHED  );
        } catch( Exception e ) {
            setFailure( e );
            updateStatus( stat, TaskFlowStatus.Status.FAILED );
        }
    }


    private void updateStatus( DefaultTaskFlowStatus stat, TaskFlowStatus.Status s ) {
        stat.setStatus( s );
        setStatus( stat );
    }


    private void updateProgress( DefaultTaskFlowStatus stat, int i ) {
        stat.setProgress( i );
        setStatus( stat );
    }
}
