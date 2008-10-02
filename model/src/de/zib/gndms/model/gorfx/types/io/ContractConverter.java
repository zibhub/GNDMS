package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.Contract;

import java.util.Calendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:16:37
 */
public class ContractConverter extends GORFXConverterBase<ContractWriter, Contract> {

    
    public ContractConverter() {
    }


    public ContractConverter( ContractWriter writer, Contract model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();

        Calendar dat = getModel( ).getAccepted( );
        if( dat != null )
            getWriter().writeIfDecisionBefore( dat );

        dat = getModel().getDeadline();
        if( dat != null )
            getWriter().writeExecutionLikelyUntil( dat );

        dat = getModel().getResultValidity();
        if( dat != null )
            getWriter().writeResultValidUntil( dat );

        getWriter().writeConstantExecutionTime( getModel().isDeadlineIsOffset() );
    }
}
