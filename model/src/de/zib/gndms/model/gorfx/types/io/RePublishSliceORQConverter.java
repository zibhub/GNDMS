package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.RePublishSliceORQ;

/**
 * Converter for publish slice orqs.
 *
 * Pretty much the same as the InterSliceTransfer converter, but doesn't write the destination slice,
 * cause it isn't part of the initial request.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:48:27
 */
public class RePublishSliceORQConverter extends ORQConverter<RePublishSliceORQWriter, RePublishSliceORQ> {

    public RePublishSliceORQConverter() {
    }


    public RePublishSliceORQConverter( RePublishSliceORQWriter writer, RePublishSliceORQ model ) {
        super( writer, model );
    }


    public void convert() {
        super.convert();
        getWriter( ).writeSourceSlice( getModel().getSourceSlice() );
        if( getModel( ).hasFileMap( ) )
            getWriter( ).writeFileMap( getModel().getFileMap() );

        getWriter().done( );
    }
}
