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
import de.zib.gndms.gndmc.security.SetupSSL;
import de.zib.gndms.stuff.devel.StreamCopyNIO;
import org.kohsuke.args4j.Option;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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


    public static void main( String[] args ) throws Exception {

        ESGFGet cnt = new ESGFGet();
        cnt.run(args);
        System.exit(0);
        
    }


    @Override
    protected void run() throws Exception {

        SetupSSL setupSSL = new SetupSSL();
        setupSSL.setKeystoreLocation( keyStoreLocation );
        setupSSL.prepareUserCert( passwd.toCharArray(), passwd.toCharArray() );
        setupSSL.setupDefaultSSLContext();
                
        RestTemplate rt = new RestTemplate();
        
        rt.execute( url, HttpMethod.GET, null, new ResponseExtractor<Object>() {
            @Override
            public Object extractData( final ClientHttpResponse response ) throws IOException {
                
                System.out.println( response.getStatusCode().toString() );
                for ( String s: response.getHeaders().keySet() )
                    for( String v : response.getHeaders().get( s ) )
                        System.out.println( s+ ":"+v );

                System.out.println( response.getStatusCode().toString() );

                System.out.println( "Received data, copying" );
                InputStream is = response.getBody();
                OutputStream os = new FileOutputStream( off );
                StreamCopyNIO.copyStream( is, os );
                System.out.println( "Done" );
                return null;
            }
        } );
    }
}
