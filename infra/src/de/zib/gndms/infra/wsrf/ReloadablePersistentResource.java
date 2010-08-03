package de.zib.gndms.infra.wsrf;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.infra.service.GNDMServiceHome;
import org.globus.wsrf.PersistentResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;


/**
 * Additional methods required by GNDMS PersistentResources
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 11:07:52
 */
public interface ReloadablePersistentResource<M, H extends GNDMServiceHome>
	  extends PersistentResource {
	/**
	 * Fetch model from database by id without touching the resource object state
	 *
	 * @param id
	 * @return
	 * @throws ResourceException
	 */
	@NotNull
	M loadModelById(@NotNull String id) throws ResourceException;

	/**
	 * Reset this resource's state from the model with id id
	 *
	 * @param id
	 * @throws ResourceException esp. if id != getID()
	 */
	void loadViaModelId(@NotNull String id) throws ResourceException;

	/**
	 * Reset this resource's state from the model
	 *
	 * @param model
	 * @throws ResourceException esp. if getID() != model.getId()
	 */
	void loadFromModel(@NotNull M model) throws ResourceException;


	/**
	 *
	 * @return the resource home
	 */
	@NotNull H getResourceHome();


	/**
	 * The resourceHome setter.  Must be called exactly once before initialize.
	 *
	 * @param resourceHomeParam resource home to be set
	 */
	void setResourceHome(@NotNull H resourceHomeParam);


	/**
	 *
	 * @return Subclasses return their uuid string as ID
	 */
	String getID();
}
