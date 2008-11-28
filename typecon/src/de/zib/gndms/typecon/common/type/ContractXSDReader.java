package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.common.types.TransientContract;
import org.joda.time.DateTime;
import types.OfferExecutionContractT;
import types.ContextT;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 11:23:00
 */
public class ContractXSDReader {
	private ContractXSDReader() {}


	@SuppressWarnings({ "FeatureEnvy", "StaticMethodOnlyUsedInOneClass" })
    public static TransientContract readContract( OfferExecutionContractT src ) {

        if( src != null ) {
            TransientContract con = new TransientContract();
            con.setAccepted( new DateTime(src.getIfDecisionBefore()) );
	        
            con.setDeadline( FutureTimeXSDReader.read(src.getExecutionLikelyUntil()) );
            con.setResultValidity( FutureTimeXSDReader.read(src.getResultValidUntil()) );

	        final Long estMaxSize = src.getEstMaxSize();
	        if (estMaxSize != null)
	            con.setExpectedSize(estMaxSize);

            ContextT ctx = src.getVolatileRequestInfo();

            if( ctx != null )
                con.setAdditionalNotes( ContextXSDReader.readContext( ctx ) );

            return con;
        }
        
        return null;
    }
}
