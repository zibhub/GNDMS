package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SpaceConstraint;
import de.zib.gndms.model.gorfx.types.TimeConstraint;

import java.util.HashMap;

/**
 * So called builder interface for a data descriptor.
 *
 * Implement this interface to convert a data descriptor to a given format.
 * Then hand this implementation over to the DataDescriptorConverter along with the instance
 * you want to be converted.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:25:08
 */
public interface DataDescriptorWriter extends GORFXWriterBase {

    public void writeObjectList( String[] objectList );
    //no writeSpaceConstraint see below 
    public void writeTimeConstraint ( TimeConstraint timeConstraint );
    public void writeCFList( String[] CFList );
    public void writeConstraintList( HashMap<String,String> constraintList );
    public void writeDataFormat( String dataFormat );
    public void writeMetaDataFormat( String metaDataFormat );

    // Used by the converter to write a space constraint
    public SpaceConstraintWriter getSpaceConstraintWriter( );

    // The following methods are called by the DataDescriptorConverter immediatly before and
    // after the write of the space constraint is triggered.
    //
    // Uses the following methods to perform context switches on your write
    // if required
    public void beginWritingSpaceConstraint( );
    public void doneWritingSpaceConstraint( );
}
