package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 15:21:09
 */
public class ProviderStageInORQPropertyReader extends AbstractPropertyReader<ProviderStageInORQ> {

    public ProviderStageInORQPropertyReader( ) {
        super( ProviderStageInORQ.class );
    }


    public ProviderStageInORQPropertyReader( Properties properties ) {
        super( ProviderStageInORQ.class, properties );
    }


    public void read() {

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setDataDescriptor( dr.getProduct() );

        getProduct( ).setDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        getProduct( ).setMetadataFile( getProperties().getProperty( SfrProperty.META_FILE.key ) );

    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
