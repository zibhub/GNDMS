package de.zib.gndms.infra.model;

import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.SingletonGridResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query
import de.zib.gndms.model.common.GridEntity;


/**
 * ModelHandler specializing for SingletonGridResources
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:29:43
 */
class SingletonGridResourceModelHandler<M extends SingletonGridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends AbstractSingletonGridResourceModelHandler<M, H, R> {

	def public SingletonGridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);
	}

	def @NotNull M getSingleModel(final EntityManager emParam,
	                              final @NotNull String queryName, final ModelInitializer<M> creator)
		  throws ResourceException {
		(M) txRun(emParam) { EntityManager em ->
			try {
				final Query query = em.createNamedQuery(queryName)
				query.setParameter("gridName", getGridName())
				return (M) query.getSingleResult()
			}
			catch (NoResultException e) {
				final @NotNull M model = createNewEntity()
				if (creator != null)
					creator.initializeModel(model)
				return persistModel(em, (GridEntity)model)
			}
			catch (NonUniqueResultException e)
				{ throw new ResourceException(e); }
		}
	}

	@NotNull
	@Override
	def protected M createNewEntity() {
		final M model = (M) super.createNewEntity();
		((SingletonGridResource)model).setGridName(getGridName());
		return model;    // Overridden method
	}
}
