package de.zib.gndms.typecon.common.type;

import types.RePublishSliceORQT;
import de.zib.gndms.model.gorfx.types.io.RePublishSliceORQWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefConverter;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.typecon.common.GORFXClientTools;

import javax.xml.soap.SOAPException;
import java.util.*;

import org.apache.axis.message.MessageElement;
import org.apache.axis.description.FieldDesc;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 05.11.2008, Time: 10:36:45
 */
public class RePublishSliceORQXSDTypeWriter extends AbstractXSDTypeWriter<RePublishSliceORQT>
    implements RePublishSliceORQWriter
{
    private SliceRefXSDTypeWriter sliceWriter;
    private SliceRefConverter sliceConverter;


    public RePublishSliceORQXSDTypeWriter( ) {
        sliceWriter = new SliceRefXSDTypeWriter();
        sliceConverter = new SliceRefConverter( );
        sliceConverter.setWriter( sliceWriter );
    }


    public void writeSourceSlice( SliceRef sr ) {
        
        sliceConverter.setModel( sr );
        sliceConverter.convert( );
        try {
            getProduct().get_any( )[0].setObjectValue( sliceWriter.getProduct() );
        } catch ( SOAPException e ) {
            throw new IllegalStateException( e.getMessage(), e );
        }
    }


    public void writeFileMap( TreeMap<String, String> fm ) {

        FieldDesc fd = RePublishSliceORQT.getTypeDesc().getFieldByName( "files" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            al.add(
                GORFXClientTools.createElementForField( fd, XSDReadWriteAux.write( fm ) ) );
        } catch ( SOAPException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );
    }


    public void writeJustEstimate( boolean je ) {
        // not required here
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // not required here
    }


    public void begin() {

        try {
            setProduct( GORFXClientTools.createEmptyRePublishSliceORQT() );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }


    public void done() {
        // not required here
    }


    public void writeId( String id ) {
        // Not required here
    }
}
