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



import de.zib.gndms.model.gorfx.types.DataDescriptor;

import java.util.Properties;

/**
 * Reads a data descriptor form a property file.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 10:36:23
 */
public class DataDescriptorPropertyReader extends AbstractPropertyReader<DataDescriptor> {


    public DataDescriptorPropertyReader() {
        super( DataDescriptor.class );
    }


    public DataDescriptorPropertyReader( Properties properties ) {
        super(  DataDescriptor.class, properties );
    }


    /**
     * Reads a provided property file.
     * PRECONDITION  begin( ) must have been called.
     */
    @Override
    public void read( ) {

        getProduct( ).setObjectList(
            PropertyReadWriteAux.readListMultiLine( getProperties(), SfrProperty.OBJECT_ITEMS.key ) );

        if(! getProperties().containsKey( SfrProperty.JUST_DOWNLOAD.key ) )
            getProduct().setConstrains( DataConstraintsPropertyReader.readDataConstraints( getProperties() ) );

        getProduct( ).setDataFormat( getProperties().getProperty( SfrProperty.FILE_FORMAT.key ) );
        getProduct( ).setDataArchiveFormat( getProperties().getProperty( SfrProperty.FILE_ARCHIVE_FORMAT.key ) );

        getProduct( ).setMetaDataFormat( getMandatoryProperty( SfrProperty.META_FILE_FORMAT.key ) );

        getProduct( ).setMetaDataArchiveFormat( getProperties().getProperty( SfrProperty.META_FILE_ARCHIVE_FORMAT.key ) );
    }


    public void done() {
    }
}
