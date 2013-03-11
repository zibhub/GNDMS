package de.zib.gndms.common.model.gorfx.types.io;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import de.zib.gndms.common.model.gorfx.types.FutureTime;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Map;
import java.util.Set;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 14:15:26
 */
public class ContractStdoutWriter implements ContractWriter {

    private final DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime( );

    public void writeIfDecisionBefore( DateTime dat ) {
        showDate( dat, "IfDecisionBefore: " );
    }


    public void writeExecutionLikelyUntil( FutureTime dat ) {
        showDate( dat, "ExecutionLikelyUnitl: " );
    }


	public void writeExpectedSize(final Long l) {
		System.out.println( "ExpectedSize " + (l == null ? "(null)" : l.toString()));		
	}


	public void writeConstantExecutionTime( boolean et ) {
        System.out.println( "ConstantExecutionTime? " + et );
    }


    public void writeResultValidUntil( FutureTime dat ) {
        showDate( dat, "ResultValidUntil: " );
    }


    public void writeAdditionalNotes( Map<String, String> additionalNotes ) {
        System.out.println( "Additional Notes: " );
        Set<String> ks = additionalNotes.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + additionalNotes.get( k ) );
    }

    public void writeContext( Map<String, String> context ) {
        System.out.println( "Context information: " );
        Set<String> ks = context.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + context.get( k ) );
    }

    public void begin() {

        // Not required here
    }


    public void done() {
        // Not required here
    }

    private void showDate( DateTime cal, String msg ) {
        System.out.println( msg + isoFormatter.print( cal ) );
    }

    private void showDate( FutureTime cal, String msg ) {
        System.out.println( msg + ' ' + cal.toString() );
    }
}
