package de.zib.gndms.gritserv.typecon.types;

import types.SliceStageInORQT;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQConverter;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;

import javax.xml.soap.SOAPException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;

import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 08:51:08
 */
public class SliceStageInORQXSDTypeWriter extends AbstractXSDTypeWriter<SliceStageInORQT>
    implements SliceStageInORQWriter {

    private DataDescriptorXSDTypeWriter dataWriter;


    public void writeGridSiteName( String gsn ) {
        addElement( 0, "gridSite", gsn );
    }


    public void writeDataFileName( String dfn ) {
        addElement( 2, "dataFile", dfn );
    }


    public void writeMetaDataFileName( String mfn ) {
        try {
            String fn = "MetadataFile";
            MessageElement me = findElement( fn );
            me.setObjectValue( mfn );
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
            String fn = "StagedData";
            MessageElement me = findElement( fn );
            me.setObjectValue( dataWriter.getProduct( ) );
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
            setProduct( GORFXClientTools.createEmptySliceStageInORQT( )  );
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
    public static SliceStageInORQT write( SliceStageInORQ orq ) {

        SliceStageInORQXSDTypeWriter writer = new SliceStageInORQXSDTypeWriter();
        SliceStageInORQConverter conv = new SliceStageInORQConverter( writer, orq );
        conv.convert();

        return writer.getProduct();
    }


    protected void addElement( int idx, String nam, Object elm ) {

        FieldDesc fd = SliceStageInORQT.getTypeDesc().getFieldByName( nam );
        LinkedList<MessageElement> ll = new LinkedList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            ll.add( idx,
                GORFXClientTools.createElementForField( fd, elm ) );
        } catch ( SOAPException e ) {
            boxException( e );
        }
        getProduct().set_any( ll.toArray( new MessageElement[ll.size()] ) );
    }


    protected MessageElement findElement( String nam ) {

        MessageElement[] mes = getProduct().get_any();

        if( mes == null )
            return null;

        for ( MessageElement me : mes ) {
            if ( nam.equals( me.getName() ) )
                return me;
        }

        return null;
    }
}
