package de.zib.gndms.logic.action;

import de.zib.gndms.model.common.GridEntity;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 16:24:56
 */
public interface ModelAction<M extends GridEntity, R> extends de.zib.gndms.logic.action.Action<R> {

    void setModel( M mdl );

    M getModel( );
}
