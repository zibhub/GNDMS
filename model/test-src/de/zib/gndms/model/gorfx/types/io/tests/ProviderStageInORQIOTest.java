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



import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.gorfx.types.io.*;
import org.joda.time.DateTime;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 16:19:48
 */
public class ProviderStageInORQIOTest {

    public static void main( String[] args ) {

        DataDescriptor ddt = new DataDescriptor();
        DataConstraints dc = new DataConstraints();
        String[] ol = { "hello", "world" };
        String[] cfl = { "foo", "bar", "foobar", "blubber", };
        HashMap<String,String> cl = new HashMap<String,String>( );
        cl.put( "gibson", "les-paul");
        cl.put( "fender", "Stratocaster" );
        cl.put( "duesenberg", "double-cat" );

        SpaceConstraint sc = new SpaceConstraint();
        sc.setLatitude( new MinMaxPair( -1.3, 5.7 ) );
        sc.setLongitude( new MinMaxPair( 2.7, 42.0 ) );
        sc.setAltitude(  new MinMaxPair( 1500., 3000.  ) );
        sc.setVerticalCRS( "some vertical crs" );
        sc.setAreaCRS( "the area crs" );

        DateTime tm = new DateTime( );
        TimeConstraint tc = new TimeConstraint();
        tc.setMinTime( tm );
        tc.setMaxTime( tm.plusDays( 6 ) );

        String dff = "plain";
        String dfaf = "zip";
        String mff = "ini";
        String mfaf = "tar";

        dc.setCFList( cfl );
        dc.setSpaceConstraint( sc );
        dc.setTimeConstraint( tc );
        dc.setConstraintList( cl );
        
        ddt.setObjectList( ol );
        ddt.setConstrains( dc );
        ddt.setDataFormat( dff );
        ddt.setDataArchiveFormat( dfaf );
        ddt.setMetaDataFormat( mff );
        ddt.setMetaDataArchiveFormat( mfaf );

        ProviderStageInOrder order = new ProviderStageInOrder();
        order.setDataDescriptor( ddt );
        order.setActDataFile( "data_file" );
        order.setActMetadataFile( "meta_data_file" );
        order.setActId( "asdfjkl" );

        Properties prop = new Properties( );
        ProviderStageInOrderPropertyWriter wrt = new ProviderStageInOrderPropertyWriter( prop );
        ProviderStageInOrderConverter conv = new ProviderStageInOrderConverter( wrt, order );
        conv.convert();
        try {
            prop.store( System.out, "hallo welt" );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ProviderStageInOrderQPropertyReader reader = new ProviderStageInOrderQPropertyReader( prop );
        reader.begin( );
        reader.read();
        ProviderStageInOrder rorq = reader.getProduct();

        String fn = "StageIn_io_test_props.properties";
        try {
            OutputStream os = new FileOutputStream( fn );
            prop.store( os, "some test props for proivder stage in io" );
            os.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // example of converter reusage
        ProviderStageInOrderStdoutWriter stdwrt = new ProviderStageInOrderStdoutWriter( );
        conv.setWriter( stdwrt );
        conv.setModel( rorq );
        conv.convert( );

        System.out.println( "Reading test prop from file" );
        fn = "prop_test_file.txt";
        try {
            InputStream is = new FileInputStream( fn );
            Properties np = new Properties( );
            np.load( is );
            is.close( );
            np.store( System.out, "test out" );
            reader.setProperties( np );
            reader.begin( );
            reader.read(); 
            showORQ( reader.getProduct() );
        } catch ( FileNotFoundException e ) {
            System.out.println( "File not found make!" );
            System.out.println( "Please make sure your working dir contains " + fn );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        // example for just download
        DataDescriptor ddt2 = new DataDescriptor();
        String[] objs = { "no", "download" };

        dff = "plain";
        dfaf = "zip";
        mff = "ini";
        mfaf = "tar";

        ddt2.setObjectList( objs );
        ddt2.setDataFormat( dff );
        ddt2.setDataArchiveFormat( dfaf );
        ddt2.setMetaDataFormat( mff );
        ddt2.setMetaDataArchiveFormat( mfaf );

        Properties prop2 = new Properties( );
        DataDescriptorPropertyWriter ddw = new DataDescriptorPropertyWriter( prop2 );
        DataDescriptorConverter dcon = new DataDescriptorConverter( ddw, ddt2 );
        dcon.convert( );

        try {
            prop2.store( System.out, "just downloading?" );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
        if( prop2.containsKey( SfrProperty.JUST_DOWNLOAD.key ) )
            System.out.println( ">>>>>>>>>>> JUST_DOWNLOAD contained");
        else
            throw new IllegalStateException( "JUST_DOWNLOAD missing" );

        DataDescriptorPropertyReader ddpr = new DataDescriptorPropertyReader( prop2 );
        ddpr.performReading();
        DataDescriptor ddt3 = ddpr.getProduct();

        Properties prop3 = new Properties( );
        ddw = new DataDescriptorPropertyWriter( prop3 );
        dcon.setWriter( ddw );
        dcon.setModel( ddt3 );
        dcon.convert( );

        if( prop2.equals( prop3 ) )
            System.out.println( "Prop write read write: OKAY" );
        else
            System.out.println( "Prop write read write: ERROR (different results)"  );

    }

    public static void showORQ( ProviderStageInOrder order ) {

        ProviderStageInOrderStdoutWriter stdwrt = new ProviderStageInOrderStdoutWriter( );
        ProviderStageInOrderConverter conv = new ProviderStageInOrderConverter( stdwrt, order );
        conv.convert( );
    }

}
