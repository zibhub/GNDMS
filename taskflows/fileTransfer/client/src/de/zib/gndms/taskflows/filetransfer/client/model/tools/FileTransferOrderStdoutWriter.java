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



import de.zib.gndms.model.gorfx.types.io.OrderStdoutWriter;

import java.util.Map;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 15:02:21
 */
public class FileTransferOrderStdoutWriter extends OrderStdoutWriter implements FileTransferOrderWriter {

    public void writeSourceURI( String uri ) {

        System.out.println( "SourceURI: " + uri );
    }


    public void writeDestinationURI( String uri ) {
        System.out.println( "DestinationURI: " + uri );
    }


    public void writeFileMap( Map<String, String> fm ) {
        System.out.println( "FileMap: " );
        showMap( fm );
    }


    public void begin() {
        System.out.println( "******************** FileTransferOrder ********************" );
    }


    public void done() {
        System.out.println( "******************* EOFileTransferORQ *******************" );
    }
}
