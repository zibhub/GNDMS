/*
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Map;

/**
 * @date: 14.03.12
 * @time: 13:06
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class HTTPGetter {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
    
    private String keyStoreLocation;
    private String trustStoreLocation;
    private String password; // TODO: don't use same password for keystore and key?!

    final private Map< Integer, EnhancedResponseExtractor > extractorMap;
    
    public HTTPGetter() {
        extractorMap = new HashMap< Integer, EnhancedResponseExtractor >();
        resetExtractorMap();
    }

    public void resetExtractorMap() {
        logger.trace( "Resetting ResponseExtractorMap." );

        extractorMap.clear();
        extractorMap.put( 3, new RedirectResponseExtractor() );
        extractorMap.put( 0, new DefaultResponseExtractor() );
    }
    
    public EnhancedResponseExtractor getResponseExtractor( int statusCode ) {
        return extractorMap.get( statusCode );
    }

    public void setupSSL( ) throws
            IOException,
            NoSuchAlgorithmException,
            KeyStoreException,
            CertificateException,
            UnrecoverableKeyException,
            KeyManagementException {
        logger.trace( "Resetting SSL context with Keystore " + keyStoreLocation + " and Truststore " + trustStoreLocation );

        SetupSSL setupSSL = new SetupSSL();
        setupSSL.setKeyStoreLocation(keyStoreLocation);
        setupSSL.setTrustStoreLocation( trustStoreLocation );

        setupSSL.initKeyStore(password.toCharArray(), password.toCharArray());
        setupSSL.prepareTrustStore( password.toCharArray() );
        setupSSL.setupDefaultSSLContext();
    }

    public void setExtractor( int httpStatusCode, EnhancedResponseExtractor responseExtractor ) {
        logger.trace( "Setting ResponseExtractor " + responseExtractor.getClass().getCanonicalName() + " for HTTP status code " + httpStatusCode );

        extractorMap.put( httpStatusCode, responseExtractor );
    }
    
    public int get( String url ) {
        logger.debug("GET URL " + url);

        EnhancedResponseExtractor responseExtractor = get( url, null );
        int statusCode = responseExtractor.getStatusCode();

        int redirectionCounter = 0;

        // redirect as long as needed
        while( 300 <= statusCode && statusCode < 400 ) {
            final List< String > cookies = DefaultResponseExtractor.getCookies( responseExtractor.getHeaders() );
            final String location = DefaultResponseExtractor.getLocation( responseExtractor.getHeaders() );

            logger.debug( "Redirection " + ++redirectionCounter );
            logger.trace( "Redirecting to " + location + " with cookies " + cookies.toString() );

            responseExtractor = get( location, new RequestCallback() {
                @Override
                public void doWithRequest( ClientHttpRequest request ) throws IOException {
                    for( String c: cookies )
                        request.getHeaders().add( "Cookie", c.split( ";", 2 )[0] );
                }
            });

            statusCode = responseExtractor.getStatusCode();
        }
        
        logger.debug( "HTTP GET Status Code " + statusCode + " after " + redirectionCounter + " redirections");

        return statusCode;
    }

    EnhancedResponseExtractor get( final String url, final RequestCallback requestCallback ) {
        RestTemplate rt = new RestTemplate();

        return rt.execute( url, HttpMethod.GET, requestCallback, new ResponseExtractor< EnhancedResponseExtractor >() {

            // call the EnhancedResponseExtractor registered for this response.statusCode
            @Override
            public EnhancedResponseExtractor extractData( ClientHttpResponse response ) throws IOException {
                int statusCode = response.getStatusCode().value();
                
                EnhancedResponseExtractor enhancedResponseExtractor = extractorMap.get( statusCode );
                if( null == enhancedResponseExtractor )
                    enhancedResponseExtractor = extractorMap.get( statusCode / 100 );
                if( null == enhancedResponseExtractor )
                    enhancedResponseExtractor = extractorMap.get( 0 );
                if( null == enhancedResponseExtractor )
                    throw new IllegalStateException( "No default ResponseExtractor registered. THIS IS NOT HAPPENING :/" );

                enhancedResponseExtractor.extractData(url, response);

                return enhancedResponseExtractor;
            }

        } );
    }
    
    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation( String keyStoreLocation ) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    public void setTrustStoreLocation( String trustStoreLocation ) {
        this.trustStoreLocation = trustStoreLocation;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
}
