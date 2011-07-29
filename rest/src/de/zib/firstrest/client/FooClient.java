/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.firstrest.client;

import de.zib.firstrest.domain.Foo;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 29.07.11  10:56
 * @brief
 */
public class FooClient {

    public final static String URI = "http://localhost:8080/rest/foo/";

    public static void main( String[] args ) {

        if ( args.length != 1 )  {
            System.err.print( "usage: java "+ FooClient.class.getName() + " <foo_id>" );
            System.exit( 1 );
        }
        final XStreamMarshaller marshaller =  new XStreamMarshaller();
        marshaller.setAutodetectAnnotations( true );
        marshaller.setAnnotatedClasses( new Class<?>[]{ Foo.class } );

        RestTemplate temp = new RestTemplate();
        temp.setMessageConverters(
            new ArrayList<HttpMessageConverter<?>>( 1 ) {{
                add( new MarshallingHttpMessageConverter( marshaller ) );
            }}
        );

        try {
            Foo foo = temp.getForObject( URI + args[0], Foo.class );
            System.out.println( foo );
            System.exit( 0 );
        } catch ( HttpClientErrorException ex ) {
            System.out.println( "HttpStatus: "+ ex.getStatusCode() );
            System.out.println( ex.getMessage() );
            System.exit( 2 );
        }
    }
}
