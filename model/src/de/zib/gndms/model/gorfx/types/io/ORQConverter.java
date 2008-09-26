package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.AbstractORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:52:45
 */
public abstract class ORQConverter<W extends ORQWriter, M extends AbstractORQ> extends GORFXConverterBase<W, M> {

    protected ORQConverter() {
    }


    protected ORQConverter( W writer, M model ) {
        super( writer, model );
    }

    public void convert( ) {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeJustEstimate( getModel().getJustEstimate() );
    }
}
