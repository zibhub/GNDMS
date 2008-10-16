package de.zib.gndms.GORFX.common.type.io;

import types.SpaceConstraintT;
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
        // Not required here
    }


    public void writeLongitude( MinMaxPair lon ) {
        // Not required here
    }


    public void writeAltitude( MinMaxPair alt ) {
        // Not required here
    }


    public void writeVerticalCRS( String verticalCRS ) {
        // Not required here
    }


    public void writeAreaCRS( String areaCRS ) {
        // Not required here
    }


    public void begin() {
        // Not required here
    }


    public void done() {
        // Not required here
    }
}
