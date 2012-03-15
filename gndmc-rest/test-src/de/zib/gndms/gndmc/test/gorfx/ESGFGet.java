package de.zib.gndms.gndmc.test.gorfx;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.kit.application.AbstractApplication;
import de.zib.gndms.gndmc.utils.HTTPGetter;
import org.kohsuke.args4j.Option;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 13.03.12  15:51
 * @brief
 */
public class ESGFGet extends AbstractApplication {

    @Option( name="-passwd", required = true )
    protected String passwd;
    @Option( name="-url", required = true )
    protected String url;
    @Option( name="-out", required = true )
    protected String off;
    @Option( name="-cred", required = true )
    protected String keyStoreLocation;
    @Option( name="-trust", required = true )
    protected String trustStoreLocation;


    public static void main( String[] args ) throws Exception {

        ESGFGet cnt = new ESGFGet();
        cnt.run(args);
        System.exit(0);
        
    }


    @Override
    protected void run() throws Exception {

        HTTPGetter getter = new HTTPGetter();

        getter.setKeyStoreLocation( keyStoreLocation );
        getter.setTrustStoreLocation( trustStoreLocation );
        getter.setPassword( passwd );
        getter.setupSSL();

        MyResponseExtractor responseExtractor = get( url, null );
        int statusCode = responseExtractor.getStatusCode();

        while( 302 == statusCode) {
            final List< String > cookie = responseExtractor.getCookie();
            final String location = responseExtractor.getLocation();

            responseExtractor = get( location, new RequestCallback() {
                @Override
                public void doWithRequest( ClientHttpRequest request ) throws IOException {
                    for( String c: cookie )
                        request.getHeaders().add("Cookie", c.split(";", 2)[0]);
                }
            });

            statusCode = responseExtractor.getStatusCode();
        }
    }
    
    
    MyResponseExtractor get( String url, RequestCallback requestCallback ) {
        MyResponseExtractor responseExtractor = new MyResponseExtractor();

        RestTemplate rt = new RestTemplate();
        rt.execute(url, HttpMethod.GET, requestCallback, responseExtractor);

        return responseExtractor;
    }
    
    
    class MyResponseExtractor implements ResponseExtractor< Object > {
        
        private int statusCode;
        private List< String > cookie = null;
        private String location = null;
        private HttpHeaders headers;
        private InputStream inputStream;

        @Override
        public Object extractData( final ClientHttpResponse response ) throws IOException {
            
            statusCode = response.getStatusCode().value();
            headers = response.getHeaders();
            
            for( String header: headers.keySet() ) {
                if( "Set-Cookie".equals( header ) ) {
                    cookie = new LinkedList< String >();

                    for( String value: headers.get( header ) ) {
                        cookie.add( value );
                    }
                }
                else if( "Location".equals( header ) ) {
                    location = URLDecoder.decode( headers.get( header ).get( 0 ) );
                }
            }

            return null;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public List< String > getCookie() {
            return cookie;
        }

        public String getLocation() {
            return location;
        }

        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
