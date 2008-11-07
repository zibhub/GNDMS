package de.zib.gndms.typecon.common.type;

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


    public static InterSliceTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {
        
        try {
            InterSliceTransferORQ torq = AbstractORQXSDReader.read( InterSliceTransferORQ.class, ctx );

            MessageElement[] mes = orq.get_any();

            if( mes.length < 2 )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > 3 )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            SliceReference sr =  ( SliceReference ) mes[0].getObjectValue( SliceReference.class );
            torq.setSourceSlice( SliceRefXSDReader.read( sr ) );
            sr = ( SliceReference ) mes[1].getObjectValue( SliceReference.class );
            torq.setDestinationSlice( SliceRefXSDReader.read( sr ) );

            if( mes.length == 3 ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( InstantiationException e ) {
            e.printStackTrace();  //To change body of catch statement use InterSlice | Settings | InterSlice Templates.
        } catch ( Exception e ) {
            e.printStackTrace();  //To change body of catch statement use InterSlice | Settings | InterSlice Templates.
        }
        return null;
    }
}
