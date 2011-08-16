package de.zib.gndms.taskflows.filetransfer.client.model.tools;

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



import de.zib.gndms.model.gorfx.types.FileTransferOrder;
import de.zib.gndms.model.gorfx.types.io.OrderPropertyReader;
import de.zib.gndms.model.gorfx.types.io.PropertyReadWriteAux;
import de.zib.gndms.model.gorfx.types.io.SfrProperty;

import java.util.Properties;
import java.util.TreeMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:19:06
 */
public class FileTransferOrderPropertyReader extends OrderPropertyReader<FileTransferOrder> {

    public FileTransferOrderPropertyReader() {
        super( FileTransferOrder.class );
    }


    public FileTransferOrderPropertyReader( Properties properties ) {
        super( FileTransferOrder.class, properties );
    }


    @Override
    public void read( ) {

        super.read( );
        getProduct().setSourceURI( getProperties().getProperty(
            SfrProperty.FILE_TRANSFER_SOURCE_URI.key ) );
        getProduct().setDestinationURI( getProperties().getProperty(
            SfrProperty.FILE_TRANSFER_DESTINATION_URI.key ) );

        if( getProperties().containsKey( SfrProperty.FILE_TRANSFER_FILE_MAPPING.key ) )
            getProduct().setFileMap(
                new TreeMap<String, String> (
                    PropertyReadWriteAux.readMap( getProperties(), SfrProperty.FILE_TRANSFER_FILE_MAPPING.key )
                )
            );
    }
    

    public void done() {
        // Not required here
    }
}
