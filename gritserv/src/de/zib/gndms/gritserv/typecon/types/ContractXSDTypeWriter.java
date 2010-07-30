package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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

        getProduct().setVolatileRequestInfo( ContextXSDTypeWriter.writeContext( additionalNotes ) );
    }


    public void begin() {
        setProduct( new OfferExecutionContractT( ) );
    }


    public void done() {
        // Not required here
    }


    public static OfferExecutionContractT write( TransientContract con ) {

        ContractXSDTypeWriter writ = new ContractXSDTypeWriter();
        ContractConverter conv = new ContractConverter( writ, con );
        conv.convert( );
        return writ.getProduct( );
    }
}
