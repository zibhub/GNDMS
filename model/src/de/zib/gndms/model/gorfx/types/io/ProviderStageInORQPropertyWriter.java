package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataDescriptor;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 10:08:18
 */
public class ProviderStageInORQPropertyWriter extends AbstractPropertyIO implements ProviderStageInORQWriter {

    public ProviderStageInORQPropertyWriter() {
    }


    public ProviderStageInORQPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeDataDescriptor( DataDescriptor dps ) {
        DataDescriptorPropertyWriter dpw = new DataDescriptorPropertyWriter( getProperties() );
        DataDescriptorConverter conv = new DataDescriptorConverter( dpw, dps );
        conv.convert( );
    }


    public void writeDataFileName( String dfn ) {
        getProperties( ).setProperty( SfrProperty.BASE_FILE.key, dfn );
    }


    public void writeMetaDataFileName( String mfn ) {
        getProperties( ).setProperty( SfrProperty.META_FILE.key, mfn );
    }


    public DataDescriptorWriter getDataDescriptorWriter( ) {
        return new DataDescriptorPropertyWriter( getProperties() );
    }


    public void beginWritingDataDescriptor( ) {

    }


    public void doneWritingDataDescriptor( ) {

    }
    

    public void done() {
        // not required
    }
}
