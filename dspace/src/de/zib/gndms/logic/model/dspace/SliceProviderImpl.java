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

import de.zib.gndms.model.dspace.Slice;

/**
 * The slice provider which handles the available subspaces providing 
 * a mapping of slice ids and slices.
 * 
 * @author Ulrike Golas
 */

public class SliceProviderImpl implements SliceProvider {

	/**
	 * Map of subspace ids, slice ids and slices.
	 */
    private Map<String, Map<String, Slice>> slices;

	@Override
	public final void init(final SubspaceProvider provider) {		
		for (String sub : provider.listSubspaces()) {
				Set<Slice> allSlices = provider.getSubspace(sub).getSlices();
				Map<String, Slice> map = new HashMap<String, Slice>();
				
				// TODO: is this the right way to get the slice ids?
				for (Slice s : allSlices)  {
					map.put(s.toString(), s);
				}
				slices.put(sub, map);
			}
	}

	@Override
	public final boolean exists(final String subspace, final String slice) {
		try {
			return slices.get(subspace).containsKey(slice);
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public final List<String> listSlices(final String subspace) throws NoSuchElementException {
		try {
	        return new ArrayList<String>(slices.get(subspace).keySet());
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Override
	public final Slice getSlice(final String subspace, final String slice) throws NoSuchElementException {
		try {
			return slices.get(subspace).get(slice);
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

}
