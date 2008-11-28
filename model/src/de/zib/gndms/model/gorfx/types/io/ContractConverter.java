package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.common.types.TransientContract;
import org.joda.time.DateTime;


/**
 * This converter operates on transient contracts.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:16:37
 */
@SuppressWarnings({ "FeatureEnvy" })
public class ContractConverter extends GORFXConverterBase<ContractWriter, TransientContract> {

    
    public ContractConverter() {
        super();
    }


    public ContractConverter( ContractWriter writer, TransientContract model ) {
        super( writer, model );
    }


    @Override
    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();

        DateTime dat = getModel( ).getAccepted( );
        if( dat != null )
            getWriter().writeIfDecisionBefore( dat );

        FutureTime dead = getModel().getDeadline();
        if( dead != null )
            getWriter().writeExecutionLikelyUntil( dead );

        FutureTime rv = getModel().getResultValidity();
        if( rv != null )
            getWriter().writeResultValidUntil( rv );

	    if (getModel().hasExpectedSize())
            getWriter().writeExpectedSize( getModel().getExpectedSize() );

        if ( getModel( ).has )
        
        getWriter().done();
    }
}
