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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.SliceKind;

/**
 * The slice kind provider which handles the available subspaces providing 
 * a mapping of slice kind ids and slice kinds.
 * 
 * @author Ulrike Golas
 */

public class SliceKindProviderImpl implements SliceKindProvider {

	/**
	 * Map of subspace ids, slice kind ids and slice kinds.
	 */
    private Map<String, Map<String, SliceKind>> sliceKinds;

	@Override
	public final void init(final SubspaceProvider provider) {
		for (String sub : provider.listSubspaces()) {
				Set<SliceKind> subSliceKinds = provider.getSubspace(sub).getCreatableSliceKinds();
				Map<String, SliceKind> map = new HashMap<String, SliceKind>();
				
				// TODO: is this the right way to get the slice kind ids?
				for (SliceKind sk : subSliceKinds)  {
					map.put(sk.toString(), sk);
				}
				sliceKinds.put(sub, map);
			}
	}

	@Override
	public final boolean exists(final String subspace, final String sliceKind) {
		try {
			return sliceKinds.get(subspace).containsKey(sliceKind);
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public final List<String> listSliceKindIds(final String subspace) throws NoSuchElementException {
		try {
	        return new ArrayList<String>(sliceKinds.get(subspace).keySet());
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Override
	public final SliceKind getSliceKind(final String subspace, final String sliceKind) throws NoSuchElementException {
		try {
			return sliceKinds.get(subspace).get(sliceKind);
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

}
