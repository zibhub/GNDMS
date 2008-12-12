package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;

import java.util.TreeMap;
import java.util.Properties;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:38:41
 */
public class FileTransferORQIOTest {

    public static void main( String[] args ) {
        FileTransferORQ orq = new FileTransferORQ( );
        orq.setSourceURI( "gsiftp://csr-priv5/home/mjorra/tmp/C3GridTests/dataTransferTestSource" );
        orq.setTargetURI( "gsiftp://csr-priv5/home/mjorra/C3GridTests/dataTransferTestTarget" );

        TreeMap<String, String> fileMap = new TreeMap<String, String>( );
        fileMap.put( "a_1KB_file", null );
        fileMap.put( "b_1MB_file", "b_1000KB_file" );
        fileMap.put( "c_1GB_file", "c_largeFile" );
        orq.setFileMap( fileMap );

        HashMap<String, String> ctx = new HashMap<String, String>( );
        ctx.put( "k1", "v1" );
        ctx.put( "k2", "v2" );
        ctx.put( "k3", "v3" );
        ctx.put( "k4", "v4" );
        orq.setActContext( ctx );

        FileTransferORQStdoutWriter cout = new FileTransferORQStdoutWriter( );
        FileTransferORQConverter conv = new FileTransferORQConverter( cout, orq );
        conv.convert( );

        Properties prop = new Properties( );
        FileTransferORQPropertyWriter propw = new FileTransferORQPropertyWriter( prop );
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

        FileTransferORQPropertyReader propr = new FileTransferORQPropertyReader( prop );
        propr.performReading();

        FileTransferORQ orq2 = propr.getProduct();
        System.out.println( "\nRead orq: " );
        conv.setModel( orq2 );
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
