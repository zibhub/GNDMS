package de.zib.gndms.taskflows.dummy.server;

import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.taskflows.dummy.client.model.DummyOrder;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:56
 * @brief Dummy quote calculator.
 */
public class DummyQuoteCalculator extends AbstractQuoteCalculator<DummyOrder> {


    @Override
    public boolean validate() {
        DummyOrder order = getOrderBean();
        return order!= null
            && order.getDelay() > 0
            && order.getMessage() != null
            && !order.getMessage().trim().equals( "" )
            && order.getTimes() > 0;
    }


    @Override
    public List<Quote> createQuotes() throws UnsatisfiableOrderException {

        if(! validate() )
            throw new UnsatisfiableOrderException( );

        final Quote q = new Quote();
        Duration d = new Duration( getOrderBean().getTimes() * getOrderBean().getDelay() );
        q.setDeadline( FutureTime.atOffset( d ) );
        FutureTime rv = FutureTime.atOffset( d.plus( 10 * 1000 * 60 ) );
        q.setResultValidity( rv );
        q.setExpectedSize( (long) 0 );

        return new ArrayList<Quote>( 1 ) {{ add( q ); }};
    }


    @Override
    public List<Quote> createQuotes( Quote preferred ) throws UnsatisfiableOrderException {

        List<Quote> rl = createQuotes();
        rl.add( preferred );

        return rl;
    }
}
