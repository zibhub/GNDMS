package de.zib.gndms.taskflows.interslicetransfer.client;

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
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.filetransfer.client.tools.FileTransferOrderProperties;
import de.zib.gndms.taskflows.filetransfer.client.tools.FileTransferOrderPropertyReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 * Bean for the parameters of the client.
 *
 * This bean is given in the form of properties specified by the user
 *
 */
public class InterSliceTransferExampleBean {

    private static final DocumentedKey FINAL_DESTINATION_KEY;
    private static final DocumentedKey REMOTE_DESTINATION_BASE_KEY;
    private static final DocumentedKey SUBSPACE_NAME_KEY;
    private static final DocumentedKey SLICE_KIND_NAME_KEY;
    private static final List<DocumentedKey> KEYS_WITH_DOCUMENTATION;

    static {
        ArrayList<DocumentedKey> keys = new ArrayList<DocumentedKey>( 4 );
        FINAL_DESTINATION_KEY = 
                DocumentedKey.createAndRegisterKey( keys,
                "gndms.interSliceTransfer.checkOutDestinationUrl",
                "A full GridFTP url for the final transfer operation (check-out)" );
        REMOTE_DESTINATION_BASE_KEY = DocumentedKey.createAndRegisterKey( keys,
                "gndms.interSliceTransfer.remoteBaseUrl",
                "The base url of the site where files should be transfered to.\n"+
                "Something like http://some.si.te:8080/gndms/c3grid" );
        SUBSPACE_NAME_KEY = DocumentedKey.createAndRegisterKey( keys,
                "gndms.interSliceTransfer.remoteSubspaceName",
                "The name of the subspace the files should be imported into.\n" +
                "Must exist on _gndms.interSliceTransfer.remoteBaseUrl_" );
        SLICE_KIND_NAME_KEY = DocumentedKey.createAndRegisterKey( keys,
                "gndms.interSliceTransfer.remoteSliceKindType",
                "The name of the sliceKind of the import slice.\n" +
                "Must exist in  gndms.interSliceTransfer.remoteSubspaceName" );
        KEYS_WITH_DOCUMENTATION = Collections.unmodifiableList( keys );
    }

    private String checkOutDestinationUri;

    private FileTransferOrder order;

    private String destinationBaseUri;
    
    private String subspace;

    private String sliceKind;


    public String getSubspace() {

        return subspace;
    }


    public String getSliceKind() {

        return sliceKind;
    }


    public String getDestinationBaseUri() {

        return destinationBaseUri;
    }


    public FileTransferOrder getOrder() {

        return order;
    }


    public String getCheckOutDestinationUri() {

        return checkOutDestinationUri;
    }


    public InterSliceTransferExampleBean() {

    }


    public void readFromFile( final File orderPropFile )
            throws IOException
    {

        InputStream is = new FileInputStream( orderPropFile );
        Properties props = new Properties();
        props.load( is );

        checkOutDestinationUri = props.getProperty( FINAL_DESTINATION_KEY.key );
        destinationBaseUri = props.getProperty( REMOTE_DESTINATION_BASE_KEY.key );
        subspace = props.getProperty( SUBSPACE_NAME_KEY.key );
        sliceKind = props.getProperty( SLICE_KIND_NAME_KEY.key );

        FileTransferOrderPropertyReader fileTransferOrderPropertyReader = new
                FileTransferOrderPropertyReader( props );

        order = fileTransferOrderPropertyReader.getProduct();

    }
    
    
    public void createPropsTemplate( final File propTemplateFile ) throws IOException {
        
        OutputStream os = new FileOutputStream( propTemplateFile );
        PrintWriter pw = new PrintWriter( os );
        for( DocumentedKey dk : KEYS_WITH_DOCUMENTATION )
            dk.asPropertiesTemplate( pw );

        FileTransferOrderProperties.createTemplate( pw );
        pw.close();
    }
}
