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
    private BatchUpdateAction<GridResource, ?> postponedActions;
    @SuppressWarnings({ "InstanceVariableNamingConvention" })
    private ModelUUIDGen UUIDGen;   // the uuid generator
    private boolean runningOwnPostponedActions = true;
    private boolean closingEntityManagerOnCleanup = true;

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
        if (isExecutingInsideTransaction()) {
            return executeInsideTransaction(em);
        }
        else
         return execute(em);
    }

    @SuppressWarnings({ "MethodMayBeStatic" })
    protected boolean isExecutingInsideTransaction() {
        return true;
    }


    private R executeInsideTransaction(final EntityManager emParam) {
        final EntityTransaction tx = emParam.getTransaction();
        if (tx.isActive())
            return execute(emParam);
        else
            try {
                tx.begin();
                R result = execute(emParam);
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

    
    public EntityManager getOwnEntityManager() {
        return entityManager;
    }


    public void setOwnEntityManager(final @NotNull EntityManager entityManagerParam) {
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


    public final void setOwnPostponedActions(
          @NotNull final BatchUpdateAction<GridResource, ?> postponedActionsParam) {
        doNotOverwrite("postponedActions", postponedActions);
        postponedActions = postponedActionsParam;
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

    public void addChangedModel( GridResource model  ) {
        getPostponedActions( ).addAction( new ModelChangedAction( model ) );
    }


    public boolean hasOwnPostponedActions() {
        return postponedActions != null;
    }

    public boolean isRunningOwnPostponedActions() {
        return runningOwnPostponedActions;
    }


    public void setRunningOwnPostponedActions(final boolean runningOwnPostponedActionsParam) {
        runningOwnPostponedActions = runningOwnPostponedActionsParam;
    }


    @Override
    public void cleanUp() {
        final BatchUpdateAction<GridResource, ?> batched = getPostponedActions();
        if (hasOwnPostponedActions() && isRunningOwnPostponedActions())
            postponedActions.call();
        if (isClosingEntityManagerOnCleanup() && getEntityManager() != null)
            if (getEntityManager().isOpen())
                getEntityManager().close();
        super.cleanUp();    // Overridden method
    }


    public boolean isClosingEntityManagerOnCleanup() {
        return closingEntityManagerOnCleanup;
    }


    public void setClosingEntityManagerOnCleanup(final boolean closingEntityManagerOnCleanupParam) {
        closingEntityManagerOnCleanup = closingEntityManagerOnCleanupParam;
    }
}
