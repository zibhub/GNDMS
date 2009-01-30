package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.MinMaxPair;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 12:55:38
 */
public class SpaceConstraintStdoutWriter implements SpaceConstraintWriter {

    public void writeLatitude( MinMaxPair lat ) {

        System.out.println( "  Latitude:" );
        showMinMax( lat, "    " );
    }


    public void writeLongitude( MinMaxPair lon ) {
        System.out.println( "  Longitude:" );
        showMinMax( lon, "    " );
    }


    public void writeAltitude( MinMaxPair alt ) {
        System.out.println( "  Altitude:" );
        showMinMax( alt, "    " );
    }


    public void writeAreaCRS( String crs ) {
        System.out.println( "AreaCRS:" + crs );
    }


    public void writeVerticalCRS( String crs ) {
        System.out.println( "VerticalCRS:" + crs );
    }


    public void begin() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void showMinMax( MinMaxPair mmp, String ind ) {
        if( mmp == null )
            System.out.println( ind + "null" );
        else {
            System.out.println( ind + "Min: " + mmp.getMinValue() );
            System.out.println( ind + "Max: " + mmp.getMaxValue() );
        }
    }
}
