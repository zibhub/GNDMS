package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import types.ContextT;
import types.DynamicOfferDataSeqT;
import types.FileMappingSeqT;
import org.apache.axis.message.MessageElement;

/**
 * This class is used to create a FileTransfer instance out of a {@code DynamicOfferDataSeqT} object.
 * To opposite way is done by {@link de.zib.gndms.typecon.common.type.FileTransferORQXSDTypeWriter}.
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:14:47
 */
public class FileTransferORQXSDReader {

    /**
     * Creates a {@code FileTransferORQ} instance out of a given {@code DynamicOfferDataSeqT} along with its ContextT parameter
     *
     * @param orq
     * @param ctx
     * @return
     */
    public static FileTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {

        try {
            FileTransferORQ torq = AbstractORQXSDReader.read( FileTransferORQ.class, ctx );

            MessageElement[] mes = orq.get_any();

            if( mes.length < 2 )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > 3 )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            torq.setSourceURI( ( String ) mes[0].getObjectValue( String.class ) );
            torq.setTargetURI( ( String ) mes[1].getObjectValue( String.class ) );

            if( mes.length == 3 ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
}
