package de.zib.gndms.infra.model;

import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.zib.gndms.infra.wsrf.ReloadablePersistentResource
import javax.persistence.EntityManagerFactory
import javax.persistence.EntityManager
import org.globus.wsrf.ResourceIdentifier
import de.zib.gndms.infra.service.GNDMServiceHome
import de.zib.gndms.infra.db.EMFactoryProvider
import de.zib.gndms.model.common.GridEntity
import de.zib.gndms.infra.db.EMTools
import de.zib.gndms.model.common.GridResource
import de.zib.gndms.model.common.SingletonGridResource
import javax.persistence.Query
import javax.persistence.NoResultException
import javax.persistence.NonUniqueResultException

/**
 * Helper class for managing persistent models of GNDMS resources 
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:41:17
 */
class GridEntityModelHandler<M extends GridEntity, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> implements EMFactoryProvider {

	// private Log logger = LogFactory.getLog(ModelHandler.class);

	private final @NotNull Class<M> clazz;
	private final @NotNull GNDMServiceHome home;

	GridEntityModelHandler(final @NotNull Class<M> theClazz, final @NotNull H homeParam) {
		clazz = theClazz;
		home = (GNDMServiceHome) homeParam;
	}


	final public @NotNull M createNewGridEntity() {
		return createNewEntity()
	}

	protected def @NotNull createNewEntity()
		{ clazz.newInstance() }


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource a resource
	 * @return model for resource if included in database, null otherwise
	 */
	final @Nullable M tryLoadModel(final EntityManager emParam, final @NotNull R resource) {
		(M) txRun(emParam,
			  { EntityManager em ->
				  tryLoadModelById(em, (String) ((ResourceIdentifier)resource).getID())
			  })
	}


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param id a resource id
	 * @return model with id id if included in database, null otherwise
	 */
	final @Nullable M tryLoadModelById(final EntityManager emParam, final @NotNull String id)
		{ (M) txRun(emParam, { EntityManager em ->	em.find(clazz, id) }) }


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource a resource
	 * @return model for resource
	 * @throws NoSuchResourceException if no model exists
	 */
	final @NotNull M loadModel(final EntityManager emParam, final @NotNull R resource)
		  throws ResourceException {
		(M) txRun(emParam,
			  { EntityManager em ->
					M model = tryLoadModel(em, resource)
					if (model == null)
						throw new NoSuchResourceException("Could not load model from database")
					return model
			  })
	}


	/**
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param id a resource id
	 * @return model for resource id
	 * @throws NoSuchResourceException if no model exists
	 */
	final @NotNull M loadModelById(final EntityManager emParam, final @NotNull String id)
		  throws ResourceException {
		(M) txRun(emParam,
			  { EntityManager em ->
					M model = tryLoadModelById(em, id)
					if (model == null)
						throw new NoSuchResourceException("Could not load model from database")
					model
			  })
	}


	final def refreshModel(final EntityManager emParam, final @NotNull M model)
		{ (M) txRun(emParam) { EntityManager em -> em.refresh(model) } }


	/**
	 * Removes a resource's model from the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param resource
	 * @throws NoSuchResourceException if no model exists
	 */
	final def removeModel(final EntityManager emParam, final @NotNull R resource)
		{ (M) txRun(emParam) { EntityManager em -> em.remove(loadModel(em, resource)) } }


	/**
	 * Stores a new model in the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param model
	 */
	final @NotNull M persistModel(final EntityManager emParam, final @NotNull M model)
		{ (M) txRun(emParam,
			  { EntityManager em ->
				((GridEntity)model).setSystemId(getGridName())
				em.persist(model)
				return model
			  })
		}


	/**
	 * Merges a detached model into the persistent store
	 *
	 * @param emParam the EntityManager to be used or null for an EM from this handler's system
	 * @param model
	 */
	final @NotNull M mergeModel(final EntityManager emParam, final @NotNull M model)
	{ (M) txRun(emParam,
		  { EntityManager em ->
			((GridEntity)model).setSystemId(getGridName())
			em.merge(model)
			return model
		  })
	}


	final @NotNull String getGridName()
		{ home.getSystem().getGridName() }


	final @NotNull String nextUUID()
		{ home.getSystem().nextUUID() }


	final @NotNull EntityManagerFactory getEntityManagerFactory()
		{ home.getEntityManagerFactory() }


	final @NotNull Class<M> getModelClazz()
		{ clazz }
}


/**
 * Specializing ModelHandler for GridResources
 *
 * @see GridResource
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:21:32
 */
class GridResourceModelHandler<M extends GridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridEntityModelHandler<M, H, R> {

	GridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);    // Overridden method
	}

	@Override
	protected def @NotNull createNewEntity() {
		final @NotNull Object model = super.createNewEntity();    // Overridden method
		((GridResource)model).setId(nextUUID());
		return (M) model;
	}

}

/**
 * ModelHandler specializing for SingletonGridResources
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:29:43
 */
final class SingletonGridResourceModelHandler<M extends SingletonGridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridResourceModelHandler<M, H, R> {

	SingletonGridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);
	}

	@NotNull M getSingleModel(final EntityManager emParam,
	                          final @NotNull String queryName, final ModelInitializer<M> creator)
		  throws ResourceException {
		(M) txRun(emParam) { EntityManager em ->
			try {
				final Query query = em.createNamedQuery(queryName)
				query.setParameter("gridName", getGridName())
				return (M) query.getSingleResult()
			}
			catch (NoResultException e) {
				final @NotNull M model = (M) createNewGridEntity()
				if (creator != null)
					creator.initializeModel(model)
				return persistModel(em, (GridEntity)model)
			}
			catch (NonUniqueResultException e)
				{ throw new ResourceException(e); }
		}
	}


	@Override
	protected def @NotNull createNewEntity() {
		final Object model = super.createNewEntity()
		((SingletonGridResource)model).setGridName(getGridName())
		return (M) model
	}
}