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
import de.zib.gndms.common.kit.security.SetupSSL;
import de.zib.gndms.stuff.devel.StreamCopyNIO;
import org.kohsuke.args4j.Option;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLDecoder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 13.03.12  15:51
 * @brief
 */
public class ESGFGet extends AbstractApplication {

    @Option( name="-keystorePasswd", required = true )
    protected String passwd;
    @Option( name="-url", required = true )
    protected String url;
    @Option( name="-out", required = true )
    protected String off;
    @Option( name="-cred", required = true )
    protected String keyStoreLocation;

    @Option( name="-dummy", required = false )
    protected String dummy;

    public static void main( String[] args ) throws Exception {

        CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
        InputStream is = new FileInputStream( "/var/tmp/gndms/keystore/x509_proxy.pem" );
        System.out.println( cf.generateCertificate( is ) );
        System.out.println( " ------------------------------------------------- " );
        Thread.sleep( 1000 );
        System.out.println( "AND: " + cf.generateCertificate( is ) );
        System.exit( 0 );
        
        ESGFGet cnt = new ESGFGet();
        cnt.run(args);
        System.exit(0);
        
    }


    @Override
    protected void run() throws Exception {

        SetupSSL setupSSL = new SetupSSL();
        setupSSL.setKeyStoreLocation( keyStoreLocation );
        setupSSL.prepareKeyStore( passwd, passwd );
        setupSSL.setupDefaultSSLContext( passwd );
                
        final RestTemplate rt = new RestTemplate();
        
        rt.execute( url, HttpMethod.GET, null, new ResponseExtractor<Object>() {
            @Override
            public Object extractData( final ClientHttpResponse response ) throws IOException {
                
                String url = null;
                String cookieTmp = null;
                System.out.println( response.getStatusCode().toString() );
                for ( String s: response.getHeaders().keySet() )
                    for( String v : response.getHeaders().get( s ) ) {
                        System.out.println( s+ ":"+v );
                        if( "Location".equals( s ) )
                            url = v;
                        else if( "Set-Cookie".equals( s ) )
                            cookieTmp = v;
                    }
                final String cookie = cookieTmp.split( ";" )[0];


                if( url != null )
                    rt.execute( decodeUrl( url ), HttpMethod.GET, new RequestCallback() {
                                @Override
                                public void doWithRequest( final ClientHttpRequest request )
                                        throws IOException
                                {

                                    System.out.println( "setting cookie: " + cookie );
                                    request.getHeaders().set( "Cookie", cookie );
                                }
                            }, new ResponseExtractor<Object>() {
                        @Override
                        public Object extractData( final ClientHttpResponse response )
                                throws IOException
                        {

                            System.out.println( response.getStatusCode().toString() );
                            System.out.println( "Received data, copying" );
                            InputStream is = response.getBody();
                            OutputStream os = new FileOutputStream( off );
                            StreamCopyNIO.copyStream( is, os );
                            System.out.println( "Done" );
                            return null;
                        }
                    }
                    );
                            
                return null;
            }
        } );
    }


    private String decodeUrl( final String url ) {
        System.out.println( "encoded url" + url );
        String durl;
        try {
            durl = URLDecoder.decode( url, "UTF-8" );
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException( e );
        }
        System.out.println( "decoded url" + durl );
        
        return durl;

    }
}
