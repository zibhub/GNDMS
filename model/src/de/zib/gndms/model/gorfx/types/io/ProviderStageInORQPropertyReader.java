package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 15:21:09
 */
public class ProviderStageInORQPropertyReader extends ORQPropertyReader<ProviderStageInORQ> {

    public ProviderStageInORQPropertyReader( ) {
        super( ProviderStageInORQ.class );
    }


    public ProviderStageInORQPropertyReader( Properties properties ) {
        super( ProviderStageInORQ.class, properties );
    }


    public void read() {

        super.read();

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setDataDescriptor( dr.getProduct() );

        if( getProperties( ).containsKey( SfrProperty.BASE_FILE.key ) )
            getProduct( ).setDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        
        getProduct( ).setMetadataFile( getMandatoryProperty( SfrProperty.META_FILE.key ) );
    }


    public void done() {
        // not required here
    }


    public static ProviderStageInORQ readFromFile( final String fileName ) throws IOException {

        InputStream is = null;
        try {
            is = new FileInputStream( fileName );
            Properties prop = new Properties( );
            prop.load( is );
            is.close( );
            ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( prop );
            reader.performReading( );
            return  reader.getProduct();
        } finally {
            if( is != null )
                is.close( );
        }
    }
}
