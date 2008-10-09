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
    public void writeDataFormat( String dataFormat );
    public void writeDataArchiveFormat( String dataArchiveFormat );
    public void writeMetaDataFormat( String metaDataFormat );
    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat );

    // Used by the converter to write a data constraint
    public DataConstraintsWriter getDataConstraintsWriter();

    
    // The following methods are called by the DataDescriptorConverter immediately before and
    // after the write of the data constraint is triggered.
    //
    // Uses the following methods to perform context switches on your writer
    // if required it.
    public void beginWritingDataConstraints();
    public void doneWritingDataConstraints();


    // This method is called when the DataDescriptor doesn't have constraints.
    public void writeJustDownload();
}
