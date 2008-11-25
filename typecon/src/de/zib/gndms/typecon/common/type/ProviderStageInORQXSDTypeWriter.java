package de.zib.gndms.typecon.common.type;

import types.ProviderStageInORQT;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.typecon.common.GORFXClientTools;

import javax.xml.soap.SOAPException;
import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 08:51:08
 */
public class ProviderStageInORQXSDTypeWriter extends AbstractXSDTypeWriter<ProviderStageInORQT>
    implements ProviderStageInORQWriter {

    private DataDescriptorXSDTypeWriter dataWriter;

    public void writeDataFileName( String dfn ) {
        try {
            getProduct().get_any()[1].setObjectValue( dfn );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public void writeMetaDataFileName( String mfn ) {
        try {
            getProduct().get_any()[2].setObjectValue( mfn );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public DataDescriptorWriter getDataDescriptorWriter() {

        if( dataWriter == null )
            dataWriter = new DataDescriptorXSDTypeWriter();
        return dataWriter;
    }


    public void beginWritingDataDescriptor() {
        if( dataWriter == null )
            throw new IllegalStateException( "no data writer present" );
    }


    public void doneWritingDataDescriptor() {
        if( dataWriter == null )
            throw new IllegalStateException( "no data writer present" );
        try {
            getProduct().get_any()[0].setObjectValue( dataWriter.getProduct( ) );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public void writeJustEstimate( boolean je ) {
        // not required in XSD context.
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // not required in XSD context.
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createEmptyProviderStageInORQT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    protected void boxException( Exception e ) {
        throw new RuntimeException( e.getMessage(), e );
    }


    /**
     * On shot write resp. convert operation.
     *
     * @param orq Source provider stage in orq.
     * @return XSD version of the source orq.
     */
    public static ProviderStageInORQT write( ProviderStageInORQ orq ) {

        ProviderStageInORQXSDTypeWriter writer = new ProviderStageInORQXSDTypeWriter();
        ProviderStageInORQConverter conv = new ProviderStageInORQConverter( writer, orq );
        conv.convert();

        return writer.getProduct();
    }
}
