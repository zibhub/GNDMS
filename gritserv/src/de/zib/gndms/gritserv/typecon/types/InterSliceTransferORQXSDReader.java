package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import org.apache.axis.message.MessageElement;
import types.DynamicOfferDataSeqT;
import types.ContextT;
import types.SliceReference;
import types.FileMappingSeqT;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 18:36:24
 */
public class InterSliceTransferORQXSDReader {

    private static final int MIN_ME = 2; // ME = Message Element
    private static final int MAX_ME = 3;

    public static InterSliceTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {

        try {

            MessageElement[] mes = orq.get_any();

            if( mes.length < MIN_ME )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > MAX_ME )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            InterSliceTransferORQ torq = AbstractORQXSDReader.read( InterSliceTransferORQ.class, ctx );

            SliceReference sr =  ( SliceReference ) mes[0].getObjectValue( SliceReference.class );
            torq.setSourceSlice( SliceRefXSDReader.read( sr ) );
            sr = ( SliceReference ) mes[1].getObjectValue( SliceReference.class );
            torq.setDestinationSlice( SliceRefXSDReader.read( sr ) );

            if( mes.length == MAX_ME ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );  //To change body of catch statement use InterSlice | Settings | InterSlice Templates.
        }
    }
}
