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

import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 26.01.11, Time: 14:25
 *
 * @brief A class for the generic Http response-header specified for GNDMS.
 */
// TODO: Does that really make sense, that all the fields may occur multiple times (since this is a list ...)?

public class GNDMSResponseHeader extends HttpHeaders {

	/**
	 * The key for a resource url.
	 */
    public static final String RESOURCE_URL = "resourceURL";
	/**
	 * The key for a facet url.
	 */
    public static final String FACET_URL = "facetURL";
	/**
	 * The key for a parent url.
	 */
    public static final String PARENT_URL = "parentURL";
	/**
	 * The key for a dn.
	 */
    public static final String DN = "DN";
	/**
	 * The key for a wid.
	 */
    public static final String WID = "WId";

    
    /**
	 * The constructor.
	 */
    public GNDMSResponseHeader() {
    }

    /**
     * The constructor setting the given fields.
     * @param resourceURL The resource url.
     * @param facetURL The facet url.
     * @param parentURL The parent url.
     * @param dn The dn.
     * @param wid The wid.
     */
    public GNDMSResponseHeader( final String resourceURL, final String facetURL, 
    		final String parentURL, final String dn, final String wid ) {
         if ( resourceURL != null ) {
             setResourceURL( resourceURL );
         }
         if ( facetURL != null ) {
             setFacetURL( facetURL );
         }
         if ( parentURL != null ) {
             setParentURL( parentURL );
         }
         if ( dn != null ) {
             setDN( dn );
         }
         if ( wid != null ) {
             setWId( wid );
         }
    }

    /**
     * The constructor, setting the given http headers.
     * @param h The http headers.
     */
    public GNDMSResponseHeader( final HttpHeaders h ) {
        putAll( h );
    }

    /**
     * Returns the resource url.
     * @return A list containing the resource url.
     */
    public final List<String> getResourceURL() {
        return get(RESOURCE_URL);
    }

    /**
     * Sets a unique resource url.
     * @param resourceURL The resource url.
     */
    public final void setResourceURL( final String resourceURL ) {
        if (this.containsKey(RESOURCE_URL)) {
        	this.remove(RESOURCE_URL);
        }
    	this.add(RESOURCE_URL, resourceURL );
    }

    /**
     * Returns the facet url.
     * @return A list containing the facet url.
     */
    public final List<String> getFacetURL() {
        return get( FACET_URL );
    }

    /**
     * Sets a unique facet url.
     * @param facetURL The facet url.
     */
    public final void setFacetURL( final String facetURL ) {
        if (this.containsKey(FACET_URL)) {
        	this.remove(FACET_URL);
        }
        this.add( FACET_URL, facetURL );
    }

    /**
     * Returns the parent url.
     * @return A list containing the parent url.
     */
    public final List<String> getParentURL() {
        return get( PARENT_URL );
    }

    /**
     * Adds a unique parent url.
     * @param parentURL The parent url.
     */
    public final void setParentURL( final String parentURL ) {
        if (this.containsKey(PARENT_URL)) {
        	this.remove(PARENT_URL);
        }
        this.add( PARENT_URL, parentURL );
    }

    /**
     * Returns the dn.
     * @return A list containing the dn.
     */
    public final List<String> getDN() {
        return get( DN );
    }

    /**
     * Adds a unique dn.
     * @param dn The dn.
     */
    public final void setDN( final String dn ) {
        if (this.containsKey(DN)) {
        	this.remove(DN);
        }
        this.add( DN, dn );
    }

    /**
     * Returns the wid.
     * @return A list containing the wid.
     */
    public final List<String> getWId() {
        return get( WID );
    }

    /**
     * Adds a unique wid.
     * @param wid The wid.
     */
    public final void setWId( final String wid ) {
        if (this.containsKey(WID)) {
        	this.remove(WID);
        }
        this.add( WID, wid );
    }
}
