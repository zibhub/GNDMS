package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:39:28
 */
public class NumericAltitude implements Altitude {

    private Double value;
    private AltitudeUnit unit;


    public NumericAltitude() {
    }


    public NumericAltitude( Double value, AltitudeUnit unit ) {
        this.value = value;
        this.unit = unit;
    }


    public AltitudeUnit getUnit() {
        return unit;
    }


    public void setUnit( AltitudeUnit unit ) {
        this.unit = unit;
    }


    public Double getValue() {
        return value;
    }


    public void setValue( Double value ) {
        this.value = value;
    }
}
