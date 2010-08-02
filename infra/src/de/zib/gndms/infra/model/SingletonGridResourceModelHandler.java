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



import com.google.common.base.Function;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.SingletonGridResource;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 * ModelHandler specializing for SingletonGridResources
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:29:43
 */
public final class SingletonGridResourceModelHandler<M extends SingletonGridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridResourceModelHandler<M, H, R> {

	public SingletonGridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);
	}

	public @NotNull  M getSingleModel(final EntityManager emParam,
	                                  final @NotNull String queryName, final ModelInitializer<M> creator)
		  throws ResourceException {
		return txRun(emParam, new Function<EntityManager, M>() {
            public M apply(@com.google.common.base.Nullable @NotNull EntityManager em) {
                try {
                    final Query query = em.createNamedQuery(queryName);
                    // TODO: Test
                    return (M) query.getSingleResult();
                }
                catch (NoResultException e) {
                    final @NotNull M model = createNewGridEntity();
                    if (creator != null)
                        creator.initializeModel(model);
                    return persistModel(em, model);
                }
                catch (NonUniqueResultException e)
                    { throw new RuntimeException(e); }
            }
        });
	}
}
