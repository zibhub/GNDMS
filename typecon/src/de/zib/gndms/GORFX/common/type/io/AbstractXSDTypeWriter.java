package de.zib.gndms.GORFX.common.type.io;

/**
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
