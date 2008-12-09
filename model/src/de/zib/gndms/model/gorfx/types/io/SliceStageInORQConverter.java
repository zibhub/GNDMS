package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 11:15:19
 * To change this template use File | Settings | File Templates.
 */
public class SliceStageInORQConverter extends ORQConverter<SliceStageInORQWriter, SliceStageInORQ>{

    public SliceStageInORQConverter() {
        
    }

    public SliceStageInORQConverter( SliceStageInORQWriter writer, SliceStageInORQ slicestageinORQ){
        super( writer, slicestageinORQ );    
    }

    public void convert(){
        super.convert();
        DataDescriptorWriter ddw = getWriter( ).getDataDescriptorWriter();
        getWriter().beginWritingDataDescriptor();
        DataDescriptorConverter ddc = new DataDescriptorConverter( ddw,  getModel().getDataDescriptor() );
        ddc.convert( );
        getWriter().doneWritingDataDescriptor();

        getWriter().writeDataFileName( getModel().getActDataFile() );

        if ( getModel().hasGridSite() )
            getWriter().writeGridSiteName( getModel().getGridSite() );

        getWriter().writeMetaDataFileName( getModel().getActMetadataFile() );
        getWriter().done();
    }
}
