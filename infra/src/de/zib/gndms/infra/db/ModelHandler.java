package de.zib.gndms.infra.db;

import org.globus.wsrf.PersistentResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:41:17
 */
@SuppressWarnings({"FeatureEnvy"})
public class ModelHandler<M> {
	@NotNull
	private final Class<M> clazz;

	public ModelHandler(@NotNull final Class<M> theClazz) {
		clazz = theClazz;
	}

	@Nullable
	public M tryLoadModel(@NotNull EntityManagerGuard emg, @NotNull PersistentResource r) {
		boolean flag = emg.begin();
		try {
			return emg.getEM().find(clazz, r.getID());
		}
		catch (RuntimeException e) { emg.rollback(flag, e); /* unreachable */ return null; }			
		finally {
			emg.commitAndClose(flag);
		}
	}

	@NotNull
	public M loadModel(@NotNull EntityManagerGuard emg, @NotNull PersistentResource r)
		  throws ResourceException {
		M model = tryLoadModel(emg, r);
		if (model == null)
			throw new ResourceException("Could not load model from database");
		return model;
	}

	public void refreshModel(@NotNull EntityManagerGuard emg, @NotNull M m) {
		boolean flag = emg.begin();
		try {
			emg.getEM().refresh(m);
		}
		catch (RuntimeException e) { emg.rollback(flag, e); /* unreachable */ }
		finally {
			emg.commitAndClose(flag);
		}
	}
	public void removeModel(@NotNull EntityManagerGuard emg, @NotNull PersistentResource r) {
		boolean flag = emg.begin();
		try {
			final M model = emg.getEM().find(clazz, r.getID());
			if (model != null)
				emg.getEM().remove(model);
		}
		catch (RuntimeException e) { emg.rollback(flag, e); /* unreachable */ }
		finally {
			emg.commitAndClose(flag);
		}
	}

	public void storeModel(@NotNull EntityManagerGuard emg, @NotNull M model) {
		boolean flag = emg.begin();
		try {
			emg.getEM().persist(model);
		}
		catch (RuntimeException e) { emg.rollback(flag, e); /* unreachable */ }
		finally {
			emg.commitAndClose(flag);
		}
	}

	@NotNull
	public Class<M> getClazz() {
		return clazz;
	}
}
