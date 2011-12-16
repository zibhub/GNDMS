/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.GridResourceCache;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SubspaceProviderImpl extends GridResourceDAO< Subspace > implements SubspaceProvider {

	public SubspaceProviderImpl( final EntityManagerFactory emf ) {
        super(
                emf,
                new GridResourceCache< Subspace >(
                        Subspace.class,
                        emf
                ),
                SetupSubspaceAction.class
        );
    }

    protected String getListQuery( ) {
        return "listAllSubspaceIds";
    }

    @Override
    public boolean exists( String subspace ) {
        return super.exists( subspace );
    }

    @Override
    public List<Subspace> list() {
        return null;
    }
}
