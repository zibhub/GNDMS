package de.zib.gndms.taskflows.esgfStaging.server;

import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;

/**
 * @author bachmann@zib.de
 * @date 14.02.11  16:56
 * @brief Dummy quote calculator.
 */
public class ESGFStagingQuoteCalculator extends AbstractQuoteCalculator<ESGFStagingOrder> {


    @Override
    public boolean validate() {
        ESGFStagingOrder order = getOrderBean();
        return order!= null;
    }


    @Override
    public List<Quote> createQuotes() throws UnsatisfiableOrderException {

        if(! validate() )
            throw new UnsatisfiableOrderException( );

        final Quote q = new Quote();
        Duration d = new Duration( 10 );
        q.setDeadline( FutureTime.atOffset( d ) );
        FutureTime rv = FutureTime.atOffset( d.plus( 10 * 1000 * 60 ) );
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
