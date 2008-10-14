package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.AbstractTaskResult;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 12:42:34
 */
public abstract class TaskResultConverter<W extends TaskResultWriter, M extends AbstractTaskResult> extends GORFXConverterBase<W,M>{

    protected TaskResultConverter() {
    }


    protected TaskResultConverter( W writer, M model ) {
        super( writer, model );
    }


    /*
     * call this method in the convert method of inheriting clas
     * 
     */
    protected void checkedConvert( ) {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
    }
}
