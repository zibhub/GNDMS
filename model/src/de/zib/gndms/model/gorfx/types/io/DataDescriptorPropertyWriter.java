package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.common.model.gorfx.types.io.AbstractPropertyIO;
import de.zib.gndms.common.model.gorfx.types.io.PropertyReadWriteAux;
import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Properties;

/**
 * Builder to export a data descriptor into a property file.
 *
 * NOTE loading and storing of the Properties must be performed by their provider.
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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

    @Override
    public void writeObjectList( @NotNull List<String> objectList ) {
        PropertyReadWriteAux.writeListMultiLine( getProperties( ), SfrProperty.OBJECT_ITEMS.key, objectList );
    }



    @Override
    public void writeDataFormat( @NotNull String dataFormat ) {
        getProperties().setProperty( SfrProperty.FILE_FORMAT.key, dataFormat );
    }


    @Override
    public void writeDataArchiveFormat( String dataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.FILE_ARCHIVE_FORMAT.key, dataArchiveFormat );
    }


    @Override
    public void writeMetaDataFormat( @NotNull String metaDataFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_FORMAT.key, metaDataFormat );
    }


    @Override
    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_ARCHIVE_FORMAT.key, metaDataArchiveFormat );
    }


    @Override
    public void writeJustDownload() {
        getProperties().setProperty( SfrProperty.JUST_DOWNLOAD.key, "true" );
    }


    @Override
    public DataConstraintsWriter getDataConstraintsWriter() {
        return new DataConstraintsPropertyWriter( getProperties() );
    }


    @Override
    public void beginWritingDataConstraints() {
        // Not required here
    }


    @Override
    public void doneWritingDataConstraints() {
        // Not required here
    }


    @Override
    public void done() {
        // Not required here
    }
}
