package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.CommonSliceResult;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 13:35:40
 */
public abstract class CommonSliceResultConverter<W extends CommonSliceResultWriter, R extends CommonSliceResult> extends TaskResultConverter<W,R>{

    protected CommonSliceResultConverter() {
    }


    protected CommonSliceResultConverter( W writer, R model ) {
        super( writer, model );
    }


    /**
     * Note this neither calls begin nor end of the writer.
     * So you can place this call wherever you like.
     */
    public void convert() {

        SliceRefWriter wrt = getWriter( ).getSliceRefWriter();

        getWriter().beginWritingSliceRef( );
        SliceRefConverter src = new SliceRefConverter( wrt, getModel().getSliceRef() );
        src.convert();
        getWriter().doneWritingSliceRef( );
    }
}
