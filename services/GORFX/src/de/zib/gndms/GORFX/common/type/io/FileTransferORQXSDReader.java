package de.zib.gndms.GORFX.common.type.io;

import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import types.ContextT;
import types.FileTransferORQT;
import types.FileMappingSeqT;
import org.apache.axis.message.MessageElement;

import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:14:47
 */
public class FileTransferORQXSDReader {

    public static FileTransferORQ read( FileTransferORQT orq, ContextT ctx )  {

        try {
            FileTransferORQ torq = AbstractORQXSDReader.read( FileTransferORQ.class, ctx );

            MessageElement[] mes = orq.get_any();

            if( mes.length < 2 )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > 3 )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            torq.setSourceURI( mes[0].toString( ) );
            torq.setTargetURI( mes[1].toString( ) );

            if( mes.length == 3 ) {
                torq.setFileMap(
                     FromXSDReaderAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
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
