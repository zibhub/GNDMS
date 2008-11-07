package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:48:27
 */
public class InterSliceTransferORQConverter extends ORQConverter<InterSliceTransferORQWriter, InterSliceTransferORQ> {

    public InterSliceTransferORQConverter() {
    }


    public InterSliceTransferORQConverter( InterSliceTransferORQWriter writer, InterSliceTransferORQ model ) {
        super( writer, model );
    }


    public void convert() {
        super.convert();    //To change body of overridden methods use File | Settings | File Templates.
        getWriter( ).writeSourceSlice( getModel().getSourceSlice() );
        getWriter( ).writeDestinationSlice( getModel().getDestinationSlice() );
        if( getModel( ).hasFileMap( ) )
            getWriter( ).writeFileMap( getModel().getFileMap() );

        getWriter().done( );
    }
}
