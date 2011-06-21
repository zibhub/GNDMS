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
import de.zib.gndms.logic.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.model.gorfx.types.Quote;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:56
 * @brief Dummy quote calculator.
 */
public class DummyQuoteCalculator implements AbstractQuoteCalculator<DummyOrder> {

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
        DateTime dt = new DateTime();
        dt.plusMillis( order.getTimes() * order.getDelay() );
        q.setDeadline( dt );
        DateTime rv = new DateTime( dt );
        rv.plusMinutes( 10 );
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
