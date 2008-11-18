package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.io.CommonSliceResultWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefWriter;
import de.zib.gndms.model.gorfx.types.io.RePublishSliceResultConverter;
import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.typecon.common.GORFXClientTools;
import types.RePublishSliceResultT;

import javax.xml.soap.SOAPException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 10:14:21
 */
public class RePublishSliceResultXSDTypeWriter extends CommonSliceResultXSDTypeWriter<RePublishSliceResultT> {



    public void doneWritingSliceRef() {
        if( getSliceRefWriter( ) == null )
            throw new IllegalStateException( "no slice ref writer present" );

        try {
            getProduct( ).get_any()[0].setObjectValue( ( (SliceRefXSDTypeWriter) getSliceRefWriter() ).getProduct() );
        } catch ( SOAPException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createRePublishSliceResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public static RePublishSliceResultT writeResult( RePublishSliceResult res ) {

        final RePublishSliceResultXSDTypeWriter writer = new RePublishSliceResultXSDTypeWriter();
        final RePublishSliceResultConverter conv = new RePublishSliceResultConverter( writer, res );
        conv.convert();

        return writer.getProduct();
    }
}
