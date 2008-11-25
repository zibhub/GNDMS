package de.zib.gndms.typecon.common;

import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.gorfx.types.io.ContractWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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
