package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.LofiSetStageInORQ;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:38:55
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQPropertyReader extends ORQPropertyReader<LofiSetStageInORQ>{

    public LofiSetStageInORQPropertyReader(){
       super( LofiSetStageInORQ.class ); 
    }
    protected LofiSetStageInORQPropertyReader( Properties properties) {
        super(LofiSetStageInORQ.class, properties);
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
        
    }
}
