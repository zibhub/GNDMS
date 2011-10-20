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

import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.taskflows.dummy.DummyOrder;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:56
 * @brief Dummy quote calculator.
 */
public class DummyQuoteCalculator extends AbstractQuoteCalculator<DummyOrder> {

    private DummyOrder order;

    public void setOrder( DummyOrder order ) {
        this.order = order;
    }


    public boolean validate() {
        return order!= null
            && order.getDelay() > 0
            && order.getMessage() != null
            && !order.getMessage().trim().equals( "" )
            && order.getTimes() > 0;
    }


    public List<Quote> createQuotes() throws UnsatisfiableOrderException {

        if(! validate() )
            throw new UnsatisfiableOrderException( );

        final Quote q = new Quote();
        Duration d = new Duration( order.getTimes() * order.getDelay() );
        q.setDeadline( FutureTime.atOffset( d ) );
        FutureTime rv = FutureTime.atOffset( d.plus( 10 * 1000 * 60 ) );
        q.setResultValidity( rv );
        q.setExpectedSize( (long) 0 );

        return new ArrayList<Quote>( 1 ) {{ add( q ); }};
    }


    public List<Quote> createQuotes( Quote preferred ) throws UnsatisfiableOrderException {

        List<Quote> rl = createQuotes();
        rl.add( preferred );

        return rl;
    }
}
