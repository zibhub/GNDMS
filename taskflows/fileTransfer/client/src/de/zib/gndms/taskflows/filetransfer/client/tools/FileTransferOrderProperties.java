package de.zib.gndms.taskflows.filetransfer.client.tools;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.stuff.misc.DocumentedKey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @brief Keys for the file transfer order
 *
 * When an order should be read from a property file the property-keys defined in this class will
 * be used.
 *
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 23.02.12  15:55
 */
public final class FileTransferOrderProperties {


    // file transfer orq
    public final static DocumentedKey FILE_TRANSFER_SOURCE_URI;
    public final static DocumentedKey FILE_TRANSFER_DESTINATION_URI;
    public final static DocumentedKey FILE_TRANSFER_FILE_MAPPING;
    public final static List<DocumentedKey> FILE_TRANSFER_FILE_KEYS;

    static {
        ArrayList<DocumentedKey> keys = new ArrayList<DocumentedKey>( 3 );
        FILE_TRANSFER_SOURCE_URI = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FileTransferRequest.SourceURI",
                "source gsiftp URL for the transfer (remember to escape the colon)"
        );
        FILE_TRANSFER_DESTINATION_URI = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FileTransferRequest.DestinationURI",
                "destination gsiftp URL for the transfer (remember to escape the colon)"
        );
        FILE_TRANSFER_FILE_MAPPING = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FileTransferRequest.FileMapping",
                "Map of files which should be transferred\n" +
                "File names should be separated by spaces"
        );
        FILE_TRANSFER_FILE_KEYS = Collections.unmodifiableList( keys );
    }


    public static void createTemplate( PrintWriter out ) {
        for ( DocumentedKey dk : FILE_TRANSFER_FILE_KEYS )
            dk.asPropertiesTemplate( out );
    }

    // no instance of this one
    private FileTransferOrderProperties(){ }
}
