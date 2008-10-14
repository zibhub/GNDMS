package de.zib.gndms.GORFX.common.type.io;

import types.FileTransferResultT;
import de.zib.gndms.model.gorfx.types.io.FileTransferResultWriter;
import de.zib.gndms.GORFX.common.GORFXClientTools;

import javax.xml.soap.SOAPException;

import org.apache.axis.message.MessageElement;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.description.FieldDesc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:18:40
 */
public class FileTransferResultXSDTypeWriter extends AbstractXSDTypeWriter<FileTransferResultT> implements FileTransferResultWriter {


    public void writeFiles( String[] files ) {

        FieldDesc fd = FileTransferResultT.getTypeDesc().getFieldByName( "file" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );
        for( int i=0; i < files.length; ++i ) {
            try {
                al.add( GORFXClientTools.createElementForField( fd, files[i] ) );
            } catch ( SOAPException e ) {
                throw new IllegalStateException( "SOAPException occurred: " + e.getMessage( ) );
            }
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createFileTransferResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        // Not required here
    }
}
