package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:02:26
 */
public class ProviderStageInORQConverter extends ORQConverter<ProviderStageInORQWriter, ProviderStageInORQ> {


    public ProviderStageInORQConverter() {
    }


    public ProviderStageInORQConverter( ProviderStageInORQWriter writer, ProviderStageInORQ providerStageInORQ ) {
        super( writer, providerStageInORQ );
    }


    public void convert( ) {

        super.convert( );
        DataDescriptorWriter ddw = getWriter( ).getDataDescriptorWriter();
        DataDescriptorConverter ddc = new DataDescriptorConverter( ddw,  getModel().getDataDescriptor() );
        ddc.convert( );
        getWriter().writeDataFileName( getModel().getDataFile() );
        getWriter().writeMetaDataFileName( getModel().getMetadataFile() );
        getWriter().done ();
    }
}
