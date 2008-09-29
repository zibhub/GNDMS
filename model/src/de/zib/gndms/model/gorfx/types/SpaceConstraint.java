package de.zib.gndms.model.gorfx.types;

/**
 * A space constrain is a selection criteria for data stagin.
 * 
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:10:44 PM
 *
 */
public class SpaceConstraint implements DataConstraint {

    private MinMaxPair latitude;
    private MinMaxPair longitude;
    private LevelRange Altitude;


    public boolean hasLongitude( ) {
        return longitude != null;
    }


    public boolean hasLatitude( ) {
        return latitude != null;
    }


    public boolean hasAltitude( ) {
        return Altitude != null;
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


    public LevelRange getAltitude() {
        return Altitude;
    }


    public void setAltitude( LevelRange altitude ) {
        Altitude = altitude;
    }
}
