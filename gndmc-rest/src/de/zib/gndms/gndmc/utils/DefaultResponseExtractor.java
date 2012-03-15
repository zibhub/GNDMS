/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.gndmc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @date: 14.03.12
 * @time: 13:42
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DefaultResponseExtractor implements EnhancedResponseExtractor {
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private int statusCode;
    private HttpHeaders headers;
    private InputStream body;
    private String url;

    @Override
    public void extractData( final String url, final ClientHttpResponse response ) throws IOException {
        statusCode = response.getStatusCode().value();
        headers = response.getHeaders();
        body = response.getBody();
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }

    public static List< String > getCookies( final HttpHeaders headers ) {
        final List< String > cookies = new LinkedList<String>();

        for( String header: headers.keySet() ) {
            if( "Set-Cookie".equals( header ) ) {
                for( String value: headers.get( header ) ) {
                    cookies.add( value );
                }
            }
        }

        return cookies;
    }
    
    public static String getLocation( final HttpHeaders headers ) {
        for( String header: headers.keySet() ) {
            if( "Location".equals( header) ) {
                return headers.get( header ).get( 0 );
            }
        }

        return null;
    }

    public String getURL() {
        return url;
    }
}
