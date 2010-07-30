package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.model.gorfx.types.TimeConstraint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Builder to export a data descriptor into a property file.
 *
 * NOTE loading and storing of the Properties must be performed by their provider.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:34:01
 */
public class DataDescriptorPropertyWriter extends AbstractPropertyIO implements DataDescriptorWriter {

    public DataDescriptorPropertyWriter() {
        super( );
    }


    public DataDescriptorPropertyWriter( @NotNull Properties properties ) {
        super( properties );
    }


    public void writeObjectList( @NotNull String[] objectList ) {
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.OBJECT_ITEMS.key, Arrays.asList( objectList ) );
    }



    public void writeDataFormat( @NotNull String dataFormat ) {
        getProperties().setProperty( SfrProperty.FILE_FORMAT.key, dataFormat );
    }


    public void writeDataArchiveFormat( String dataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.FILE_ARCHIVE_FORMAT.key, dataArchiveFormat );
    }


    public void writeMetaDataFormat( @NotNull String metaDataFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_FORMAT.key, metaDataFormat );
    }


    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_ARCHIVE_FORMAT.key, metaDataArchiveFormat );
    }


    public void writeJustDownload() {
        getProperties().setProperty( SfrProperty.JUST_DOWNLOAD.key, "true" );
    }


    public DataConstraintsWriter getDataConstraintsWriter() {
        return new DataConstraintsPropertyWriter( getProperties() );
    }

    
    public void beginWritingDataConstraints() {
        // Not required here
    }


    public void doneWritingDataConstraints() {
        // Not required here
    }


    public void done() {
        // Not required here
    }
}
