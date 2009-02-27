package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;


/**
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 16:24:56
 */
public interface ModelAction<M extends GridEntity, R> extends EntityAction<R> {

	M getModel( );

    void setModel( final @NotNull M mdl );


}
