package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.AbstractAction;
import de.zib.gndms.logic.action.ModelAction;
import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * An abstract action on a given persisten model.
 * 
 * The first template parameter is the model for this action, the second is the return type.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 16:36:20
 */
public abstract class AbstractModelAction<M extends GridEntity, R> extends AbstractAction<R>
	  implements ModelAction<M, R> {

    private M model;

	private EntityManager entityManager;

	private BatchUpdateAction<?> postponedActions;

	public M getModel() {
	    return model;
	}

	public void setModel(final @NotNull M mdl) {
	    model = mdl;
	}


	@Override
	public void initialize() {
		if( getEntityManager() == null )
		    throw new NoEntityManagerException( );
		if (getModel() == null)
			throw new IllegalStateException("No model set");
	}


	@Override
	public final R execute( ) {
		return execute(getEntityManager());
	}

    public abstract R execute( final @NotNull EntityManager em );

    public EntityManager getEntityManager() {
        if (entityManager == null) {
	        final ModelAction<?, ?> modelAction = nextParentOfType(ModelAction.class);
	        if (modelAction != null)
	            entityManager = modelAction.getEntityManager();
        }

	    return entityManager;
    }

    public void setEntityManager(final @NotNull EntityManager entityManagerParam) {
	    if (entityManager != null)
	        throw new IllegalStateException("Cant overwrite entityManager");
	    entityManager = entityManagerParam;
    }


	public final BatchUpdateAction<?> getPostponedActions() {
		if (postponedActions == null) {
			final ModelAction<?,?> modelAction = nextParentOfType(ModelAction.class);
			postponedActions = modelAction == null ? null : modelAction.getPostponedActions();
		}

		return postponedActions;
	}


	public final void setPostponedActions(
		  @NotNull final BatchUpdateAction<?> postponedActionsParam) {
		if (getPostponedActions() != null)
			throw new IllegalStateException("Cant overwrite postponedActions");

		postponedActions = postponedActionsParam;
	}
}
