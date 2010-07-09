package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import de.zib.gndms.model.gorfx.types.io.SliceStageInResultConverter;
import de.zib.gndms.model.gorfx.types.SliceStageInResult;
import types.SliceStageInResultT;

import javax.xml.soap.SOAPException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 10:14:21
 */
public class SliceStageInResultXSDTypeWriter extends CommonSliceResultXSDTypeWriter<SliceStageInResultT> {


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
            setProduct( GORFXClientTools.createSliceStageInResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public static SliceStageInResultT writeResult( SliceStageInResult res ) {

        final SliceStageInResultXSDTypeWriter writer = new SliceStageInResultXSDTypeWriter();
        final SliceStageInResultConverter conv = new SliceStageInResultConverter( writer, res );
        conv.convert();

        return writer.getProduct();
    }
}
