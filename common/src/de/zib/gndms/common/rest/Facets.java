package de.zib.gndms.common.rest;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 26.01.11, Time: 14:15
 *
 * @brief Class for a list of facets.
 */
public class Facets {

	/**
	 * The list of all facets.
	 */
    private List<Facet> facets; 

    /**
     * The constructor.
     */
    public Facets() {
    }

    /**
     * The constructor setting the given facets.
     * @param facets The list of  facets.
     */
    public Facets( final List<Facet> facets ) {
        this.facets = facets;
    }

    /**
     * Returns all facets.
     * @return The list of facets.
     */
    public final List<Facet> getFacets() {
        return facets;
    }

    /**
     * Sets all facets.
     * @param facets The list of facets.
     */
    public final void setFacets( final List<Facet> facets ) {
        this.facets = facets;
    }

    /**
     * Searches for the facet of a given facet name.
     *
     *
     * @param fn The facets name
     * @return The url of the facet or null.
     *
     * Note: this method is expensive on a large set of facets.
     */
    public final Facet findFacet( final String fn ) {

        for ( Facet f : facets ) {
            if ( f.getName().equals( fn ) ) {
                return f;       
            }
        }
        return null;
    }
}
