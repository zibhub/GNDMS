package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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
