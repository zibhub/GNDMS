package de.zib.gndms.GORFX.service;

import de.zib.gndms.devel.NotYetImplementedException;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.logic.taskflow.AbstractQuoteCalculator;
import de.zib.gndms.logic.taskflow.TaskFlowFactory;
import de.zib.gndms.logic.taskflow.TaskFlowProvider;
import de.zib.gndms.logic.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private UriFactory uriFactory;
    private TaskClient taskClient;


    @PostConstruct
    public void init( ) {
        facetsNames.add( "order" );
        facetsNames.add( "quote" );
        facetsNames.add( "task" );
        facetsNames.add( "result" );
        facetsNames.add( "status" );
        facetsNames.add( "errors" );
        uriFactory = new UriFactory( serviceUrl );
        taskClient = new TaskClient( serviceUrl );

    }

    @RequestMapping( value = "/_{type}/_{id}", method = RequestMethod.GET )
    public ResponseEntity<Facets> getFacets( @PathVariable String type, @PathVariable String id, @RequestHeader( "DN" ) String dn ) {

        Map<String,String> uriargs = new HashMap<String, String>( 2 );
        uriargs.put( "id", id );
        uriargs.put( "type", type );
        uriargs.put( "service", "gorfx" );

        GNDMSResponseHeader header = new GNDMSResponseHeader( uriFactory.taskFlowTypeUri( uriargs, null ), null, serviceUrl, dn, null );

        if( taskFlowProvider.exists( type ) ) {
            TaskFlow tf = taskFlowProvider.getFactoryForTaskFlow( type ).find( id );
            if ( tf != null ) {
                List<Facet> fl = new ArrayList<Facet>( 6 );
                for( String f : facetsNames ) {
                    String fn = uriFactory.taskFlowUri( uriargs, f );
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

        HttpStatus hs = HttpStatus.NOT_FOUND;
        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if ( tf != null ) {
                tff.delete( id );
                hs = HttpStatus.OK;
            }
        }

        return new ResponseEntity<Void>( null, getHeader( type, id, null, dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/order", method = RequestMethod.GET )
    public ResponseEntity<AbstractTF> getOrder( @PathVariable String type, @PathVariable String id,
                                                @RequestHeader( "DN" ) String dn,
                                                @RequestHeader( "WId" ) String wid ) {
        HttpStatus hs = HttpStatus.NOT_FOUND;
        AbstractTF order = null;
        try {
            TaskFlow tf = findTF( type, id );
            order = tf.getOrder();
            if ( order != null )
                hs = HttpStatus.OK;
        } catch ( NoSuchTaskFlowException e ) { /* intentionally */ }

        return new ResponseEntity<AbstractTF>( order, getHeader( type, id, "order", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/order", method = RequestMethod.POST )
    public ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id,
                                          @RequestBody AbstractTF orq, @RequestHeader( "DN" ) String dn,
                                          @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if( tf != null ) {
                AbstractQuoteCalculator qc = tff.getQuoteCalculator();
                qc.setOrder( orq );
                if( qc.validate() )
                    hs = HttpStatus.OK;
                else
                    hs = HttpStatus.BAD_REQUEST;
            }
        }

        return new ResponseEntity<Void>( null, getHeader( type, id, "order", dn, wid ), hs );

    }


    @RequestMapping( value = "/_{type}/_{id}/quotes", method = RequestMethod.GET )
    public ResponseEntity<List<Specifier<Quote>>> getQuotes( @PathVariable String type, @PathVariable String id,
                                                  @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {
        HttpStatus hs = HttpStatus.NOT_FOUND;
        List<Specifier<Quote>> res = null;
        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if( tf != null ) {
                AbstractQuoteCalculator qc = tff.getQuoteCalculator();
                qc.setOrder( tf.getOrder() );
                try {
                    List<Quote> lc;
                    if( tf.hasPreferredQuote( ) )
                        lc = qc.createQuotes( tf.getPreferredQuote() );
                    else
                        lc = qc.createQuotes( );

                    res = new ArrayList<Specifier<Quote>>( lc.size() );
                    tf.setQuotes( lc );
                    HashMap<String, String> urimap = new HashMap<String, String>( 3 );
                    for( int i=0; i < lc.size(); ++i ) {
                        urimap.put( "idx", String.valueOf( i ) );
                        Specifier<Quote> sq = new Specifier<Quote>();
                        sq.setUriMap( new HashMap<String, String>( urimap ) );
                        sq.setURL( uriFactory.quoteUri( urimap ) );
                        sq.setPayload( lc.get( i ) );
                        res.add( sq );
                    }

                } catch ( UnsatisfiableOrderException e ) {
                    tf.setUnfulfillableOrder( true );
                    hs = HttpStatus.BAD_REQUEST;
                }
            }
        }

        return new ResponseEntity<List<Specifier<Quote>>>( res, getHeader( type, id, "quotes", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/quotes/", method = RequestMethod.POST )
    public ResponseEntity<Void> setQuote( @PathVariable String type, @PathVariable String id,
                                          @RequestBody Quote cont, @RequestHeader( "DN" ) String dn,
                                          @RequestHeader( "WId" ) String wid ) {


        return new ResponseEntity<Void>( null, getHeader( type, id, "quotes", dn, wid ), HttpStatus.OK );
    }




    @RequestMapping( value = "/_{type}/_{id}/quotes/_{idx}", method = RequestMethod.GET )
    public ResponseEntity<Quote> getQuote( @PathVariable String type, @PathVariable String id, @PathVariable int idx,
                                           @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        Quote quote = null;
        try {
            TaskFlow tf = findTF( type, id );
            List<Quote> lq = tf.getQuotes();
            if( lq != null && idx >= 0 && idx < lq.size() ) {
                quote = lq.get( idx );
                hs = HttpStatus.OK;
            }

        } catch ( NoSuchTaskFlowException e ) {
            // intentionally
        }

        return new ResponseEntity<Quote>( quote, getHeader( type, id, "quotes", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/quotes/_{idx}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteQuotes( @PathVariable String type, @PathVariable String id,
                                              @PathVariable int idx, @RequestHeader( "DN" ) String dn,
                                              @RequestHeader( "WId" ) String wid ) {

       throw new NotYetImplementedException(); // together with the ProperlyNeverWillBeException
    }


    @RequestMapping( value = "/_{type}/_{id}/task/", method = RequestMethod.GET )
    public ResponseEntity<Specifier<Facets>> getTask( @PathVariable String type, @PathVariable String id,
                                           @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        Specifier<Facets> spec = null;
        try {
            TaskFlow tf = findTF( type, id );
            Task t = tf.getTask();
            if( t != null ) {

                spec = new Specifier<Facets>();
                HashMap<String, String> urimap = taskUriMap( type, id, t );
                spec.setURL( uriFactory.taskUri( urimap, null ) );
                spec.setUriMap( urimap );

                ResponseEntity<Facets> res = taskClient.getTaskFacets( t.getId(), dn );
                spec.setPayload( res.getBody() );
                hs = HttpStatus.OK;
            }
        } catch ( NoSuchTaskFlowException e ) {
            // intentionally
        }
        return new ResponseEntity<Specifier<Facets>>( spec, getHeader( type, id, "task", dn, wid ), hs );
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

        HttpStatus hs = HttpStatus.NOT_FOUND;
        TaskFlowStatus tfs = null;
        try {
            TaskFlow tf = findTF( type, id );
            tfs = TaskFlowStatus.fromTaskFlow( tf );
            Task t = tf.getTask();
            if( t != null ) {
                Specifier<Void> spec = new Specifier<Void>();
                HashMap<String, String> urimap = taskUriMap( type, id, t );
                spec.setURL( uriFactory.taskUri( urimap, null ) );
                spec.setUriMap( urimap );
                tfs.setTaskSpecifier( spec );

                hs = HttpStatus.OK;
            }
        } catch ( NoSuchTaskFlowException e ) {
            // intentionally
        }
        return new ResponseEntity<TaskFlowStatus>( tfs, getHeader( type, id, "status", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/result", method = RequestMethod.GET )
    public ResponseEntity<Specifier<TaskResult>> getResult( @PathVariable String type, @PathVariable String id,
                                                     @RequestHeader( "DN" ) String dn,
                                                     @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        Specifier<TaskResult> spec = null;
        try {
            TaskFlow tf = findTF( type, id );
            Task t = tf.getTask();
            if( t != null ) {

                spec = new Specifier<TaskResult>();
                HashMap<String, String> urimap = taskUriMap( type, id, t );
                spec.setURL( uriFactory.taskUri( urimap, null ) );
                spec.setUriMap( urimap );

                ResponseEntity<TaskResult> res = taskClient.getResult( t.getId(), dn, wid );
                spec.setPayload( res.getBody() );
                hs = HttpStatus.OK;
            }
        } catch ( NoSuchTaskFlowException e ) {
            // intentionally
        }
        return new ResponseEntity<Specifier<TaskResult>>( spec, getHeader( type, id, "result", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/errors", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowFailure> getErrors( @PathVariable String type, @PathVariable String id,
                                                      @RequestHeader( "DN" ) String dn,
                                                      @RequestHeader( "WId" ) String wid ) {

		// Actual Task Error URL (Maybe as Redirect)
        return null;
    }


    protected GNDMSResponseHeader getHeader( String type, String id, String facet, String dn, String wid ) {

        Map<String,String> uriargs = new HashMap<String, String>( 2 );
        uriargs.put( "id", id );
        uriargs.put( "type", type );
        uriargs.put( "service", "gorfx" );


        return new GNDMSResponseHeader( uriFactory.taskFlowTypeUri( uriargs, facet ), facet, serviceUrl, dn, wid );

    }


    private HashMap<String, String> taskUriMap( String type, String id, Task t ) {
        HashMap<String,String> urimap = new HashMap<String, String>( 4 );
        urimap.put( "service", "gorfx" );
        urimap.put( "type", type );
        urimap.put( "id", id );
        urimap.put( "taskId", t.getId() );
        return urimap;
    }

    protected TaskFlow findTF( String type, String id ) throws NoSuchTaskFlowException {

        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if ( tf != null )
                return tf;
        }

        throw new NoSuchTaskFlowException( );
    }
}
