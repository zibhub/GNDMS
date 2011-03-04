package de.zib.gndms.GORFX.service;

import de.zib.gndms.logic.taskflow.TaskFlowProvider;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.gorfx.types.TaskStatus;
import de.zib.gndms.rest.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

/**
 * @author try ma ik jo rr a zib
 * @date 13.01.2011 15:17:47
 *
 * @biref A controller for a REST taskflow resource which implements the
 * TaskFlowService interface.
 *
 * The taskflow service acts as interface to instantiated taskflow
 * resources. The instantiation or creation happens through the GORFX
 * service itself.
 *
 * This implicates that all method invocation concern a single
 * taskflow, not the service as a whole.
 */
@Controller
@RequestMapping( "/gorfx" )
public class TaskFlowServiceImpl implements TaskFlowService {

    // private ORQDao orqDao;
    private String serviceUrl; // inject or read from properties TODO CHECK IF THIS INCLUDES GORFX POSTFIX
    private TaskFlowProvider taskFlowProvider;
    private List<String> facetsNames = new ArrayList<String>( 7 );

    public void init( ) {
        facetsNames.add( "order" );
        facetsNames.add( "quote" );
        facetsNames.add( "task" );
        facetsNames.add( "result" );
        facetsNames.add( "status" );
        facetsNames.add( "errors" );

    }

    @RequestMapping( value = "/_{type}/_{id}", method = RequestMethod.GET )
    public ResponseEntity<Facets> getFacets( @PathVariable String type, @PathVariable String id, @RequestHeader( "DN" ) String dn ) {

        Map<String,String> uriargs = new HashMap<String, String>( 2 );
        uriargs.put( "id", id );
        uriargs.put( "type", type );
        UrlFactory uf = new UrlFactory( serviceUrl );

        GNDMSResponseHeader header = new GNDMSResponseHeader( uf.taskFlowTypeUrl( uriargs, null ), null, serviceUrl, dn, null );

        if( taskFlowProvider.exists( type ) ) {
            TaskFlow tf = taskFlowProvider.getFactoryForTaskFlow( type ).find( id );
            if ( tf != null ) {
                List<Facet> fl = new ArrayList<Facet>( 6 );
                for( String f : facetsNames ) {
                    String fn = uf.taskFlowUrl( uriargs, f );
                    fl.add( new Facet( f, fn ) );
                }

                return new ResponseEntity<Facets>( new Facets( fl ), header, HttpStatus.OK );
            }
        }

        return new ResponseEntity<Facets>( null, header, HttpStatus.NOT_FOUND );
    }


    @RequestMapping( value = "/_{type}/_{id}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteTaskflow( @PathVariable String type, @PathVariable String id,
                                                @RequestHeader( "DN" ) String dn,
                                                @RequestHeader( "WId" ) String wid ) {
        return null;  // not required here
    }


    @RequestMapping( value = "/_{type}/_{id}/order", method = RequestMethod.GET )
    public ResponseEntity<AbstractTF> getOrder( @PathVariable String type, @PathVariable String id,
                                                @RequestHeader( "DN" ) String dn,
                                                @RequestHeader( "WId" ) String wid ) {
      //  TypedUUId uid = orqDao.create( type );
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/order", method = RequestMethod.POST )
    public ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id,
                                          @RequestBody AbstractTF orq, @RequestHeader( "DN" ) String dn,
                                          @RequestHeader( "WId" ) String wid ) {

        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/quotes", method = RequestMethod.GET )
    public ResponseEntity<List<Specifier<Quote>>> getQuotes( @PathVariable String type, @PathVariable String id,
                                                  @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {
        // retrieve a list
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/quotes/", method = RequestMethod.POST )
    public ResponseEntity<Void> setQuote( @PathVariable String type, @PathVariable String id,
                                          @RequestBody Quote cont, @RequestHeader( "DN" ) String dn,
                                          @RequestHeader( "WId" ) String wid ) {
        // add an desired quotation for the order
        return null;
    }




    @RequestMapping( value = "/_{type}/_{id}/quotes/_{idx}", method = RequestMethod.GET )
    public ResponseEntity<Quote> getQuote( @PathVariable String type, @PathVariable String id, @PathVariable int idx,
                                           @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {
        // retrieve desired quotes for the order.
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/quotes/_{idx}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteQuotes( @PathVariable String type, @PathVariable String id,
                                              @PathVariable int idx, @RequestHeader( "DN" ) String dn,
                                              @RequestHeader( "WId" ) String wid ) {
        // retrieve desired quotes for the order.
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/task/", method = RequestMethod.GET )
    public ResponseEntity<Specifier<Facets>> getTask( @PathVariable String type, @PathVariable String id,
                                           @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {
        // redirects to the task of the taskflow, creates this task if it doesn't exists.
        boolean taskExists=true;
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        ResponseEntity<String> resp;
        if ( taskExists )
            resp = new ResponseEntity<String>( "the string URL", responseHeaders, HttpStatus.FOUND );
        else
            resp = new ResponseEntity<String>( null, responseHeaders, HttpStatus.NOT_FOUND );

        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/task/", method = RequestMethod.PUT )
    public ResponseEntity<Specifier<Facets>> createTask( @PathVariable String type, @PathVariable String id,
                                              @RequestParam( value = "quote", required = false ) String quoteId,
                                              @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {
        // redirects to the task of the taskflow, creates this task if it doesn't exists.
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        //return new ResponseEntity<Specifier<Facets>>( "the string URL", responseHeaders, HttpStatus.CREATED );
        return null;

    }


    @RequestMapping( value = "/_{type}/_{id}/status", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowStatus> getStatus( @PathVariable String type, @PathVariable String id,
                                                     @RequestHeader( "DN" ) String dn,
                                                     @RequestHeader( "WId" ) String wid ) {

        // Actual Task Error URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/result", method = RequestMethod.GET )
    public ResponseEntity<Specifier<TaskResult>> getResult( @PathVariable String type, @PathVariable String id,
                                                     @RequestHeader( "DN" ) String dn,
                                                     @RequestHeader( "WId" ) String wid ) {

            // Actual Task Result URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/_{type}/_{id}/errors", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowFailure> getErrors( @PathVariable String type, @PathVariable String id,
                                                      @RequestHeader( "DN" ) String dn,
                                                      @RequestHeader( "WId" ) String wid ) {

		// Actual Task Error URL (Maybe as Redirect)
        return null;
    }
}
