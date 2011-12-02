package de.zib.gndms.taskflows.dummy.server;


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


import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.dummy.client.DummyTaskFlowMeta;
import de.zib.gndms.taskflows.dummy.client.model.DummyOrder;
import de.zib.gndms.taskflows.dummy.client.model.DummyTaskFlowResult;
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
    public Class<DummyOrder> getOrderBeanClass( ) {
        return DummyOrder.class;
    }


    public DummyTFAction() {
        super( DummyTaskFlowMeta.TASK_FLOW_TYPE_KEY );
    }


    public DummyTFAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model ) {

        super( em, dao, model );
        setKey( DummyTaskFlowMeta.TASK_FLOW_TYPE_KEY );
    }


    @Override
    protected void onCreated( @NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState ) throws Exception {

        logger.debug( "CALLED" );
        if( !isRestartedTask ) {
            final Session session = getDao().beginSession();
            try {
                Task task = getTask( session );
                task.setProgress( 0 );
                task.setMaxProgress( (( DelegatingOrder<DummyOrder>) task.getOrder( )).getOrderBean().getTimes() );
                logger.debug( "maxprogess set to: " + task.getMaxProgress() );
                session.success();
            }
            finally { session.finish(); }
            super.onCreated( wid, state, isRestartedTask, altTaskState );    // overridden method implementation
        }
    }


    @Override
    protected void onInProgress( @NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState ) throws Exception {

        StringBuilder out = new StringBuilder();
        DummyOrder order = getOrderBean();
        for( int i = 0; i < order.getTimes(); ++i ) {
            out.append( order.getMessage() );
            out.append( '\n' );
            //if( i % 5 == 0)
                updateProgress( i );
            Thread.sleep( order.getDelay() );
            if( order.isFailIntentionally()
                && i > order.getTimes( ) / 2 )
                throw new RuntimeException( "Halp -- I'm failing intentionally" );
        }
        
        final Session session = getDao().beginSession();
        try {
            getTask( session ).setPayload( new DummyTaskFlowResult( out.toString() ) );
            session.success();
        }
        finally { session.finish(); }

        super.onInProgress( wid, state, isRestartedTask, altTaskState );    // overridden method implementation
    }


    @Override
    public DelegatingOrder<DummyOrder> getOrder() {

        DelegatingOrder<DummyOrder> order = null;

        final Session session = getDao().beginSession();
        try {
            order = ( DelegatingOrder<DummyOrder> ) getTask( session ).getOrder( );
            session.success();
        }
        finally { session.finish(); }

        return order;
    }


    private void updateProgress( int i ) {
        final Session session = getDao().beginSession();
        try {
            getTask( session ).setProgress( i );
            session.success();
        } finally { session.finish(); }
    }
}
