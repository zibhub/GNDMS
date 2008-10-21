package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.LofiSetStageInORQ;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:54:03
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQConverter extends ORQConverter<LofiSetStageInORQWriter, LofiSetStageInORQ>{

    public LofiSetStageInORQConverter(){

    }

    public LofiSetStageInORQConverter(LofiSetStageInORQWriter writer, LofiSetStageInORQ lofiSetStageInORQ){
        super( writer, lofiSetStageInORQ);
    }

    public void convert(){
        super.convert();
        DataDescriptorWriter ddw = getWriter( ).getDataDescriptorWriter();
        DataDescriptorConverter ddc = new DataDescriptorConverter( ddw,  getModel().getDataDescriptor() );
        ddc.convert( );

        getWriter().writeDataFileName( getModel().getDataFile() );
        if ( getModel().hasGridSite())
        getWriter().writeGridSiteName( getModel().getGridSite() );
        getWriter().writeMetaDataFileName( getModel().getMetadataFile() );
        getWriter().done();
    }


}
