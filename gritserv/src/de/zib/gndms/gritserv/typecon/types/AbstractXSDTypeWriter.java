package de.zib.gndms.gritserv.typecon.types;

/**
 * An AbstractXSDTypeWriter is used to convert a gndms class to the corresponding axis type.
 * It should be used in conjunction with {@link de.zib.gndms.model.gorfx.types.io.ORQWriter}
 * It contains the created axis type.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:10:31
 */
public abstract class AbstractXSDTypeWriter<M> {

    private M product;


    public M getProduct ( ) {
        return product;
    }


    protected void setProduct( M p ) {
        product = p;
    }
}
