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

import de.zib.gndms.model.dspace.Subspace;

import java.util.List;

/**
 * Provides a mapping of subspace ids and subspaces.
 * 
 * @author Ulrike Golas, Jörg Bachmann
 */

public interface SubspaceProvider {
    
	/**
	 * Checks whether a given subspace id exists.
	 * @param subspace The subspace id.
	 * @return true, if this subspace exists, otherwise false.
	 */
	boolean exists( String subspace );

	/**
	 * Returns a list containing all existing subspace ids.
	 * @return The list.
	 */
	List< Subspace > list();

    /**
     * Returns the subspace for a given subspace id.
     * @param subspace The requested subspace id.
     * @return The corresponding subspace.
     */
	Subspace get( String subspace );

    void create(String subspace, String config);
}
