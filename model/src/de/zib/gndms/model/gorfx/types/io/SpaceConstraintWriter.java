package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.MinMaxPair;
import de.zib.gndms.model.gorfx.types.LevelRange;

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

    public abstract void writeLatitude( MinMaxPair lat );
    public abstract void writeLongitude( MinMaxPair lon );

    // Methods for the altitude
    // this methods are analogous to the SpaceConstraintWriter methods
    // of DattaDescriptorConverter. Look their for details. 
    public LevelRangeWriter getLevelRangeWriter();
    public void beginWritingLevelRangeWriter();
    public void doneWritingLevelRangeWriter();
}
