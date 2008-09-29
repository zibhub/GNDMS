package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;

import java.util.HashMap;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.joda.time.DateTime;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 16:19:48
 */
public class ProviderStageInORQIOTest {

    public static void main( String[] args ) {

        DataDescriptor ddt = new DataDescriptor();
        String[] ol = { "hello", "world" };
        String[] cfl = { "foo", "bar", "foobar", "blubber", };
        HashMap<String,String> cl = new HashMap<String,String>( );
        cl.put( "gibson", "les-paul");
        cl.put( "fender", "Stratocaster" );
        cl.put( "duesenberg", "double-cat" );

        SpaceConstraint sc = new SpaceConstraint();
        sc.setLatitude( new MinMaxPair( -1.3, 5.7 ) );
        sc.setLongitude( new MinMaxPair( 2.7, 42.0 ) );
        sc.setAltitude( new LevelRange( new NumericAltitude( new Double( -20000 ), AltitudeUnit.METER ),
            new NamedAltitude( "Mt. Eribus" ), "Some vericalcrs"  ) );

        DateTime tm = new DateTime( );
        TimeConstraint tc = new TimeConstraint();
        tc.setMinTime( tm );
        tc.setMaxTime( tm.plusDays( 6 ) );

        String dff = "plain";
        String mff = "ini";

        ddt.setObjectList( ol );
        ddt.setCFList( cfl );
        ddt.setSpaceConstraint( sc );
        ddt.setTimeConstraint( tc );
        ddt.setConstraintList( cl );
        ddt.setDataFormat( dff );
        ddt.setMetaDataFormat( mff );

        ProviderStageInORQ orq = new ProviderStageInORQ();
        orq.setDataDescriptor( ddt );
        orq.setDataFile( "data_file" );
        orq.setMetadataFile( "meta_data_file" );

        Properties prop = new Properties( );
        ProviderStageInORQPropertyWriter wrt = new ProviderStageInORQPropertyWriter( prop );
        ProviderStageInORQConverter conv = new ProviderStageInORQConverter( wrt, orq );
        conv.convert();
        try {
            prop.store( System.out, "hallo welt" );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( prop );
        reader.begin( );
        reader.read();
        ProviderStageInORQ rorq = reader.getProduct();


        // example of converter reusage
        ProviderStageInORQStdoutWriter stdwrt = new ProviderStageInORQStdoutWriter( );
        conv.setWriter( stdwrt );
        conv.setModel( rorq );
        conv.convert( );

        System.out.println( "Reading test prop from file" );
        String fn = "prop_test_file.txt";
        try {
            InputStream is = new FileInputStream( fn );
            Properties np = new Properties( );
            np.load( is );
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
    }

    public static void showORQ( ProviderStageInORQ orq ) {

        ProviderStageInORQStdoutWriter stdwrt = new ProviderStageInORQStdoutWriter( );
        ProviderStageInORQConverter conv = new ProviderStageInORQConverter( stdwrt, orq );
        conv.convert( );
    }

}
