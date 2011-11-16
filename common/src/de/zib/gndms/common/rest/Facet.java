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

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 26.01.11, Time: 12:29
 *
 * @brief Class representing a facet of a rest resource.
 */
public class Facet {

    /**
     * The short name of the facet.
     */
	private String name; 
	/**
	 * The complete url.
	 */
    private String url; 

    /**
     * The constructor.
     */
    public Facet() {
    }

    /**
     * The constructor, setting name and url of the facet.
     * @param name The name.
     * @param url The url.
     */
    public Facet( final String name, final String url ) {
        this.name = name;
        this.url = url;
    }

    /**
     * Returns the name of the facet.
     * @return The name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the facet.
     * @param name The name.
     */
    public final void setName( final String name ) {
        this.name = name;
    }

    /**
     * Returns the url of the facet.
     * @return The url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Sets the url of the facet.
     * @param url The url.
     */
    public final void setUrl( final String url ) {
        this.url = url;
    }
}
