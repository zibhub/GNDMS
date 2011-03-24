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



import java.util.Map;
import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 16:46:13
 */
public class FileTransferORQPropertyWriter extends ORQPropertyWriter implements FileTransferORQWriter {

    public FileTransferORQPropertyWriter() {
    }


    public FileTransferORQPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeSourceURI( String uri ) {
        getProperties().setProperty( SfrProperty.FILE_TRANSFER_SOURCE_URI.key, uri );
    }


    public void writeDestinationURI( String uri ) {
        getProperties().setProperty( SfrProperty.FILE_TRANSFER_DESTINATION_URI.key, uri );
    }


    public void writeFileMap( Map<String, String> fm ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.FILE_TRANSFER_FILE_MAPPING.key, fm );
    }


    public void done() {
        // Not required here
    }
}
