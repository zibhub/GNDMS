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
 * * A specifier describes a resource in different ways. It may contain a global url to the resource together with more
 * abstract id attributes.
 * 
 * @author try ma ik jo rr a zib
 * @param <T> The result type of the specifier.
 * @date 02.03.11  17:17
 * @brief Specifier for a REST resource.
 */
public class Specifier<T> {

	/**
	 * The complete url of the resource.
	 */
	private String url;
	/**
	 * An id map for the resource, used to fill url templates.
	 */
    private Map<String, String> uriMap;
    /**
     * Some additional data for the resource, which is usually the result of a GET request.
     */
    private T payload;
    /**
     * Nesting of specifiers.
     */
    @SuppressWarnings("rawtypes")
	private Specifier specifier;

    /**
     * Returns the url of the specifier.
     * @return The url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Sets the url of a specifier.
     * @param url The url.
     */
    public final void setUrl( final String url ) {
        this.url = url;
    }

    /**
     * Returns the id map.
     * @return The map.
     */
    public final Map<String, String> getUriMap() {
        return uriMap;
    }

    /**
     * Sets the id map.
     * @param uriMap The map.
     */
    public final void setUriMap( final Map<String, String> uriMap ) {
        this.uriMap = uriMap;
    }

    /**
     * Adds a key-value pair to the id map.
     * @param key The key.
     * @param value The value.
     */
    public final void addMapping( final String key, final String value ) {
        if ( uriMap == null ) {
            uriMap = new HashMap<String, String>( 1 );
        }
        uriMap.put( key, value );
    }

    /**
     * Checks, if the specifier has a payload.
     * @return true, if the payload is not null.
     */
    public final boolean hasPayload() {
        return payload != null;
    }

    /**
     * Returns the payload of the specifier.
     * @return The payload.
     */
    public final T getPayload() {
        return payload;
    }

    /**
     * Sets the payload of the specifier.
     * @param payload The payload.
     */
    public final void setPayload( final T payload ) {
        this.payload = payload;
    }

    /**
     * Returns the nested specifier of this specifier.
     * @return The nested specifier.
     */
    @SuppressWarnings("rawtypes")
	public final Specifier getSpecifier() {
        return specifier;
    }

    /**
     * Sets the nested specifier of this specifier.
     * @param specifier The snested specifier.
     */
    @SuppressWarnings("rawtypes")
	public final void setSpecifier( final Specifier specifier ) {
        this.specifier = specifier;
    }
}
