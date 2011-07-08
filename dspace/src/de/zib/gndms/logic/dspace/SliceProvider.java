package de.zib.gndms.logic.dspace;

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

import java.util.List;

import de.zib.gndms.model.dspace.Slice;

/**
 * Provides a mapping of slice ids and slices.
 * 
 * @author Ulrike Golas
 */

// TODO implementation

public interface SliceProvider {

	/**
	 * Checks whether a given slice id exists.
	 * @param slice The slice id.
	 * @return true, if this slice exists, otherwise false.
	 */
	boolean exists(String slice);
	
	/**
	 * Returns a list containing all existing slice ids.
	 * @return The list.
	 */
    List<String> listSlices();

    /**
     * Returns the slice for a given slice id.
     * @param slice The requested slice id.
     * @return The corresponding slice.
     */
    Slice getSlice(String slice);

}
