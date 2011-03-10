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

import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.Quote;
import de.zib.gndms.rest.Facet;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 10.03.11  16:37
 * @brief
 */
public class TaskFlowExecClient {

    private GORFXClient gorfxClient;
    private TaskFlowClient tfClient;
    private TaskClient taskClient;


    public void execTF( AbstractTF order, String dn ) {

        String wid = UUID.randomUUID().toString();

        ResponseEntity<Specifier<Facets>> res = gorfxClient.createTaskFlow( order.getTaskFlowType(), order, dn, wid );

        if(! HttpStatus.CREATED.equals( res.getStatusCode() ) )
            throw new RuntimeException( "createTaskFlow failed " + res.getStatusCode() );

        String tid = res.getBody().getUrlMap().get( "id" );
        ResponseEntity<List<Specifier<Quote>>> res2 = tfClient.getQuotes( order.getTaskFlowType(), tid, dn, wid );

        if(! HttpStatus.OK.equals( res.getStatusCode() ) )
            throw new RuntimeException( "getQuotes failed " + res.getStatusCode() );

        Integer q = selectQuote( res2.getBody() );

        // 'til here it is valid to change the order and request new quotes
        ResponseEntity<Specifier<Facets>> res3 = tfClient.createTask( order.getTaskFlowType(), tid,
            q != null ? q.toString() : null, dn, wid );

        if(! HttpStatus.CREATED.equals( res.getStatusCode() ) )
            throw new RuntimeException( "getQuotes failed " + res.getStatusCode() );

        handleTaskSpecifier( res3.getBody() );

       }


    private void handleTaskSpecifier( Specifier<Facets> ts ) {
        showFacets( ts.getPayload() );
    }


   private void showFacets( Facets payload ) {

       for( Facet f : payload.getFacets() )
           System.out.println( f.getName() +": " + f.getUrl() );
    }


    private Integer selectQuote( List<Specifier<Quote>> quotes ) {
        for ( Specifier<Quote> sq : quotes ) {
            System.out.println( "showing quote " + sq.getUrlMap().get( "idx" ) );
            showQuote( sq.getPayload() );
        }
        return 0;
    }


   private void showQuote( Quote payload ) {
       System.out.println( "accepted: " +  payload.getAccepted() );
       System.out.println( "deadline: " + payload.getDeadline() );
       System.out.println( "expectedSize: " + payload.getExpectedSize() );
       System.out.println( "resultValidity: " + payload.getResultValidity() );
       System.out.println( "comments: " );
       for( String k : payload.getAdditionalNotes().keySet() )
           System.out.println(  "     " + k + ": " + payload.getAdditionalNotes().get( k ) );
    }
}

