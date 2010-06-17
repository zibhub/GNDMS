package de.zib.gndms.model.gorfx.types.io;

import org.joda.time.DateTime;

/**
 * Builder interface for a time constraint.
 *
 * NOTE Cause of the few methods there is now converter class for the
 *      time constraint yet. This might change if the class gets more
 *      complex.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 14:54:27
 */
public interface TimeConstraintWriter extends GORFXWriterBase {

    public abstract void writeMinTime( DateTime dt );
    public abstract void writeMaxTime( DateTime dt );
}
