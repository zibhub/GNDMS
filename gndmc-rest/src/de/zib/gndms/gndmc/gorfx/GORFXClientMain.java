package de.zib.gndms.gndmc.gorfx;
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

import com.sun.net.httpserver.HttpHandler;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.rest.Facet;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 10.02.11  15:57
 * @brief A test run for the gorfx client.
 */
public class GORFXClientMain extends AbstractApplication {


    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    protected String gorfxEpUrl;
    @Option( name="-dn", required=true, usage="DN" )
    protected String dn;


    public static void main(String[] args) throws Exception {


        GORFXClientMain cnt = new GORFXClientMain();
        cnt.run( args );
        System.exit( 0 );
    }


    @Override
    public void run() throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml");
        FullGORFXClient gorfxClient =
            ( FullGORFXClient ) context.getAutowireCapableBeanFactory().createBean(
                FullGORFXClient.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        gorfxClient.setServiceURL( gorfxEpUrl );

        if( gorfxClient.getRestTemplate() == null )
            throw new IllegalStateException( "restTemplate is null" );

        System.out.println( "requesting facets" );
        ResponseEntity<Facets> res = gorfxClient.listAvailableFacets( dn );
        System.out.println( "StatusCode: " + res.getStatusCode() );
        showHeader( res.getHeaders() );
        System.out.println( "Body: " );
        Facets f = res.getBody();
        for ( Facet fa : f.getFacets() ) {
            System.out.println( fa.getName() + " " + fa.getUrl() );
        }
    }


    public static void showHeader( HttpHeaders head ) {

        GNDMSResponseHeader h = new GNDMSResponseHeader( head );

        showList( "parentURL", h.getParentURL() );
        showList( "facetURL", h.getFacetURL() );
        showList( "DN", h.getDN() );
        showList( "WId", h.getWid() );
    }


    public static void showList( String name, List<String> list ) {

        StringBuffer sb = new StringBuffer(  );

        sb.append( name + ": "  );
        if ( list == null || list.size() == 0 ) {
            sb.append( "NIL\n" );
            return;
        }

        for ( String s: list) {
            sb.append( s );
            sb.append( "," );
        }

        sb.replace( sb.length() - 1, sb.length() - 1, "" );

        System.out.println( sb.toString() );
    }

}
