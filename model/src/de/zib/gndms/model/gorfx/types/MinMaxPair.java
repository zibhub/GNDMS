package de.zib.gndms.model.gorfx.types;

import java.io.Serializable;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 14:02:40
 */
public class MinMaxPair implements Serializable {

    private double minValue;
    private double maxValue;
    
    private static final long serialVersionUID = 3626614300502324191L;


    public MinMaxPair() {
    }

    public MinMaxPair( double minValue, double maxValue ) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue( double minValue ) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue( double maxValue ) {
        this.maxValue = maxValue;
    }
}
