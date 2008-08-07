package de.zib.gndms.infra.wsrf;

import org.globus.wsrf.PersistentResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

/**
 * Additional methods required by GNDMS PersistentResources
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 11:07:52
 */
public interface ReloadablePersistentResource<M> extends PersistentResource {
	/**
	 * Fetch model from database by id without touching the resource object state
	 *
	 * @param id
	 * @return
	 * @throws ResourceException
	 */
	@NotNull
	M loadModel(@NotNull String id) throws ResourceException;

	/**
	 * Reset this resource's state from the model with id id
	 *
	 * @param id
	 * @throws ResourceException esp. if id != getID()
	 */
	void loadById(@NotNull String id) throws ResourceException;

	/**
	 * Reset this resource's state from the model
	 *
	 * @param model
	 * @throws ResourceException esp. if getID() != model.getId()
	 */
	void loadFromModel(@NotNull M model) throws ResourceException;
}
