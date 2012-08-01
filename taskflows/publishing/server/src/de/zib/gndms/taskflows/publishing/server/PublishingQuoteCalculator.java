package de.zib.gndms.taskflows.publishing.server;

import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.taskflows.publishing.client.model.PublishingOrder;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;

/**
 * @author bachmann@zib.de
 * @date 31.07.2012  14:31
 * @brief Quote Calculator for publishing
 */
public class PublishingQuoteCalculator extends AbstractQuoteCalculator< PublishingOrder > {


    @Override
    public boolean validate() {
        PublishingOrder order = getOrderBean();

        if( null == order )
            return false;

        if( null == order.getSliceId() )
            return false;

        return true;
    }


    @Override
    public List<Quote> createQuotes() throws UnsatisfiableOrderException {

        if(! validate() )
            throw new UnsatisfiableOrderException( );

        final Quote q = new Quote();
        Duration d = new Duration( 10 );
        q.setDeadline( FutureTime.atOffset( d ) );
        FutureTime rv = FutureTime.atOffset( d.plus( 10 * 1000 * 60 ) ); // not interesting here
        q.setResultValidity( rv );
        q.setExpectedSize( (long) 0 );

        return Collections.singletonList( q );
    }


    @Override
    public List<Quote> createQuotes( Quote preferred ) throws UnsatisfiableOrderException {

        List<Quote> rl = createQuotes();
        rl.add( preferred );

        return rl;
    }
}
