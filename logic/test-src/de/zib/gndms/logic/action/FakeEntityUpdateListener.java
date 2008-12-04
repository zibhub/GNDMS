package de.zib.gndms.logic.action;

import de.zib.gndms.logic.model.EntityUpdateListener;
import de.zib.gndms.model.common.GridResource;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.10.2008, Time: 14:56:09
 */
public class FakeEntityUpdateListener<M extends GridResource> implements EntityUpdateListener<M> {

    public void onModelChange( M model ) {
        System.out.println( "onModelChange called with model id: "+ model.getId() );
    }
}
