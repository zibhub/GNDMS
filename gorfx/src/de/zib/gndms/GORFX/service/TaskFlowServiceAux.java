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

package de.zib.gndms.GORFX.service;

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowFactory;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowProvider;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public final class TaskFlowServiceAux {

    public static <T extends Order> HttpStatus setAndValidateOrder( Order order, TaskFlow<T> taskFlow, TaskFlowFactory<T, ?> factory ) {

        DelegatingOrder<T> delegate = setOrderAsDelegate( order, taskFlow, factory );
        delegate.setActId( UUID.randomUUID().toString() );

        return validateOrder( factory, delegate );
    }


    public static <T extends Order> DelegatingOrder<T> setOrderAsDelegate( Order order, TaskFlow<T> taskFlow, TaskFlowFactory<T, ?> factory ) {
        Class<T> orderClass = factory.getOrderClass();
        //DelegatingOrder<T> delegate = factory.getOrderDelegate( orderClass.cast( order ) );
        taskFlow.getOrder().setOrderBean( orderClass.cast( order ) );

        return taskFlow.getOrder();
    }


    public static <T extends Order> HttpStatus validateOrder( TaskFlowFactory<T, ?> factory, DelegatingOrder<T> delegate ) {
        final AbstractQuoteCalculator<T> qc;
        try {
            qc = factory.getQuoteCalculator();
        }
        catch( MandatoryOptionMissingException e ) {
            return HttpStatus.BAD_REQUEST;
        }
        qc.setOrder( delegate );

        try {
            if ( qc.validate() )
                return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.BAD_REQUEST;
    }


    public static <T extends Order> List<Quote> createQuotes( TaskFlowFactory<T,?> tff, TaskFlow<T> tf ) throws Exception {

        AbstractQuoteCalculator<T> qc = tff.getQuoteCalculator();
        qc.setOrder( tf.getOrder() );
        List<Quote> quoteList;
        if ( tf.hasPreferredQuote() )
            quoteList = qc.createQuotes( tf.getPreferredQuote() );
        else
            quoteList = qc.createQuotes();

        tf.setQuotes( quoteList );
        return quoteList;
    }


    private TaskFlowServiceAux(){ }


    public static TaskFlow fromTask( final Dao dao, final TaskFlowProvider provider, final String type,
                                     final String id ) {

        final Session ses = dao.beginSession();
        try {
            final Task t = ses.findTaskForResource( id );
            if ( t == null )
                return null;
            final TaskFlowFactory tff = provider.getFactoryForTaskFlow( type );
            final TaskFlow tf = tff.createOrphan();
          //  todo tf.setId( t.getResourceId() );
            tf.setOrder( ( DelegatingOrder<?> ) t.getOrder( ) );
            tf.addQuote( quoteFromContract( t.getContract() ) );
            tf.setTaskling( t.getTaskling() );
            tff.adopt( tf );
            ses.success();
            return tf;
        } finally {
            ses.finish();
        }
    }


    public static Quote quoteFromContract( PersistentContract contract ) {

        return contract.toTransientContract();
    }
}