/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.logic.model.gorfx.taskflow;

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.model.common.repository.Dao;
import de.zib.gndms.model.common.repository.TransientDao;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.neomodel.gorfx.TaskFlow;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 20.07.11  16:33
 * @brief
 */
public abstract class DefaultTaskFlowFactory<O extends Order, C extends AbstractQuoteCalculator<O>> implements TaskFlowFactory<O, C> {

    private String taskFlowKey;
    private Class<C> calculatorClass;
    private Class<O> orderClass;
    private final Dao<String, TaskFlow<O>, Object> taskFlows = new TransientDao<String, TaskFlow<O>, Object>() {

        @Override
        protected TaskFlow<O> newModel( String key ) {
            return new CreatableTaskFlow<O>( key );
        }


        @Override
        public String create() {
            String id = UUID.randomUUID().toString();
            TaskFlow<O> tf = newModel( id );
            add( tf, id );
            return id;
        }
    };


    @Override
    public String getTaskFlowKey() {
        return taskFlowKey;  // not required here
    }


    @Override
    public int getVersion() {
        return 0;
    }


    @Override
    public C getQuoteCalculator() {
        try {
            return calculatorClass.newInstance();
        // one of the two exceptions are thrown s.th. is wrong with the calculator class
        // and need debugging, so rethrowing is ok
        } catch ( InstantiationException e ) {
            throw new RuntimeException( e );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }


    @Override
    public TaskFlowInfo getInfo() {
        return null;  // not required here
    }


    @Override
    public TaskFlow<O> create() {

        String key = taskFlows.create();
        return taskFlows.get( key );
    }


    @Override
    public TaskFlow<O> createOrphan() {
        return prepare( new CreatableTaskFlow<O>( ) );
    }


    /**
     * Applies taskFlow specific stuff a newly created taskFlow instance.
     *
     * Stuff is usually the injection of required handlers and the like.
     * @param taskFlow A newly created taskflow.
     * @return The prepared in-parameter.
     */
    protected abstract TaskFlow<O> prepare( TaskFlow<O> taskFlow );


    @Override
    public boolean adopt( TaskFlow<O> taskflow ) {

        String key = taskflow.getId();
        try {
            taskFlows.get( key );
        } catch( NoSuchElementException e ) {
            taskFlows.add( taskflow, key );
            return true;
        }

        return false;
    }


    @Override
    public TaskFlow<O> find( String id ) {
        try {
            taskFlows.get( id );
        } catch( NoSuchElementException e ) {
            // intentionally
        }
        return null;
    }


    @Override
    public void delete( String id ) {
        taskFlows.deleteByKey( id );
    }


    @Override
    public Class<O> getOrderClass() {
        return orderClass;
    }


    @Override
    public Iterable<String> depends() {
        return new ArrayList<String>( 0 );
    }


    @Override
    public DelegatingOrder<O> getOrderDelegate( O order ) {
        return new DelegatingOrder<O>( order );
    }


    // i'd love to put this class in the factories interface where it belongs,
    // but thanks to poor design of the java language this is not possible...
    protected static class CreatableTaskFlow<O extends Order> extends TaskFlow<O> {

        public CreatableTaskFlow() {
        }


        public CreatableTaskFlow( String id ) {
            super( id );
        }


        public void setId( String id ) {
            super.setId( id );
        }
    }


    protected void setTaskFlowKey( String taskFlowKey ) {
        this.taskFlowKey = taskFlowKey;
    }


    protected void setCalculatorClass( Class<C> calculatorClass ) {
        this.calculatorClass = calculatorClass;
    }


    protected void setOrderClass( Class<O> orderClass ) {
        this.orderClass = orderClass;
    }
}
