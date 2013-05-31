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


import de.zib.gndms.common.model.gorfx.types.*;
import de.zib.gndms.common.rest.Facet;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 10.03.11  16:37
 * @brief An example implementation of AbstractTaskFlowExecClient.
 *
 * This on executes DummyOrder taskflows.
 */
public abstract class ExampleTaskFlowExecClient extends AbstractTaskFlowExecClient {


    /**
     * @brief Prints the failure to stdout.
     * @param fail The failure object.
     */
    @Override
    public void handleFailure( TaskFailure fail ) {
        showFailure( fail );
        if( fail.hasNext() ) {
            System.out.println ( "Preceding: "   );
            handleFailure( fail.getNext() );
        }
    }


    /**
     * @brief Helper for handleFailure.
     * @param fail The failure object.
     */
    protected void showFailure( TaskFailure fail ) {
        System.out.println( "Failed with: " + fail.getFaultClass() );
        System.out.println( "    message: " + fail.getMessage() );
        System.out.println( "    at     : " + fail.getFaultLocation() );
        System.out.println( "    trace  : " + fail.getFaultTrace() );
    }


    /**
     * @brief Prints the result to stdout.
     * @param res The result object.
     */
    @Override
    public abstract void handleResult( TaskResult res );


    /**
     * @brief Prints the progress to stdout.
     * @param stat The current task status.
     */
    @Override
    public void handleStatus( TaskStatus stat ){
        System.out.println( "Task is " + stat.getStatus() + " progress: " + stat.getProgress() + " / " + stat.getMaxProgress() );
    }


    /**
     * @brief Shows the facets of \c ts.
     * @param ts The task specifier.
     */
    @Override
    protected void handleTaskSpecifier( Specifier<Facets> ts ) {
        showFacets( ts.getPayload() );
    }


    /**
     * @brief Helper for handleTaskSpecifier
     * @param facets ...
     */
    private void showFacets( Facets facets ) {

        for( Facet f : facets.getFacets() )
            System.out.println( f.getName() +": " + f.getUrl() );
    }


    /**
     * @brief Shows available quotes.
     * @param quotes All available quotes.
     *
     * @return Always selects the 0th.
     */
    @Override
    protected Integer selectQuote( List<Specifier<Quote>> quotes ) {
        for ( Specifier<Quote> sq : quotes ) {
            System.out.println( "showing quote " + sq.getUriMap().get( "idx" ) );
            showQuote( sq.getPayload() );
        }
        return 0;
    }


    /**
     * @brief Helper which prints a quote to stdout.
     * @param payload The quote.
     */
    private void showQuote( Quote payload ) {
        System.out.println( "accepted: " +  payload.getAccepted() );
        System.out.println( "deadline: " + payload.getDeadline() );
        System.out.println( "expectedSize: " + payload.getExpectedSize() );
        System.out.println( "resultValidity: " + payload.getResultValidity() );
        System.out.println( "comments: " );
        if( payload.getAdditionalNotes() != null )
            for( String k : payload.getAdditionalNotes().keySet() )
                System.out.println(  "     " + k + ": " + payload.getAdditionalNotes().get( k ) );
        else if( payload.getContext() != null )
            for( String k : payload.getContext().keySet() )
                System.out.println(  "     " + k + ": " + payload.getContext().get( k ) );
        else
            System.out.println( "   none" );
    }
}

