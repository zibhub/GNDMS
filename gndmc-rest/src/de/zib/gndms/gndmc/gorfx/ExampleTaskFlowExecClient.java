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

import de.zib.gndms.logic.taskflow.tfmockup.DummyTFResult;
import de.zib.gndms.model.gorfx.types.*;
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
public class ExampleTaskFlowExecClient extends AbstractTaskFlowExecClient {


    @Override
    protected void handleFailure( TaskFailure fail ) {
        showFailure( fail );
        if( fail.hasNext() ) {
            System.out.println ( "Preceding: "   );
            handleFailure( fail.getNext() );
        }
    }


    protected void showFailure( TaskFailure fail ) {
        System.out.println( "Failed with: " + fail.getFaultClass() );
        System.out.println( "    message: " + fail.getMessage() );
        System.out.println( "    at     : " + fail.getFaultLocation() );
        System.out.println( "    trace  : " + fail.getFaultTrace() );
    }


    @Override
    protected void handleResult( TaskResult res ) {

        DummyTFResult dr = DummyTFResult.class.cast( res );
        System.out.println( "result: " + dr.getMessage() );
    }


    @Override
    protected void handleStatus( TaskStatus stat ){
        System.out.println( "Task is " + stat.getStatus() + " progress: " + stat.getProgress() + " / " + stat.getMaxProgress() );
    }

    @Override
    protected void handleTaskSpecifier( Specifier<Facets> ts ) {
        showFacets( ts.getPayload() );
    }


    private void showFacets( Facets payload ) {

        for( Facet f : payload.getFacets() )
            System.out.println( f.getName() +": " + f.getUrl() );
    }


    @Override
    protected Integer selectQuote( List<Specifier<Quote>> quotes ) {
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
        if( payload.getAdditionalNotes() != null )
            for( String k : payload.getAdditionalNotes().keySet() )
                System.out.println(  "     " + k + ": " + payload.getAdditionalNotes().get( k ) );
        else
            System.out.println( "   none" );
    }
}

