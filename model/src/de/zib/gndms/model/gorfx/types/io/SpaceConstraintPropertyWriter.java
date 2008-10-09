package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.MinMaxPair;

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
        getProperties( ).setProperty( SfrProperty.LAT_MIN.key, Double.toString( lat.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.LAT_MAX.key, Double.toString( lat.getMaxValue( ) ) );
    }


    public void writeLongitude( MinMaxPair lon ) {
        getProperties( ).setProperty( SfrProperty.LON_MIN.key, Double.toString( lon.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.LON_MAX.key, Double.toString( lon.getMaxValue( ) ) );
    }
    

    public void writeAltitude( MinMaxPair alt ) {
        getProperties( ).setProperty( SfrProperty.ALT_MIN.key, Double.toString( alt.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.ALT_MAX.key, Double.toString( alt.getMaxValue( ) ) );
    }


    public void writeAreaCRS( String acrs ) {
        getProperties().setProperty( SfrProperty.AREA_CRS.key, acrs );
    }


    public void writeVerticalCRS( String vcrs ) {
        getProperties().setProperty( SfrProperty.ALT_VCRS.key, vcrs );
    }


    public void done() {
    }
}
