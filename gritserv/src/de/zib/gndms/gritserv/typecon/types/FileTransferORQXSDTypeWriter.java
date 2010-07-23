package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQWriter;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.URI;
import types.FileTransferORQT;

import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 14:37:42
 */
public class FileTransferORQXSDTypeWriter extends AbstractXSDTypeWriter<FileTransferORQT> implements FileTransferORQWriter {

    public void writeSourceURI( String uri ) {
        try {
            getProduct().get_any( )[0].setObjectValue( new URI( uri ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void writeDestinationURI( String uri ) {

        try {
            getProduct().get_any( )[1].setObjectValue( new URI( uri ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void writeFileMap( Map<String, String> fm ) {

        FieldDesc fd = FileTransferORQT.getTypeDesc().getFieldByName( "files" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            al.add(
                GORFXClientTools.createElementForField( fd, XSDReadWriteAux.write( fm ) ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );

    }


    public void writeJustEstimate( boolean je ) {
        // Not required here
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // Not required here
    }


    public void writeId( String id ) {
        // Not required here
    }


    public void begin() {
       try {
            setProduct( GORFXClientTools.createEmptyFileTransferORQT() );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( InstantiationException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void done() {
        // Not required here
    }

    /**
     * On shot write resp. convert operation.
     *
     * @param orq Source transfer  orq.
     * @return XSD version of the source orq.
     */
    public static FileTransferORQT write( FileTransferORQ orq ) {

        FileTransferORQXSDTypeWriter writer = new FileTransferORQXSDTypeWriter();
        FileTransferORQConverter conv = new FileTransferORQConverter( writer, orq );
        conv.convert();

        return writer.getProduct();
    }
}
