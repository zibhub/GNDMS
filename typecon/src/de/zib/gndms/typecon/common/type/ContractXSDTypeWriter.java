package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractWriter;
import de.zib.gndms.model.gorfx.types.io.FutureTimeConverter;
import org.joda.time.DateTime;
import types.OfferExecutionContractT;

import java.util.Map;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:31:25
 */
public class ContractXSDTypeWriter extends AbstractXSDTypeWriter<OfferExecutionContractT> implements ContractWriter {

	private FutureTimeConverter conv = new FutureTimeConverter();
	private FutureTimeXSDTypeWriter wr = new FutureTimeXSDTypeWriter();

	{
		conv.setWriter(wr);
	}

    public void writeIfDecisionBefore( DateTime dat ) {
        getProduct( ).setIfDecisionBefore( dat.toGregorianCalendar() );
    }


    public void writeExecutionLikelyUntil( FutureTime dat ) {
	    conv.setModel(dat);
	    conv.convert();

        getProduct( ).setExecutionLikelyUntil( wr.getProduct() );
    }


	public void writeExpectedSize(final Long l) {
		if (l != null)
			getProduct().setEstMaxSize(l);
	}


	public void writeResultValidUntil(final FutureTime dat) {
		conv.setModel(dat);
		conv.convert();

        getProduct( ).setResultValidUntil( wr.getProduct() );
    }


    public void writeAdditionalNotes( Map<String, String> additionalNotes ) {

        
    }


    public void begin() {
        setProduct( new OfferExecutionContractT( ) );
    }


    public void done() {
        // Not required here
    }


    public static OfferExecutionContractT fromContract( TransientContract con ) {

        ContractXSDTypeWriter writ = new ContractXSDTypeWriter();
        ContractConverter conv = new ContractConverter( writ, con );
        conv.convert( );
        return writ.getProduct( );
    }
}
