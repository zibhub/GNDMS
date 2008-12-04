package de.zib.gndms.model.gorfx.types.io.tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 03.11.2008, Time: 12:31:56
 */
public class ProviderStageInFromFile {


    @Parameters( { "fileName" })
    @Test( groups={"io"} )
    public void testIt( String fileName ) throws Exception{

        try {
            System.out.println( "\nOrq input test: ");
            System.out.println( "----------------");
            InputStream is = new FileInputStream( fileName );
            Properties prop = new Properties( );
            prop.load( is );
            is.close( );
            ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( prop );
            reader.performReading( );
            ProviderStageInORQ orq =  reader.getProduct();
            ProviderStageInORQIOTest.showORQ( orq );
            System.out.println( "\nOrq output test: ");
            System.out.println( "-----------------");
            Properties np = new Properties( );
            ProviderStageInORQPropertyWriter writer = new ProviderStageInORQPropertyWriter( np );
            ProviderStageInORQConverter conv = new ProviderStageInORQConverter( writer, orq );
            conv.convert();

            np.store( System.out, "some props" );
        } catch ( FileNotFoundException e ) {
            System.out.println( "File not found make!" );
            System.out.println( "Please make sure your working dir contains " + fileName );
        }

    }
}
