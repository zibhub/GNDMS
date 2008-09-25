package de.zib.gndms.model.gorfx.types;

/**
 * A level range is a type to specify a level as part of a space constraint.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:42:14
 */
public class LevelRange {

    private Altitude min; // mandatory attribute
    private Altitude max; // mandatory attribute
    private String verticalCRS;


    public LevelRange() {
    }


    public LevelRange( Altitude min, Altitude max ) {
        this.min = min;
        this.max = max;
    }


    public LevelRange( Altitude min, Altitude max, String verticalCRS ) {
        this.min = min;
        this.max = max;
        this.verticalCRS = verticalCRS;
    }


    public Altitude getMin() {
        return min;
    }


    public void setMin( Altitude min ) {
        this.min = min;
    }


    public Altitude getMax() {
        return max;
    }


    public void setMax( Altitude max ) {
        this.max = max;
    }


    public boolean hasVerticalCRS( ) {
        return verticalCRS != null;
    }
    

    public String getVerticalCRS() {
        return verticalCRS;
    }


    public void setVerticalCRS( String verticalCRS ) {
        this.verticalCRS = verticalCRS;
    }
}
