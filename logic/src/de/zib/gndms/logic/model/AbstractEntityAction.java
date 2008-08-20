package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.AbstractAction;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
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
    private BatchUpdateAction<GridResource, Object> postponedActions;
    @SuppressWarnings({ "InstanceVariableNamingConvention" })
    private ModelUUIDGen UUIDGen;   // the uuid generator


    @Override
    public void initialize() {
        if( getEntityManager() == null )
            throw new NoEntityManagerException( );
        
        // if( getUUIDGen() == null )
        //    throw new IllegalThreadStateException( "No UUId generator provided" );
        if (postponedActions != null)
            postponedActions.initialize();
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
                if (tx.isActive())
                    tx.rollback();
                throw re;
            }
    }

    public abstract R execute( final @NotNull EntityManager em );

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            final EntityAction<?> entityAction = nextParentOfType(EntityAction.class);
            if (entityAction != null)
                return entityAction.getEntityManager();
        }

        return entityManager;
    }

    public void setEntityManager(final @NotNull EntityManager entityManagerParam) {
        doNotOverwrite("entityManager", entityManager);
        entityManager = entityManagerParam;
    }


    public final BatchUpdateAction<GridResource, ?> getPostponedActions() {
        if (postponedActions == null) {
            final EntityAction<?> entityAction = nextParentOfType(EntityAction.class);
            if (entityAction != null)
                return entityAction.getPostponedActions();
        }

        return postponedActions;
    }


    public final void setPostponedActions(
          @NotNull final BatchUpdateAction<GridResource, ?> postponedActionsParam) {
        doNotOverwrite("postponedActions", postponedActions);
        postponedActions = (BatchUpdateAction<GridResource, Object>) postponedActionsParam;
    }


    public ModelUUIDGen getUUIDGen() {
        if (UUIDGen == null) {
            AbstractEntityAction<?> action = nextParentOfType(AbstractEntityAction.class);
            return action == null ? null : action.getUUIDGen();
        }
        return UUIDGen;
    }


    public void setUUIDGen(final ModelUUIDGen UUIDGenParam) {
        UUIDGen = UUIDGenParam;
    }

    public final @NotNull String nextUUID() {
        final ModelUUIDGen uuidGen = getUUIDGen();
        if (uuidGen == null) {
            final ModelUUIDGen uuidGen2 = nextParentOfType(ModelUUIDGen.class);
            if (uuidGen2 == null)
                throw new IllegalStateException("Cant find ModelUUIDGen");
            return uuidGen2.nextUUID();
        }
        else
            return uuidGen.nextUUID();
    }
}
