package de.zib.gndms.model.gorfx.types.io.tests;

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
import de.zib.gndms.model.gorfx.types.io.FileTransferOrderPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.FileTransferOrderConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferOrderPropertyReader;
import de.zib.gndms.model.gorfx.types.io.FileTransferOrderStdoutWriter;

import java.util.TreeMap;
import java.util.Properties;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:38:41
 */
public class FileTransferORQIOTest {

    public static void main( String[] args ) {
        FileTransferOrder order = new FileTransferOrder( );
        order.setSourceURI( "gsiftp://csr-priv5/home/mjorra/tmp/C3GridTests/dataTransferTestSource" );
        order.setDestinationURI( "gsiftp://csr-priv5/home/mjorra/C3GridTests/dataTransferTestTarget" );

        TreeMap<String, String> fileMap = new TreeMap<String, String>( );
        fileMap.put( "a_1KB_file", null );
        fileMap.put( "b_1MB_file", "b_1000KB_file" );
        fileMap.put( "c_1GB_file", "c_largeFile" );
        order.setFileMap( fileMap );

        HashMap<String, String> ctx = new HashMap<String, String>( );
        ctx.put( "k1", "v1" );
        ctx.put( "k2", "v2" );
        ctx.put( "k3", "v3" );
        ctx.put( "k4", "v4" );
        order.setActContext( ctx );

        FileTransferOrderStdoutWriter cout = new FileTransferOrderStdoutWriter( );
        FileTransferOrderConverter conv = new FileTransferOrderConverter( cout, order );
        conv.convert( );

        Properties prop = new Properties( );
        FileTransferOrderPropertyWriter propw = new FileTransferOrderPropertyWriter( prop );
        conv.setWriter( propw );
        conv.convert( );
        showProps( prop );

        String fn = "FileTransf_io_test_props.properties";
        try {
            OutputStream os = new FileOutputStream( fn );
            prop.store( os, "some test props for file transfer io" );
            os.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        FileTransferOrderPropertyReader propr = new FileTransferOrderPropertyReader( prop );
        propr.performReading();

        FileTransferOrder order2 = propr.getProduct();
        System.out.println( "\nRead order: " );
        conv.setModel( order2 );
        conv.setWriter( cout );
        conv.convert( );

        Properties prop2 = new Properties( );
        propw.setProperties( prop2 );
        conv.setWriter( propw );
        conv.convert( );

        showProps( prop2 );
        if( prop.equals( prop2 ) )
            System.out.println( "write read write read: OKAY" );
        else
            throw new IllegalStateException( "write read write read gave different results"+
                "\nnote this might be okay and can depend on the order of the stored hashes" );
    }

    public static void showProps( Properties prop ) {

        try {
            prop.store( System.out, "Some props" );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
