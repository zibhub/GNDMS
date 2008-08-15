package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.AbstractAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:45:38
 */
public abstract class AbstractEntityAction<R> extends AbstractAction<R> implements EntityAction<R> {
    private EntityManager entityManager;
    private BatchUpdateAction<?> postponedActions;


    @Override
    public void initialize() {
        if( getEntityManager() == null )
            throw new NoEntityManagerException( );
    }


    @Override
    public final R execute( ) {
        final EntityManager em = getEntityManager();
        final EntityTransaction tx = em.getTransaction();
        if (tx.isActive())
            return execute(em);
        else
            try {
                tx.begin();
                R result = execute(em);
                tx.commit();
                return result;
            }
            catch (RuntimeException re) {
                    if (tx.isActive()) tx.rollback();
                    throw re;
            }
    }

    public abstract R execute( final @NotNull EntityManager em );

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            final EntityAction<?> entityAction = nextParentOfType(EntityAction.class);
            if (entityAction != null)
                entityManager = entityAction.getEntityManager();
        }

        return entityManager;
    }

    public void setEntityManager(final @NotNull EntityManager entityManagerParam) {
        doNotOverwrite("entityManager", entityManager);
        entityManager = entityManagerParam;
    }


    public final BatchUpdateAction<?> getPostponedActions() {
        if (postponedActions == null) {
            final EntityAction<?> entityAction = nextParentOfType(EntityAction.class);
            postponedActions = entityAction == null ? null : entityAction.getPostponedActions();
        }

        return postponedActions;
    }


    public final void setPostponedActions(
          @NotNull final BatchUpdateAction<?> postponedActionsParam) {
        doNotOverwrite("postponedActions", postponedActions);
        postponedActions = postponedActionsParam;
    }

}
