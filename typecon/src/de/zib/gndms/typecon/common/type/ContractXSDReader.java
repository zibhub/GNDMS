package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.Contract;
import types.OfferExecutionContractT;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 11:23:00
 */
public class ContractXSDReader {

    public static Contract readContract( OfferExecutionContractT src ) {

        if( src != null ) {
            Contract con = new Contract();
            con.setAccepted( src.getIfDecisionBefore() );
            con.setDeadline( src.getExecutionLikelyUntil() );
            con.setResultValidity( src.getResultValidUntil() );
            con.setDeadlineIsOffset( src.isConstantExecutionTime() );
            return con;
        }
        
        return null;
    }
}
