package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.Action;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:44:57
 */
public interface EntityAction<R> extends Action<R> {
    EntityManager getEntityManager();

    void setEntityManager(final @NotNull EntityManager entityManagerParam);


    BatchUpdateAction<?> getPostponedActions();

    void setPostponedActions(final @NotNull BatchUpdateAction<?> postponedActionsParam);
    
}
