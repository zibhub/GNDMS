package de.zib.gndms.gritserv.typecon.tests.io;

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



import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;

import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.tests.FileTransferORQIOTest;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDReader;
import types.FileTransferORQT;
import static org.testng.Assert.assertEquals;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 16:00:56
 */
public class FileTransferORQIO {

    private String propFileName;
    private Properties prop;


    @Parameters( "propFileName" )
    public FileTransferORQIO( @Optional( "FileTransf_io_test_props.properties" ) String propFileName ) {
        this.propFileName = propFileName;
    }


    @BeforeClass( groups={ "io" } )
    public void beforeClass( ) {

        prop = new Properties( );

        try {
            InputStream is = new FileInputStream( propFileName );
            prop.load( is );
            is.close( );
        } catch ( FileNotFoundException e ) {
            System.err.println( "Please ensure that your specified workingDir contains " +propFileName );
            System.err.println( "If the file isn't present one can use: " );
            System.err.println( FileTransferORQIOTest.class.getName() );
            System.err.println( "to obtain it" );
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println( "read following properties from file: " );
        try {
            prop.store( System.out, "some properties");
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Test( groups={ "io" } )
    public void testIt ( ) throws Exception {

        System.out.println( "Creating ORQ form properties" );
        FileTransferORQPropertyReader orqr = new FileTransferORQPropertyReader( prop );
        orqr.performReading();

        FileTransferORQ orq = orqr.getProduct();

        // create XSDT form orq
        FileTransferORQXSDTypeWriter writer = new FileTransferORQXSDTypeWriter();
        FileTransferORQConverter conv = new FileTransferORQConverter( writer, orq );
        conv.convert( );

        FileTransferORQT orqt = writer.getProduct( );

        // convert orqt to orq
        FileTransferORQ norq = FileTransferORQXSDReader.read( orqt, null );
        Properties nprop = new Properties( );

        FileTransferORQPropertyWriter pwrit = new FileTransferORQPropertyWriter( nprop );
        conv.setModel( norq );
        conv.setWriter( pwrit );
        conv.convert( );

        // show written prop:
        System.out.println( "rewritten prop" );
        nprop.store( System.out, "rewritten prop" );

        System.out.println( "Possible exception may be caused by different order of the hashes" );
        assertEquals( prop, nprop );
    }

}
