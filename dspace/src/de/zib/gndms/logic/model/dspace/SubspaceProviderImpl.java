package de.zib.gndms.logic.model.dspace;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.zib.gndms.model.dspace.Subspace;

/**
 * The subspace provider which handles the available subspaces providing 
 * a mapping of subspace ids and subspaces.
 * 
 * @author Ulrike Golas
 */
public class SubspaceProviderImpl implements SubspaceProvider {

	/**
	 * Map of subspace ids and subspaces.
	 */
    private Map<String, Subspace> subspaces;
	
	@Override
	public final boolean exists(final String subspace) {
        return subspaces.containsKey(subspace);
	}

	@Override
	public final List<String> listSubspaces() {
        return new ArrayList<String>(subspaces.keySet());
	}

	@Override
	public final Subspace getSubspace(final String subspace) {
		return subspaces.get(subspace);
	}

	/**
	 * Returns the subspace map.
	 * @return the subspaces
	 */
	public final Map<String, Subspace> getSubspaces() {
		return subspaces;
	}

	/**
	 * Sets the subspace map.
	 * @param subspaces the subspaces to set
	 */
	public final void setSubspaces(final Map<String, Subspace> subspaces) {
		this.subspaces = subspaces;
	}

}
