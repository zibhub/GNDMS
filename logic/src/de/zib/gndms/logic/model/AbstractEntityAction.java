package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.AbstractAction;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


/**
 * A default implementation of the {@code EntityAction} Interface.
 *
 * The template parameter is the return type.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:45:38
 */
public abstract class AbstractEntityAction<R> extends AbstractAction<R> implements EntityAction<R> {
    private EntityManager entityManager;
    /**
     * Postponed actions are executed during cleanup.
     */
    private BatchUpdateAction<GridResource, ?> postponedActions;
    @SuppressWarnings({ "InstanceVariableNamingConvention" })
    private ModelUUIDGen UUIDGen;   // the uuid generator
    private boolean runningOwnPostponedActions = true;
    private boolean closingEntityManagerOnCleanup = true;


    /**
     * Will be invoked before execute() when this is submitted to an Executor.
     * Initializes {@code postponed actions}.
     */
    @Override
    public void initialize() {

        if( getEntityManager() == null )     
            throw new NoEntityManagerException( );
        
        // if( getUUIDGen() == null )
        //    throw new IllegalThreadStateException( "No UUId generator provided" );
        if (postponedActions != null)
            postponedActions.initialize();
    }


    /**
     * Executes the EntityManager on its persistence context.
     * 
     * @return the result of the EntityManager being invoked on a its persistence context.
     */
    @Override
    public final R execute( ) {
        final EntityManager em = getEntityManager();
        if (isExecutingInsideTransaction()) {
            return executeInsideTransaction(em);
        }
        else
         return execute(em);
    }

    /**
     * Returns true by default
     * @return true by default
     */
    @SuppressWarnings({ "MethodMayBeStatic" })
    protected boolean isExecutingInsideTransaction() {
        return true;
    }

    /**
     * Tries to execute an EntityManager inside a transaction.
     *
     * If not already active, the transaction corresponding to the EntityManager will be activated
     * and the EntityManager executed.
     *
     * If an errpr occurs durings the transaction, it will be rolled back.
     * 
     * @param emParam
     * @return
     */
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


    /**
     * Defines how the EntityManager is executed on its persistence context.
     *
     * @param em the EntityManager  being executed on its persistence context.
     * @return the result of the execution
     */
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
        //doNotOverwrite("entityManager", entityManager);
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

    /**
     * Adds a {@code ModelChangedAction} to the postponed actions.
     * 
     * @param model the model which has been changed
     */
    public void addChangedModel( GridResource model  ) {
        getPostponedActions( ).addAction( new ModelChangedAction( model ) );
    }


    /**
     * Returns true, if this action has postponed actions.
     *
     * @return true, if this action has postponed actions.
     */
    public boolean hasOwnPostponedActions() {
        return postponedActions != null;
    }

    /**
     * Returns whether the postponned actions should be executed on {@code cleanUp}
     * 
     * @return whether the postponned actions should be executed on {@code cleanUp}
     */
    public boolean isRunningOwnPostponedActions() {
        return runningOwnPostponedActions;
    }


    public void setRunningOwnPostponedActions(final boolean runningOwnPostponedActionsParam) {
        runningOwnPostponedActions = runningOwnPostponedActionsParam;
    }


    /**
     * Executes postponed actions, if {@code isRunningOwnPostponedActions()} is true
     * and calls {@code super.cleanUp()}.
     * 
     */
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

    /**
     * Returns whether the EntityManager should be closed on cleanup
     * @return whether the EntityManager should be closed on cleanup
     */
    public boolean isClosingEntityManagerOnCleanup() {
        return closingEntityManagerOnCleanup;
    }

    /**
     * Decides whether the EntityManager should be closed on cleanup
     * 
     * @param closingEntityManagerOnCleanupParam if ture the EntityManager will be closed on cleanup
     */
    public void setClosingEntityManagerOnCleanup(final boolean closingEntityManagerOnCleanupParam) {
        closingEntityManagerOnCleanup = closingEntityManagerOnCleanupParam;
    }
}
