package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 09.10.2008
 * Time: 18:16:38
 * To change this template use File | Settings | File Templates.
 */
public class SliceStageInORQPropertyReader extends ORQPropertyReader<SliceStageInORQ>{

    public SliceStageInORQPropertyReader( ) {
        super( SliceStageInORQ.class );
    }

    public SliceStageInORQPropertyReader( Properties properties ) {
        super( SliceStageInORQ.class, properties );
    }

    public void read() {

        super.read();

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setDataDescriptor( dr.getProduct() );

        if(! getProperties().containsKey( SfrProperty.GRID_SITE.key ) )
          getProduct().setGridSite( getProperties().getProperty( SfrProperty.GRID_SITE.key ) );
        getProduct( ).setDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        getProduct( ).setMetadataFile( getProperties().getProperty( SfrProperty.META_FILE.key ) );

    }

    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
