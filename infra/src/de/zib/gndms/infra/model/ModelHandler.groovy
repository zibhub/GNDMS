package de.zib.gndms.infra.model;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource
import javax.persistence.EntityManagerFactory
import javax.persistence.EntityManager
import org.globus.wsrf.ResourceIdentifier
import de.zib.gndms.infra.service.GNDMServiceHome
import de.zib.gndms.infra.db.EMFactoryProvider
import de.zib.gndms.infra.db.EMTools

/**
 * Helper class for managing persistent models of GNDMS resources 
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:41:17
 */
public class ModelHandler<M, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> implements EMFactoryProvider {

	// private Log logger = LogFactory.getLog(ModelHandler.class);

	private final @NotNull Class<M> clazz;
	private final @NotNull GNDMServiceHome home;

	def ModelHandler(final @NotNull Class<M> theClazz, final @NotNull H homeParam) {
		clazz = theClazz;
		home = (GNDMServiceHome) homeParam;
	}


	def @NotNull M getSingleModel(@NotNull String queryName, ModelCreator<M> creator)
		  throws ResourceException {
		(M) txRun { EntityManager em ->
			try {
				final Query query = em.createNamedQuery(queryName)
				query.setParameter("gridName", getGridName())
				M model = (M) query.getSingleResult()
				return model
			}
			catch (NoResultException e) {
				if (creator == null)
					throw new InvalidResourceKeyException()
				else
					persistModel(em, creator.createInitialModel(nextUUID(), getGridName()))
			}
			catch (NonUniqueResultException e)
				{ throw new ResourceException(e); }
		}
	}


	/**
	 * @param resource a resource
	 * @return model for resource if included in database, null otherwise
	 */
	def @Nullable M tryLoadModel(@NotNull R resource)
		{ (M) txRun { EntityManager em -> tryLoadModel(em, resource) } }

	def @Nullable M tryLoadModel(@NotNull EntityManager em, @NotNull R resource)
		{ tryLoadModelById(em, (String) ((ResourceIdentifier)resource).getID()) }


	/**
	* @param id a resource id
	* @return model with id id if included in database, null otherwise
	*/
	def @Nullable M tryLoadModelById(@NotNull String id)
		{ (M) txRun { EntityManager em -> tryLoadModelById(id) } }

	def @Nullable M tryLoadModelById(@NotNull EntityManager em, @NotNull String id)
		{ em.find(clazz, id) }


	/**
	 * @param resource a resource
	 * @return model for resource
	 * @throws NoSuchResourceException if no model exists
	 */
	def @NotNull M loadModel(@NotNull R resource)
		{ (M) txRun { EntityManager em -> loadModel(em, resource) } }

	def @NotNull M loadModel(@NotNull EntityManager em, @NotNull R resource)
		  throws ResourceException {
		M model = tryLoadModel(em, resource)
		if (model == null)
			throw new NoSuchResourceException("Could not load model from database");
		return model
	}


	/**
	 * @param id a resource id
	 * @return model for resource id
	 * @throws NoSuchResourceException if no model exists
	 */
	def @NotNull M loadModelById(@NotNull String id)
		{ (M) txRun { EntityManager em -> loadModelById(id) } }

	def @NotNull M loadModelById(@NotNull EntityManager em, @NotNull String id)
		  throws ResourceException {
		M model = tryLoadModelById(em, id)
		if (model == null)
			throw new NoSuchResourceException("Could not load model from database")
		model
	}


	def void refreshModel(@NotNull M model)
		{ txRun { EntityManager em -> refreshModel(em, model) } }

	def void refreshModel(@NotNull EntityManager em, @NotNull M m)
		{ em.refresh(m)	}


	/**
	 * Removes a resource's model from the persistent store
	 * @param resource
	 * @throws NoSuchResourceException if no model exists
	 */
	def void removeModel(@NotNull R resource)
		{ txRun { EntityManager em -> removeModel(em, resource) } }

	def void removeModel(@NotNull EntityManager em, @NotNull R resource)
		{ em.remove(loadModel(em, resource)) }


	/**
	 * Stores a new model in the persistent store
	 * @param model
	 */
	def @NotNull M persistModel(@NotNull M model)
		{ (M) txRun { EntityManager em -> persistModel(em, model) } }

	def @NotNull M persistModel(@NotNull EntityManager em, @NotNull M model) {
		em.persist(model)
		return model
	}


	/**
	 * Merges a detached model into the persistent store
	 * @param model
	 */
	def @NotNull M mergeModel(@NotNull M model)
		{ (M) txRun { EntityManager em -> persistModel(em, model) } }

	def @NotNull M mergeModel(@NotNull EntityManager em, @NotNull M model) {
		em.merge(model)
		return model
	}


	def @NotNull String getGridName()
		{ home.getSystem().getGridName() }


	def @NotNull String nextUUID()
		{ home.getSystem().nextUUID() }


	def @NotNull EntityManagerFactory getEntityManagerFactory()
		{ home.getEntityManagerFactory() }


	def @NotNull Class<M> getModelClazz()
		{ clazz }
}
