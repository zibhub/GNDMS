package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.MinMaxPair;
import de.zib.gndms.model.gorfx.types.LevelRange;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:16:08
 */
public class SpaceConstraintPropertyWriter extends AbstractPropertyIO implements SpaceConstraintWriter {


    public SpaceConstraintPropertyWriter( Properties properties ) {
        super( properties );
    }


    public SpaceConstraintPropertyWriter() {
        super( );
    }


    public void writeLatitude( MinMaxPair lat ) {
        writeLatitudeToProperty( getProperties(), lat );
    }


    public void writeLongitude( MinMaxPair lon ) {
        writeLongitudeToProperty( getProperties(), lon );
    }


    public LevelRangeWriter getLevelRangeWriter() {
        return new LevelRangePropertyWriter( getProperties( ) );
    }


    public static void writeLatitudeToProperty( Properties prop, MinMaxPair lat ) {
        prop.setProperty( SfrProperty.LAT_MIN.key, Double.toString( lat.getMinValue( ) ) );
        prop.setProperty( SfrProperty.LAT_MAX.key, Double.toString( lat.getMaxValue( ) ) );
    }


    public static void writeLongitudeToProperty( Properties prop, MinMaxPair lon ) {
        prop.setProperty( SfrProperty.LON_MIN.key, Double.toString( lon.getMinValue( ) ) );
        prop.setProperty( SfrProperty.LON_MAX.key, Double.toString( lon.getMaxValue( ) ) );
    }
    

    public void done() {
    }


    public void beginWritingLevelRangeWriter() {
        // not required here
    }


    public void doneWritingLevelRangeWriter() {
        // not required here
    }
}
