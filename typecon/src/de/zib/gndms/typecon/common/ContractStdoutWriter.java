package de.zib.gndms.typecon.common;

import de.zib.gndms.model.gorfx.types.io.ContractWriter;

import java.util.Calendar;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTime;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 14:15:26
 */
public class ContractStdoutWriter implements ContractWriter {

    private final DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime( );

    public void writeIfDecisionBefore( Calendar dat ) {
        showDate( dat, "IfDecisionBefore: " );
    }


    public void writeExecutionLikelyUntil( Calendar dat ) {
        showDate( dat, "ExecutionLikelyUnitl: " );
    }


    public void writeConstantExecutionTime( boolean et ) {
        System.out.println( "ConstantExecutionTime? " + et );
    }


    public void writeResultValidUntil( Calendar dat ) {
        showDate( dat, "ResultValidUntil: " );
    }


    public void begin() {

        // Not required here
    }


    public void done() {
        // Not required here
    }

    private void showDate( Calendar cal, String msg ) {
        DateTime dt = new DateTime( cal );
        System.out.println( msg + isoFormatter.print( dt ) );
    }
}
