package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQWriter;
import de.zib.gndms.typecon.common.GORFXClientTools;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;
import types.ProviderStageInORQT;

import javax.xml.soap.SOAPException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

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
        FieldDesc fd = ProviderStageInORQT.getTypeDesc().getFieldByName( "dataFile" );
        LinkedList<MessageElement> ll = new LinkedList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            ll.add( 1,
                GORFXClientTools.createElementForField( fd, dfn ) );
        } catch ( SOAPException e ) {
            boxException( e );
        }
        getProduct().set_any( ll.toArray( new MessageElement[ll.size()] ) );
    }


    public void writeMetaDataFileName( String mfn ) {
        try {
            MessageElement[] mes = getProduct().get_any();
            int idx = mes.length - 1;
            mes[idx].setObjectValue( mfn );
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


    public void writeId( String id ) {
        // Not required here
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
