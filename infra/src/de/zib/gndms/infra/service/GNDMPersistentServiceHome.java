package de.zib.gndms.infra.service;

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



import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * ThingAMagic.
 *
 * @see org.globus.wsrf.Resource
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 07.10.2008 Time: 16:20:10
 */
public interface GNDMPersistentServiceHome<M extends GridResource> extends GNDMServiceHome {

    void refresh(final @NotNull M resourceModel) throws ResourceException;

    /**
     * Returns a {@code Query} to find all {@code Resources} of {@code this} 
     *
     * @param em the EntityManager, on which the query will be done
     *
     * @return a {@code Query} to find all {@code Resources} of {@code this}
     */
    @NotNull Query getListAllQuery(final @NotNull EntityManager em);

    @NotNull Class<M> getModelClass();

    ResourceKey getKeyForResourceModel( @NotNull M model );
}
