package de.zib.gndms.infra.db;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.PersistentResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:41:17
 */
@SuppressWarnings({"FeatureEnvy"})
public class ModelHandler<M> {
	// private Log logger = LogFactory.getLog(ModelHandler.class);

	@NotNull
	private final Class<M> clazz;

	@NotNull
	private final SystemHolder holder;

	public ModelHandler(@NotNull final Class<M> theClazz, @NotNull final SystemHolder theHolder) {
		clazz = theClazz;
		holder = theHolder;
	}

	@SuppressWarnings({"unchecked"})
	@NotNull
	public M getSingleModel(@NotNull String queryName, ModelCreator<M> creator)
		  throws ResourceException {
		final GNDMSystem sys = holder.getSystem();
		final String gridName = sys.getGridName();
		final EntityManagerGuard emg = sys.currentEMG();

		final boolean flag = emg.begin();
		try {
			final Query query = emg.getEM().createNamedQuery(queryName);
			query.setParameter("gridName", gridName);
			return (M) query.getSingleResult();
		}
		catch (NoResultException e) {
			if (creator == null)
				throw emg.rollback(new InvalidResourceKeyException());
			else 
				return storeModel(creator.createInitialModel(sys.nextUUID(), sys.getGridName()));
		}
		catch (NonUniqueResultException e)
			{ throw emg.rollback(new ResourceException(e)); }
		finally
			{ emg.commitAndClose(flag);	}
	}


	@Nullable
	public M tryLoadModel(@NotNull PersistentResource r) {
		return tryLoadModelById((String) r.getID());
	}

	@Nullable
	public M tryLoadModelById(@NotNull String id) {
		final EntityManagerGuard emg = holder.currentEMG();
		boolean flag = emg.begin();
		try {
			return emg.getEM().find(clazz, id);
		}
		catch (RuntimeException e) { throw emg.rollback(e); }
		finally {
			emg.commitAndClose(flag);
		}
	}

	@NotNull
	public M loadModel(@NotNull PersistentResource r)
		  throws ResourceException {
		M model = tryLoadModel(r);
		if (model == null)
			throw new ResourceException("Could not load model from database");
		return model;
	}

	@NotNull
	public M loadModelById(@NotNull String id)
		  throws ResourceException {
		M model = tryLoadModelById(id);
		if (model == null)
			throw new ResourceException("Could not load model from database");
		return model;
	}


	public void refreshModel(@NotNull M m) {
		final EntityManagerGuard emg = holder.currentEMG();
		boolean flag = emg.begin();
		try {
			emg.getEM().refresh(m);
		}
		catch (RuntimeException e) { throw emg.rollback(e); }
		finally { emg.commitAndClose(flag); 	}
	}

	
	public void removeModel(@NotNull PersistentResource r) {
		final EntityManagerGuard emg = holder.currentEMG();
		boolean flag = emg.begin();
		try {
			final M model = emg.getEM().find(clazz, r.getID());
			if (model != null)
				emg.getEM().remove(model);
		}
		catch (RuntimeException e) { throw emg.rollback(e); }
		finally { emg.commitAndClose(flag); }
	}

	@NotNull
	public M storeModel(@NotNull M model) {
		final EntityManagerGuard emg = holder.currentEMG();
		boolean flag = emg.begin();
		try {
			emg.getEM().persist(model);
		}
		catch (RuntimeException e) { throw emg.rollback(e); }
		finally { emg.commitAndClose(flag);	}
		return model;
	}

	@NotNull
	public Class<M> getClazz() {
		return clazz;
	}
}
