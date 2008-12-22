package de.zib.gndms.model.gorfx.types;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;


/**
 * A time constrain is a selection criteria for data stagin.
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:11:00 PM
 */
public class TimeConstraint implements Serializable {
	
	private DateTime minTime;
    private DateTime maxTime;

    private final transient DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime( );

    private static final long serialVersionUID = 2682486067028199164L;


    public boolean hasMinTime() {
        return minTime != null;
    }

    public DateTime getMinTime() {
        return minTime;
    }


    public String getMinTimeString() {
        return isoFormatter.print( minTime );
    }


    public void setMinTime( String minTime ) {
        this.minTime = isoFormatter.parseDateTime( minTime );
    }

    
    public void setMinTime( DateTime minTime ) {
        this.minTime = minTime;
    }


    public boolean hasMaxTime() {
        return maxTime != null;
    }


    public DateTime getMaxTime() {
        return maxTime;
    }


    public String getMaxTimeString() {
        return isoFormatter.print( maxTime );
    }


    public void setMaxTime( String maxTime ) {
        this.maxTime = isoFormatter.parseDateTime( maxTime );
    }


    public void setMaxTime( DateTime maxTime ) {
        this.maxTime = maxTime;
    }
}
