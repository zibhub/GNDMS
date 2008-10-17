package de.zib.gndms.typecon.common.type;

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
