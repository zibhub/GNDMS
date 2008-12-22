package de.zib.gndms.typecon.common.type;

import types.TimeConstraintT;
import de.zib.gndms.model.gorfx.types.io.TimeConstraintWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 10:28:07
 */
public class TimeConstraintXSDTypeWriter extends AbstractXSDTypeWriter<TimeConstraintT> implements TimeConstraintWriter {

    private DateTimeFormatter ISOFormatter;

    public TimeConstraintXSDTypeWriter( ) {
        ISOFormatter = ISODateTimeFormat.dateTime( );
    }

    
    public void writeMinTime( DateTime dt ) {
        getProduct().setMinTime( ISOFormatter.print( dt ) );
    }


    public void writeMaxTime( DateTime dt ) {
        getProduct().setMaxTime( ISOFormatter.print( dt ) );
    }


    public void begin() {
        setProduct( new TimeConstraintT( ) );
    }


    public void done() {
        // Not required here
    }
}
