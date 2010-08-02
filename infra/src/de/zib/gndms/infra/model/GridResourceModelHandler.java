package de.zib.gndms.infra.model;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

/**
 * Specializing ModelHandler for GridResources
 *
 * @see de.zib.gndms.model.common.GridResource
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:21:32
 */
public class GridResourceModelHandler<M extends GridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridEntityModelHandler<M, H, R> {

	public GridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);    // Overridden method
	}

	@Override
	protected @NotNull M createNewEntity() {
		final @NotNull M model = super.createNewEntity();    // Overridden method
		model.setId(nextUUID());
		return model;
	}

}
