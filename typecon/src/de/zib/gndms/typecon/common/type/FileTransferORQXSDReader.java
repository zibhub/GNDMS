package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import types.ContextT;
import types.DynamicOfferDataSeqT;
import types.FileMappingSeqT;
import org.apache.axis.message.MessageElement;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:14:47
 */
public class FileTransferORQXSDReader {

    public static FileTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {

        try {
            FileTransferORQ torq = AbstractORQXSDReader.read( FileTransferORQ.class, ctx );

            MessageElement[] mes = orq.get_any();

            if( mes.length < 2 )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > 3 )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            torq.setSourceURI( mes[0].getObjectValue().toString( ) );
            torq.setTargetURI( mes[1].getObjectValue().toString( ) );

            if( mes.length == 3 ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( InstantiationException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( Exception e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
