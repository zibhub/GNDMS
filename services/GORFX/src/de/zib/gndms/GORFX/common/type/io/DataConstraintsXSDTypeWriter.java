package de.zib.gndms.GORFX.common.type.io;

import de.zib.gndms.model.gorfx.types.io.DataConstraintsWriter;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.TimeConstraint;
import types.DataDescriptorTConstraints;

import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 09:12:27
 */
public class DataConstraintsXSDTypeWriter extends AbstractXSDTypeWriter<DataDescriptorTConstraints> implements DataConstraintsWriter {

    //no writeSpaceConstraint see below
    public void writeTimeConstraint( TimeConstraint timeConstraint ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void writeCFList( String[] CFList ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void writeConstraintList( HashMap<String, String> constraintList ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }// Used by the converter to write a space constraint
    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// The following methods are called by the DataDescriptorConverter immediatly before and
    // after the write of the space constraint is triggered.
    //
    // Uses the following methods to perform context switches on your writer
    // if required
    public void beginWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void doneWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void begin() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
