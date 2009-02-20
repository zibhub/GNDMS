package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:44:57
 */
public interface EntityAction<R> extends Action<R>, ModelUUIDGen {


    /**
     * Returns the EntityManager as set by {@code setOwnEntityManager}.
     * If not set, the first EntityManger of the parents of this action, being not {@code null}, will be returned.
     * If no parent has an EntityManager, {@code null} will be returned.
     * @return the first appereance of an EntityManager in the parent chain of this action
     */
    EntityManager getEntityManager();

    /**
     * Returns the EntityManager as set by {@code setOwnEntityManager}
     * @return the EntityManager as set by {@code setOwnEntityManager}
     */
    EntityManager getOwnEntityManager();

    void setOwnEntityManager(final @NotNull EntityManager entityManagerParam);


    BatchUpdateAction<GridResource, ?> getPostponedActions();


    void setOwnPostponedActions(final @NotNull BatchUpdateAction<GridResource, ?> postponedActionsParam);

}
