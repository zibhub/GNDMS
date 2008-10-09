package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.MinMaxPair;

/**
 * Builder interface for a space constraint.
 *
 * NOTE Cause of the few methods ther is now converter class for the
 *      space constraint yet. This might change if the class gets more
 *      complex.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:14:49
 */
public interface SpaceConstraintWriter extends GORFXWriterBase {

    public void writeLatitude( MinMaxPair lat );
    public void writeLongitude( MinMaxPair lon );

    public void writeAltitude( MinMaxPair alt );

    public void writeVerticalCRS( String verticalCRS );

    public void writeAreaCRS( String areaCRS );
}
