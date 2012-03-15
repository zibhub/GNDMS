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

import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @date: 14.03.12
 * @time: 13:19
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class RedirectResponseExtractor extends DefaultResponseExtractor implements EnhancedResponseExtractor {

    private List< String > cookie = null;
    private String location = null;

    @Override
    public void extractData( final String url, final ClientHttpResponse response ) throws IOException {

        super.extractData( url, response );

        for( String header: getHeaders().keySet() ) {
            if( "Set-Cookie".equals( header ) ) {
                cookie = new LinkedList< String >();

                for( String value: getHeaders().get( header ) ) {
                    cookie.add( value );
                }
            }
            else if( "Location".equals( header ) ) {
                location = URLDecoder.decode(getHeaders().get(header).get(0));
            }
        }
    }
    
    public List< String > getCookie() {
        return cookie;
    }

    public String getLocation() {
        return location;
    }
}
