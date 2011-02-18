package de.zib.gndms.infra.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import com.google.common.base.Function;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.system.EMTools;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.kit.access.EMFactoryProvider;
import de.zib.gndms.kit.access.GNDMSBinding;
import de.zib.gndms.logic.model.*;
import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.GridResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Helper class for managing persistent models of GNDMS resources 
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:41:17
 */
public class GridEntityModelHandler<M extends GridEntity, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> implements EMFactoryProvider {

	final private Log logger = LogFactory.getLog(GridEntityModelHandler.class);

	private @NotNull Class<M> modelClass;

	private @NotNull GNDMServiceHome home;

	GridEntityModelHandler(final @NotNull Class<M> theClazz, final @NotNull H homeParam) {
		modelClass = theClazz;
		home = (GNDMServiceHome) homeParam;
	}


	final public @NotNull M createNewGridEntity() {
		return createNewEntity();
	}

	protected @NotNull M createNewEntity()
		{
            try {
                return getModelClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }


	/**
	 *  Calls the given model action after setting the corresponding parameters.
	 *
	 * There are several additional calls that just fix some of these parameters with sensible
	 * defaults: callResourceAction uses a resources' id to retrieve the model within the same
	 * transaction as the model action.  callNewResourceAction and callNewModelAction calls
	 * create a new EntityManager via this ModelHandlers' EntityManagerFactory. If no container
	 * for postponed actions is provided, a new DefaultBatchUpdateAction() is used.  If no
	 * ModelUpdateListener is provided, the one associated with this ModelHandler is used
	 * and set on postponedActions.
	 * 
	 */
	public <B> B callModelAction(
		final EntityManager emParam,
		final @NotNull BatchUpdateAction<GridResource, ?> postponedActions,
		final @Nullable ModelUpdateListener<GridResource> listener,
		final @NotNull de.zib.gndms.logic.model.ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return txRun(emParam, new Function<EntityManager, B>() {

            // Dear google, we disagree with your interface
            public B apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                if (listener != null)
                    postponedActions.setListener(listener);
               theAction.setOwnPostponedActions(postponedActions);
               theAction.setOwnEntityManager( em );
               theAction.setModel(theModel);
               return theAction.call();
            }
        });
	}

	/**
	 *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
	 */
	public final <B> B callModelAction(
		final EntityManager emParam,
		final @NotNull ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return callModelAction(emParam, new DefaultBatchUpdateAction<GridResource>(), listener, theAction,
		                       theModel);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callModelAction(
		final EntityManager emParam,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return callModelAction(emParam, getEntityUpdateListener(), theAction, theModel);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewModelAction(
		final @NotNull ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return callModelAction(null, listener, theAction, theModel);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewModelAction(
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return callModelAction(null, theAction, theModel);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewModelAction(
		final @NotNull BatchUpdateAction<GridResource, ?> postponedActions,
		final @NotNull ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull M theModel) {
		return callModelAction(null, postponedActions, listener, theAction, theModel);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callResourceAction(
			final EntityManager emParam,
			final @NotNull BatchUpdateAction<GridResource, ?> postponedActions,
			final @NotNull ModelUpdateListener<GridResource> listener,
			final @NotNull ModelAction<M, B> theAction,
	        final @NotNull R resource) {
		return txRun(emParam, new Function<EntityManager, B>() {

            public B apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                try {
                    return callModelAction(emParam, postponedActions, listener, theAction, loadModel(em, resource));
                } catch (ResourceException e) {
                    throw new RuntimeException(e);
                }
            }
        });
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callResourceAction(
		final EntityManager emParam,
		final ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
		final @NotNull R resource) {
		return callResourceAction(emParam, new DefaultBatchUpdateAction<GridResource>(), listener,
		                          theAction, resource);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callResourceAction(
		final EntityManager emParam,
		final @NotNull ModelAction<M, B> theAction,
		final @NotNull R resource) {
		return callResourceAction(emParam, getEntityUpdateListener(), theAction, resource);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewResourceAction(
		final @NotNull ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull R resource) {
		return callResourceAction(null, listener, theAction, resource);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewResourceAction(
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull R resource) {
		return callResourceAction(null, theAction, resource);
	}

    /**
     *  @see #callModelAction(EntityManager, de.zib.gndms.logic.model.BatchUpdateAction, de.zib.gndms.logic.model.ModelUpdateListener, de.zib.gndms.logic.model.ModelAction, M)
     */
	public final <B> B callNewResourceAction(
		final @NotNull BatchUpdateAction<GridResource, ?> postponedActions,
		final @NotNull ModelUpdateListener<GridResource> listener,
		final @NotNull ModelAction<M, B> theAction,
	    final @NotNull R resource) {
		return callResourceAction(null, postponedActions, listener, theAction, resource);
	}


	public @NotNull
    ModelUpdateListener<GridResource> getEntityUpdateListener() {
        // TODO: Uh-Oh
	 	return DelegatingModelUpdateListener.getInstance(GNDMSBinding.getInjector().getInstance(GNDMSystem.class));
        // return null;
	}

    protected <B> B txRun(final EntityManager emParam, final @NotNull Function<EntityManager, B> block) {
        if (emParam == null)
            return EMTools.txRun(getEntityManagerFactory().createEntityManager(), true, block);
        else
            return EMTools.txRun(emParam, false, block);
    }

    protected <B> B txResRun(final EntityManager emParam, final @NotNull Function<EntityManager, B> block)
            throws ResourceException {
        if (emParam == null)
            return EMTools.txResRun(getEntityManagerFactory().createEntityManager(), true, block);
        else
            return EMTools.txResRun(emParam, false, block);
    }

	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource a resource
	 * @return model for resource if included in database, null otherwise
	 */
	public final @Nullable M tryLoadModel(final EntityManager emParam, final @NotNull R resource) {
		return txRun(emParam, new Function<EntityManager, M>() {

            public M apply(@com.google.common.base.Nullable EntityManager em) {
                return tryLoadModelById(em, (String) ((ResourceIdentifier)resource).getID());
            }
        });
	}


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param id a resource id
	 * @return model with id id if included in database, null otherwise
	 */
	public final @Nullable M tryLoadModelById(final EntityManager emParam, final @NotNull String id)
	{
            final @NotNull Class<M> curModelClass = getModelClass();
            return txRun(emParam, new Function<EntityManager, M>() {
                public M apply(@com.google.common.base.Nullable @NotNull EntityManager entityManager) {
                    return entityManager.find(curModelClass, id);
                }
            });
     }


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource a resource
	 * @return model for resource
	 * @throws NoSuchResourceException if no model exists
	 */
	public final @NotNull M loadModel(final EntityManager emParam, final @NotNull R resource)
		  throws ResourceException {
		return txRun(emParam, new Function<EntityManager, M>() {

            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                final M model = tryLoadModel(em, resource);
                if (model == null)
                    throw new RuntimeException(new NoSuchResourceException("Could not load model from database"));
                else
                    return model;

            }
        });
	}


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param id a resource id
	 * @return model for resource id
	 * @throws NoSuchResourceException if no model exists
	 */
	public final @NotNull M loadModelById(final EntityManager emParam, final @NotNull String id)
		  throws ResourceException {
		return txRun(emParam, new Function<EntityManager, M>() {

            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                final @Nullable M model = tryLoadModelById(em, id);
                if (model == null)
                    throw new RuntimeException(new NoSuchResourceException("Could not load model from database"));
                else
                    return model;
            }
        });
	}


	public final @NotNull M refreshModel(final EntityManager emParam, final @NotNull M model)
    {
        return txRun(emParam, new Function<EntityManager, M>() {
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                em.refresh(model);
                return model;
            }
        });
    }


	/**
	 * Removes a resource's model from the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource
	 * @throws NoSuchResourceException if no model exists
	 */
	final public @NotNull M removeModel(final EntityManager emParam, final @NotNull R resource) {
        return txRun(emParam, new Function<EntityManager, M>() {
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                try {
                    final @NotNull M model = loadModel(em, resource);
                    em.remove(model);
                    return model;
                } catch (ResourceException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


	/**
	 * Stores a new model in the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param model
	 */
	final public @NotNull M persistModel(final EntityManager emParam, final @NotNull M model)
    {
        return txRun(emParam, new Function<EntityManager, M>() {
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                em.persist(model);
                return model;
            }
        });
    }

	/**
	 * Merges a detached model into the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param model
	 */
	final public @NotNull M mergeModel(final EntityManager emParam, final @NotNull M model)
	{
        return txRun(emParam, new Function<EntityManager, M>() {
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                em.merge(model);
                return model;
            }
        });
	}


	final public @NotNull String getGridName()
		{ return home.getSystem().getGridName(); }

	final public @NotNull String nextUUID()
		{ return home.getSystem().nextUUID(); }


	final public @NotNull EntityManagerFactory getEntityManagerFactory()
		{ return home.getEntityManagerFactory(); }


    @NotNull
    public Class<M> getModelClass() {
        return modelClass;
    }

    @NotNull
    public GNDMServiceHome getHome() {
        return home;
    }

    public void setModelClass(@NotNull Class<M> modelClass) {
        this.modelClass = modelClass;
    }

    public void setHome(@NotNull GNDMServiceHome home) {
        this.home = home;
    }
}


