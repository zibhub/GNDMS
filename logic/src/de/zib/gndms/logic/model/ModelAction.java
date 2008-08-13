package de.zib.gndms.logic.model;

import de.zib.gndms.logic.model.BatchUpdateAction;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 16:24:56
 */
public interface ModelAction<M extends GridEntity, R> extends Action<R> {

	M getModel( );

    void setModel( final @NotNull M mdl );


	EntityManager getEntityManager();

	void setEntityManager(final @NotNull EntityManager entityManagerParam);


	BatchUpdateAction<?> getPostponedActions();

	void setPostponedActions(final @NotNull BatchUpdateAction<?> postponedActionsParam);
}
