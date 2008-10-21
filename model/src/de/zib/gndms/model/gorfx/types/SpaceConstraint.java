package de.zib.gndms.model.gorfx.types;

import java.io.Serializable;


/**
 * A space constrain is a selection criteria for data stagin.
 * 
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:10:44 PM
 *
 */
public class SpaceConstraint implements Serializable {

    private MinMaxPair latitude; // required
    private MinMaxPair longitude; // required
    private String areaCRS; // optional
    private MinMaxPair altitude; // optional
    private String verticalCRS; // optional

    private static final long serialVersionUID = -6224923031075051128L;


    public boolean hasAltitude( ) {
        return altitude != null;
    }


    public boolean hasAreaCRS( ) {
        return areaCRS != null && areaCRS.length() != 0;
    }


    public boolean hasVerticalCRS( ) {
        return verticalCRS != null && verticalCRS.length() != 0;
    }


    public MinMaxPair getLatitude() {
        return latitude;
    }


    public void setLatitude( MinMaxPair latitude ) {
        this.latitude = latitude;
    }


    public MinMaxPair getLongitude() {
        return longitude;
    }


    public void setLongitude( MinMaxPair longitude ) {
        this.longitude = longitude;
    }


    public MinMaxPair getAltitude() {
        return altitude;
    }


    public void setAltitude( MinMaxPair altitude ) {
        this.altitude = altitude;
    }


    public String getAreaCRS() {
        return areaCRS;
    }


    public void setAreaCRS( String areaCRS ) {
        this.areaCRS = areaCRS;
    }


    public String getVerticalCRS() {
        return verticalCRS;
    }


    public void setVerticalCRS( String verticalCRS ) {
        this.verticalCRS = verticalCRS;
    }
}
