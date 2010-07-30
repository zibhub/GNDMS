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



import de.zib.gndms.model.common.types.TransientContract;
import org.joda.time.DateTime;
import types.OfferExecutionContractT;
import types.ContextT;

import java.util.Calendar;


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
            Calendar cal  = src.getIfDecisionBefore();
            if( cal != null )
                con.setAccepted( new DateTime( cal ) );
	        
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
