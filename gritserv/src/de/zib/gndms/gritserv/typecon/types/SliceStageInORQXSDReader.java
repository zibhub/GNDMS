package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import types.ContextT;
import types.DynamicOfferDataSeqT;
import org.apache.axis.message.MessageElement;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 14.01.2009, Time: 12:41:15
 */
public class SliceStageInORQXSDReader {

    public static SliceStageInORQ read(  DynamicOfferDataSeqT orqt, ContextT ctx ) {

        try {
            MessageElement[] mes = orqt.get_any();

            int idx = 0;
            MessageElement me = mes[idx];
            String gs = null;
            if( me.getElementName().getLocalName().equals( "GridSite" ) ) {
                gs = (String) me.getObjectValue( String.class );
                ++idx;
            }
            SliceStageInORQ orq = ProviderStageInORQXSDReader.read( SliceStageInORQ.class, orqt, ctx, idx );
            orq.setGridSite( gs );
            return orq;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
    
}
