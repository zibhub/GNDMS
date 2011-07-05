package de.zib.gndms.common.rest;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 02.03.11  17:17
 * @brief Specifier for a REST resource.
 *
 * A specifier describes a resource in different ways. It may contain a global url to the resource together with more
 * abstract id attributes.
 */
public class Specifier<T> {

    private String URL; ///< The whole url of the resource.
    private Map<String, String> uriMap; ///< An id map for the resource. Useful to fill url templates.
    private T payload; ///< Some additional data for the resource. Usually the results of a GET request.
    private Specifier specifier; ///< Specifiers might be nested.


    public String getURL() {
        return URL;
    }


    public void setURL( String URL ) {
        this.URL = URL;
    }


    public Map<String, String> getUriMap() {
        return uriMap;
    }


    public void setUriMap( Map<String, String> urlMap ) {
        this.uriMap = urlMap;
    }


    public void addMapping( String key, String value ) {

        if ( uriMap == null )
            uriMap = new HashMap<String, String>( 1 );

        uriMap.put( key, value );
    }


    public boolean hasPayload() {
        return payload != null;
    }


    public T getPayload() {
        return payload;
    }


    public void setPayload( T payload ) {
        this.payload = payload;
    }


    public Specifier getSpecifier() {
        return specifier;
    }


    public void setSpecifier( Specifier specifier ) {
        this.specifier = specifier;
    }
}
