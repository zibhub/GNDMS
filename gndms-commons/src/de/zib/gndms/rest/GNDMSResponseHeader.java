package de.zib.gndms.rest;
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
public class GNDMSResponseHeader extends HttpHeaders {

    public GNDMSResponseHeader() {
    }

    public GNDMSResponseHeader( String resourceURL, String facetURL, String parentURL, String dn, String wid ) {
         if( resourceURL != null )
             setResourceURL( resourceURL );
         if( facetURL != null )
             setFacetURL( facetURL );
         if( parentURL != null )
             setParentURL( parentURL );
         if( dn != null )
             setDN( dn );
         if( wid != null )
             setWId( wid );

    }

    public GNDMSResponseHeader( HttpHeaders h ) {
        putAll( h );
    }


    public List<String> getResourceURL() {
        return get( "resourceURL" );
    }


    public void setResourceURL( String resourceURL ) {
        this.add("resourceURL", resourceURL );
    }


    public List<String> getFacetURL() {
        return get( "facetURL" );
    }


    public void setFacetURL( String facetURL ) {
        this.add( "facetURL", facetURL );
    }


    public List<String> getParentURL() {
        return get( "parentURL" );
    }


    public void setParentURL( String parentURL ) {
        this.add( "parentURL", parentURL );
    }


    public List<String> getDN() {
        return get( "DN" );
    }


    public void setDN( String DN ) {
        this.add( "DN", DN );
    }


    public List<String> getWId() {

        return get( "WId" );
    }


    public void setWId( String wid ) {
        this.add( "WId", wid );
    }
}
