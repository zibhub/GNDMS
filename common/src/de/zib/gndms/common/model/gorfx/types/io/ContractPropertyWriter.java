package de.zib.gndms.common.model.gorfx.types.io;

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



import de.zib.gndms.common.model.gorfx.types.FutureTime;
import org.joda.time.DateTime;

import java.util.Properties;
import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 11:28:28
 */
public class ContractPropertyWriter extends AbstractPropertyIO implements ContractWriter {


    public ContractPropertyWriter() {
        super();
    }


    public ContractPropertyWriter(final Properties properties) {
        super(properties);
    }


    public void writeIfDecisionBefore(final DateTime dat) {
        PropertyReadWriteAux.writeISODateTime( getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key, dat);
    }


	public void writeExpectedSize(final Long l) {
		if (l != null)
			getProperties().put(SfrProperty.EST_MAX_SIZE.key, l.toString());
	}


	public void writeExecutionLikelyUntil(final FutureTime dat) {
        PropertyReadWriteAux.writeFutureTime( getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key, dat);
    }


    public void writeResultValidUntil(final FutureTime dat) {
        PropertyReadWriteAux.writeFutureTime( getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key, dat);
    }


    public void writeAdditionalNotes( Map<String, String> additionalNotes ) {
        
        if( additionalNotes != null ) 
        	
        	PropertyReadWriteAux.writeMap( getProperties( ), SfrProperty.EST_REQUEST_INFO.key, additionalNotes );
    }

    public void writeContext( Map<String, String> context ) {
        
        if( context != null ) 
        	
        	PropertyReadWriteAux.writeMap( getProperties( ), SfrProperty.CONTEXT.key, context );
    }

    public void done() {
    }
}
