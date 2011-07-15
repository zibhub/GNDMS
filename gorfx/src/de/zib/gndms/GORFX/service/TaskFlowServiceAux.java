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
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import e.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowFactory;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import org.springframework.http.HttpStatus;

import java.util.List;

public final class TaskFlowServiceAux {

    public static <T extends Order> HttpStatus setAndValidateOrder( Order order, TaskFlow<T> taskFlow, TaskFlowFactory<T, ?> factory ) {

        DelegatingOrder<T> delegate = setOrderAsDelegate( order, taskFlow, factory );

        return validateOrder( factory, delegate );
    }


    public static <T extends Order> DelegatingOrder<T> setOrderAsDelegate( Order order, TaskFlow<T> taskFlow, TaskFlowFactory<T, ?> factory ) {
        Class<T> orderClass = factory.getOrderClass();
        DelegatingOrder<T> delegate = factory.getOrderDelegate( orderClass.cast( order ) );
        taskFlow.setOrder( delegate );

        return delegate;
    }


    public static <T extends Order> HttpStatus validateOrder( TaskFlowFactory<T, ?> factory, DelegatingOrder<T> delegate ) {
        final AbstractQuoteCalculator<T> qc = factory.getQuoteCalculator();
        qc.setOrderArguments( delegate );

        if ( qc.validate() )
            return HttpStatus.OK;

        return HttpStatus.BAD_REQUEST;
    }


    public static <T extends Order> List<Quote> createQuotes( TaskFlowFactory<T,?> tff, TaskFlow<T> tf ) throws Exception {

        AbstractQuoteCalculator<T> qc = tff.getQuoteCalculator();
        qc.setOrderArguments( tf.getOrder() );
        List<Quote> quoteList;
        if ( tf.hasPreferredQuote() )
            quoteList = qc.createQuotes( tf.getPreferredQuote() );
        else
            quoteList = qc.createQuotes();

        tf.setQuotes( quoteList );
        return quoteList;
    }


    private TaskFlowServiceAux(){ }
}