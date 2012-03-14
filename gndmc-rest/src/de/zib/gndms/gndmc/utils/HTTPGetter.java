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

import de.zib.gndms.gndmc.security.SetupSSL;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @date: 14.03.12
 * @time: 13:06
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class HTTPGetter {
    
    private String keyStoreLocation;
    private String trustStoreLocation;
    private String password;

    final private Map< Integer, EnhancedResponseExtractor > extractorMap;
    
    public HTTPGetter() {
        extractorMap = new HashMap< Integer, EnhancedResponseExtractor >();
        resetExtractorMap();
    }
    
    public void setupSSL( ) throws
            IOException,
            NoSuchAlgorithmException,
            KeyStoreException,
            CertificateException,
            UnrecoverableKeyException,
            KeyManagementException {
        SetupSSL setupSSL = new SetupSSL();
        setupSSL.setKeystoreLocation( keyStoreLocation );
        setupSSL.setTrustStoreLocation(trustStoreLocation);

        setupSSL.prepareUserCert(password.toCharArray(), password.toCharArray());
        setupSSL.setupDefaultSSLContext();
    }

    public void resetExtractorMap() {
        extractorMap.clear();
        extractorMap.put(0, new RedirectResponseExtractor());
    }
    
    public void setExtractor( int httpStatusCode, EnhancedResponseExtractor responseExtractor ) {
        extractorMap.put( httpStatusCode, responseExtractor );
    }
    
    public int get( String url ) {
        EnhancedResponseExtractor responseExtractor = get( url, null );
        int statusCode = responseExtractor.getStatusCode();

        while( 302 == statusCode) {
            final List< String > cookies = DefaultResponseExtractor.getCookies( responseExtractor.getHeaders() );
            final String location = DefaultResponseExtractor.getLocation( responseExtractor.getHeaders() );

            responseExtractor = get( location, new RequestCallback() {
                @Override
                public void doWithRequest( ClientHttpRequest request ) throws IOException {
                    for( String c: cookies )
                        request.getHeaders().add( "Cookie", c.split( ";", 2 )[0] );
                }
            });

            statusCode = responseExtractor.getStatusCode();
        }

        return statusCode;
    }

    EnhancedResponseExtractor get( String url, RequestCallback requestCallback ) {
        RestTemplate rt = new RestTemplate();
        // use the list as indirect pointer
        final List< EnhancedResponseExtractor > enhancedResponseExtractor = new LinkedList<EnhancedResponseExtractor>();

        rt.execute(url, HttpMethod.GET, requestCallback, new ResponseExtractor<Object>() {

            @Override
            public Object extractData( ClientHttpResponse response ) throws IOException {
                int statusCode = response.getStatusCode().value();
                
                EnhancedResponseExtractor ere = extractorMap.get( statusCode );
                if( null == ere )
                    ere = extractorMap.get( 0 );
                
                enhancedResponseExtractor.add( ere );
                return ere.extractData( response );
            }

        } );

        return enhancedResponseExtractor.get( 0 );
    }
    
    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    public void setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
