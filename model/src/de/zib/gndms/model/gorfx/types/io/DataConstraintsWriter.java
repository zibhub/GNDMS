package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.TimeConstraint;

import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:28:23
 */
public interface DataConstraintsWriter extends GORFXWriterBase {

    //no writeSpaceConstraint see below
    public void writeTimeConstraint ( TimeConstraint timeConstraint );
    public void writeCFList( String[] CFList );
    public void writeConstraintList( HashMap<String,String> constraintList );

    
    // Used by the converter to write a space constraint
    public SpaceConstraintWriter getSpaceConstraintWriter( );

    // The following methods are called by the DataDescriptorConverter immediatly before and
    // after the write of the space constraint is triggered.
    //
    // Uses the following methods to perform context switches on your writer
    // if required
    public void beginWritingSpaceConstraint( );
    public void doneWritingSpaceConstraint( );
}
