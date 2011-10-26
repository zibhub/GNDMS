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

import java.util.List;

import de.zib.gndms.model.dspace.SliceKind;

/**
 * Provides a mapping of slice kind ids and slice kinds.
 * 
 * @author Ulrike Golas
 */

// TODO implementation

public interface SliceKindProvider {

	/**
	 * Checks whether a given slice kind id exists.
	 * @param sliceKind The slice kind id.
	 * @return true, if this slice kind exists, otherwise false.
	 */
	boolean exists(String sliceKind);
	
	/**
	 * Returns a list containing all existing slice kind ids.
	 * @return The list.
	 */
    List<String> listSliceKinds();
    
    /**
     * Returns the slice kind for a given slice kind id.
     * @param sliceKind The requested slice kind id.
     * @return The corresponding slice kind.
     */
    SliceKind getSliceKind(String sliceKind);

}
