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
	
	private String minTime;
    private String maxTime;

    private static final long serialVersionUID = 2682486067028199165L;


    public boolean hasMinTime() {
        return minTime != null;
    }

    public String getMinTime() {
        return minTime;
    }


    public String getMinTimeString() {
        return minTime;
    }


    public void setMinTime( String minTime ) {
        this.minTime = minTime;
    }

    
    public void setMinTime( DateTime minTime ) {
        this.minTime = minTime.toString();
    }


    public boolean hasMaxTime() {
        return maxTime != null;
    }


    public String getMaxTime() {
        return maxTime;
    }


    public String getMaxTimeString() {
        return maxTime;
    }


    public void setMaxTime( String maxTime ) {
        this.maxTime = maxTime;
    }


    public void setMaxTime( DateTime maxTime ) {
        this.maxTime = maxTime.toString( );
    }
}
