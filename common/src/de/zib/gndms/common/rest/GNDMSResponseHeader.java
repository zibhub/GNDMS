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

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 26.01.11, Time: 14:25
 *
 * @brief A class for the generic Http response-header specified for GNDMS.
 */
public class GNDMSResponseHeader extends HttpHeaders {

    public final static String MY_PROXY_TOKEN_PREFIX = "myproxytoken-";
    public final static String MY_PROXY_LOGIN_PREFIX = MY_PROXY_TOKEN_PREFIX + "-login-";
    public final static String MY_PROXY_PASSWORD_PREFIX = MY_PROXY_TOKEN_PREFIX + "-passwd-";

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
        setOnce( RESOURCE_URL, resourceURL );
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
        setOnce( FACET_URL, facetURL );
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
        setOnce( PARENT_URL, parentURL );
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
        setOnce( DN, dn );
    }


    private void setOnce( String key, String val ) {
        if (this.containsKey(key)) {
        	this.remove(key);
        }
        this.add( key, val );
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
        setOnce( WID, wid );
    }


    /**
     * Adds a MyProxy access token to the request header.
     *
     * @param purpose A keyword describing what this token is required for, e.g. C3GRID or ESGF...
     * @param login The login string for the MyProxy server.
     * @param passwd A password for the MyProxy server, might be omitted for passwordless access.
     */
    public final void addMyProxyToken( @NotNull String purpose, @NotNull String login, String passwd ) {

        if( login.trim().equals( "" ) )
            throw new IllegalArgumentException( "login must not be empty" );

        StringBuilder key = new StringBuilder( MY_PROXY_LOGIN_PREFIX ).append( purpose );
        setOnce( key.toString(), login );

        if( passwd != null ) {
            key = new StringBuilder( MY_PROXY_PASSWORD_PREFIX ).append( purpose );
            setOnce( key.toString(), passwd );
        }
    }


    public final void addMyProxyToken( @NotNull String purpose, @NotNull MyProxyToken token ) {
        addMyProxyToken( purpose, token.getLogin(), token.getPassword() );
    }
  
    
    public final Map<String, MyProxyToken> getMyProxyToken( ) {

        Map<String, MyProxyToken> result = new HashMap<String, MyProxyToken>( 1 );
        for( String s : keySet() ) {
            if ( s.startsWith( MY_PROXY_LOGIN_PREFIX ) ) {
                String purpose = s.substring( MY_PROXY_LOGIN_PREFIX.length(), s.length() );
                MyProxyToken token = new MyProxyToken( get( s ).get( 0 ) );
                if( containsKey( MY_PROXY_PASSWORD_PREFIX + purpose ) )
                    token.setPassword( get( MY_PROXY_PASSWORD_PREFIX + purpose ).get( 0 ) );

                result.put( purpose, token );
            }
        }
        return Collections.unmodifiableMap( result );
    }
}
