package de.zib.gndms.GORFX.common.type.io;

import types.SpaceConstraintT;
import types.MinMaxT;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.MinMaxPair;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 10:51:48
 */
public class SpaceConstrantXSDTypeWriter extends AbstractXSDTypeWriter<SpaceConstraintT> implements SpaceConstraintWriter {

    public void writeLatitude( MinMaxPair lat ) {
        getProduct().setLatitude( createMinMaxT( lat ) );
    }


    public void writeLongitude( MinMaxPair lon ) {
        getProduct().setLongitude( createMinMaxT( lon ) );
    }


    public void writeAltitude( MinMaxPair alt ) {
        getProduct().setAltitude( createMinMaxT( alt ) );
    }


    public void writeVerticalCRS( String verticalCRS ) {
        getProduct( ).setVerticalCRS( verticalCRS );
    }


    public void writeAreaCRS( String areaCRS ) {
        getProduct( ).setAreaCRS( areaCRS );
    }


    public void begin() {
        setProduct( new SpaceConstraintT( ) );
    }


    public void done() {
        // Not required here
    }
    

    public static MinMaxT createMinMaxT( MinMaxPair mmp ) {
        MinMaxT mmt = new MinMaxT( );
        mmt.setMin( mmp.getMinValue() );
        mmt.setMax( mmp.getMaxValue() );
        
        return mmt;
    }
}
