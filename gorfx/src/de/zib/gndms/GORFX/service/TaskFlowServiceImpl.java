package de.zib.gndms.GORFX.service;

import de.zib.gndms.GORFX.service.util.WidAux;
import de.zib.gndms.devel.NotYetImplementedException;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.logic.taskflow.*;
import de.zib.gndms.logic.taskflow.executor.TFExecutor;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
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
 * @brief A controller for a REST taskflow resource which implements the
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
    private Logger logger = LoggerFactory.getLogger( this.getClass() );


    @PostConstruct
    public void init( ) {
        facetsNames.add( "order" );
        facetsNames.add( "quote" );
        facetsNames.add( "task" );
        facetsNames.add( "result" );
        facetsNames.add( "status" );
        facetsNames.add( "errors" );
        uriFactory = new UriFactory( serviceUrl );
        taskClient.setServiceURL( serviceUrl );

    }

    @RequestMapping( value = "/_{type}/_{id}", method = RequestMethod.GET )
    public ResponseEntity<Facets> getFacets( @PathVariable String type, @PathVariable String id, @RequestHeader( "DN" ) String dn ) {

        Map<String,String> uriargs = new HashMap<String, String>( 2 );
        uriargs.put( UriFactory.TASKFLOW_ID, id );
        uriargs.put( UriFactory.TASKFLOW_TYPE, type );
        uriargs.put( UriFactory.SERVICE, "gorfx" );

        GNDMSResponseHeader header = new GNDMSResponseHeader( uriFactory.taskFlowTypeUri( uriargs, null ), null, serviceUrl, dn, null );

        if( taskFlowProvider.exists( type ) ) {
            TaskFlow tf = taskFlowProvider.getFactoryForTaskFlow( type ).find( id );
            if ( tf != null ) {
                ArrayList<Facet> fl = new ArrayList<Facet>( 6 );
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
    public ResponseEntity<Order> getOrder( @PathVariable String type, @PathVariable String id,
                                                @RequestHeader( "DN" ) String dn,
                                                @RequestHeader( "WId" ) String wid ) {
        HttpStatus hs = HttpStatus.NOT_FOUND;
        Order order = null;
        try {
            TaskFlow tf = findTF( type, id );
            order = tf.getOrder();
            if ( order != null )
                hs = HttpStatus.OK;
        } catch ( NoSuchResourceException e ) { /* intentionally */ }

        return new ResponseEntity<Order>( order, getHeader( type, id, "order", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/order", method = RequestMethod.POST )
    public ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id,
                                          @RequestBody Order orq, @RequestHeader( "DN" ) String dn,
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


    @RequestMapping( value = "/_{type}/_{id}/quote", method = RequestMethod.GET )
    public ResponseEntity<List<Specifier<Quote>>> getQuotes( @PathVariable String type, @PathVariable String id,
                                                  @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {
        WidAux.initWid( wid );
        logger.debug( "quote called" );
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
                    urimap.put( "service", "gorfx" );
                    urimap.put( UriFactory.TASKFLOW_ID, id );
                    urimap.put( "type", type );
                    for( int i=0; i < lc.size(); ++i ) {
                        urimap.put( "idx", String.valueOf( i ) );
                        Specifier<Quote> sq = new Specifier<Quote>();
                        sq.setUriMap( new HashMap<String, String>( urimap ) );
                        sq.setURL( uriFactory.quoteUri( urimap ) );
                        sq.setPayload( lc.get( i ) );
                        res.add( sq );
                    }
                    hs = HttpStatus.OK;
                } catch ( UnsatisfiableOrderException e ) {
                    Order o = tf.getOrder();
                    logger.debug( "Unsatisfiable order: " + o );
                    tf.setUnfulfillableOrder( true );
                    hs = HttpStatus.BAD_REQUEST;
                }
            }
        }

        WidAux.removeWid();
        return new ResponseEntity<List<Specifier<Quote>>>( res, getHeader( type, id, "quote", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/quote", method = RequestMethod.POST )
    public ResponseEntity<Void> setQuote( @PathVariable String type, @PathVariable String id,
                                          @RequestBody Quote cont, @RequestHeader( "DN" ) String dn,
                                          @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        try {
            TaskFlow tf = findTF( type, id );
            tf.setPreferredQuote( cont );
            hs = HttpStatus.OK;

        } catch ( NoSuchResourceException e ) {

        }

        return new ResponseEntity<Void>( null, getHeader( type, id, "quote", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/quote/_{idx}", method = RequestMethod.GET )
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

        } catch ( NoSuchResourceException e ) {
            // intentionally
        }

        return new ResponseEntity<Quote>( quote, getHeader( type, id, "quote", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/quote/_{idx}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteQuotes( @PathVariable String type, @PathVariable String id,
                                              @PathVariable int idx, @RequestHeader( "DN" ) String dn,
                                              @RequestHeader( "WId" ) String wid ) {

       throw new NotYetImplementedException(); // together with the ProperlyNeverWillBeException
    }


    @RequestMapping( value = "/_{type}/_{id}/task", method = RequestMethod.GET )
    public ResponseEntity<Specifier<Facets>> getTask( @PathVariable String type, @PathVariable String id,
                                           @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {

        WidAux.initWid( wid );
        logger.debug( "getTask task called" );
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
                if( res != null )
                    if( HttpStatus.OK.equals( res.getStatusCode() ) ) {
                        spec.setPayload( res.getBody() );
                        hs = HttpStatus.OK;

                    } else {
                        logger.debug( "unexpected status: " + res.getStatusCode().name() );
                    }
                else
                    logger.debug( "getTaskFacets returned null" );
            }
        } catch ( NoSuchResourceException e ) {
            // intentionally
        }
        logger.debug( "returning with " + hs.name() );
        WidAux.removeWid();
        return new ResponseEntity<Specifier<Facets>>( spec, getHeader( type, id, "task", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/task", method = RequestMethod.PUT )
    public ResponseEntity<Specifier<Facets>> createTask( @PathVariable String type, @PathVariable String id,
                                              @RequestParam( value = "quote", required = false ) String quoteId,
                                              @RequestHeader( "DN" ) String dn, @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;

        WidAux.initWid( wid );
        logger.debug( "create task called" );
        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if ( tf != null ) {
                if( tf.getTask() != null )
                    hs = HttpStatus.CONFLICT;
                else {
                    TaskAction ta = tff.createAction( );
                    TaskServiceAux taskServiceAux = new TaskServiceAux( type, id, dn, wid, ta ).invoke();
                    tf.setTask( taskServiceAux.getTask( ) );
                    if ( taskServiceAux.is() )
                        return new ResponseEntity<Specifier<Facets>>( res.getBody(), res.getHeaders(), HttpStatus.CREATED );
                }
            }
        }
        logger.debug( "Problem, returning " + hs.name() );
        WidAux.removeWid();
        return new ResponseEntity<Specifier<Facets>>( null, getHeader( type, id, "task", dn, wid), hs );
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
                spec.setURL( uriFactory.taskUri( urimap, "status" ) );
                spec.setUriMap( urimap );
                tfs.setTaskSpecifier( spec );

                hs = HttpStatus.OK;
            }
        } catch ( NoSuchResourceException e ) {
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
            spec = createTaskSpecifier( TaskResult.class, type, id, "result" );

            ResponseEntity<TaskResult> res = taskClient.getResult( spec.getUriMap().get( "taskId" ), dn, wid );
            if ( HttpStatus.OK.equals( res.getStatusCode() ) ) {
                spec.setPayload( res.getBody() );
                hs = HttpStatus.OK;
            } else spec = null;
        } catch ( NoSuchResourceException e ) {
            // intentionally
        }
        return new ResponseEntity<Specifier<TaskResult>>( spec, getHeader( type, id, "result", dn, wid ), hs );
    }


    @RequestMapping( value = "/_{type}/_{id}/errors", method = RequestMethod.GET )
    public ResponseEntity<Specifier<TaskFailure>> getErrors( @PathVariable String type, @PathVariable String id,
                                                             @RequestHeader( "DN" ) String dn,
                                                             @RequestHeader( "WId" ) String wid ) {

        HttpStatus hs = HttpStatus.NOT_FOUND;
        Specifier<TaskFailure> spec = null;
        try {
            spec = createTaskSpecifier( TaskFailure.class, type, id, "errors" );
            ResponseEntity<TaskFailure> res = taskClient.getErrors( spec.getUriMap().get( "taskId" ), dn, wid );
            if ( res.getStatusCode() == HttpStatus.OK ) {
                spec.setPayload( res.getBody() );
                hs = HttpStatus.OK;
            } else spec = null;
        } catch ( NoSuchResourceException e ) {
            // intentionally
        }
        return new ResponseEntity<Specifier<TaskFailure>>( spec, getHeader( type, id, "result", dn, wid ), hs );
    }


    protected GNDMSResponseHeader getHeader( String type, String id, String facet, String dn, String wid ) {

        Map<String,String> uriargs = new HashMap<String, String>( 2 );
        uriargs.put( UriFactory.TASKFLOW_ID, id );
        uriargs.put( UriFactory.TASKFLOW_TYPE, type );
        uriargs.put( UriFactory.SERVICE, "gorfx" );

        return new GNDMSResponseHeader( uriFactory.taskFlowTypeUri( uriargs, facet ), facet, serviceUrl, dn, wid );
    }


    private HashMap<String, String> taskUriMap( String type, String id, Task t ) {
        HashMap<String,String> urimap = new HashMap<String, String>( 4 );
        urimap.put( UriFactory.SERVICE, "gorfx" );
        urimap.put( UriFactory.TASKFLOW_TYPE, type );
        urimap.put( UriFactory.TASKFLOW_ID, id );
        urimap.put( UriFactory.TASK_ID, t.getId() );
        return urimap;
    }


    private <T> Specifier<T> createTaskSpecifier( Class<T> resClass,  String type, String id, String facet ) throws NoSuchResourceException {

        TaskFlow tf = findTF( type, id );
        Task t = tf.getTask();
        HashMap<String, String> urimap = taskUriMap( type, id, t );
        Specifier<T> spec;
        if( t != null ) {
            spec = new Specifier<T>();
            spec.setURL( uriFactory.taskUri( urimap, facet ) );
            spec.setUriMap( urimap );
        } else
            throw new NoSuchResourceException( "task", urimap );

        return spec;
    }


    protected TaskFlow findTF( String type, String id ) throws NoSuchResourceException {

        if( taskFlowProvider.exists( type ) ) {
            TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
            TaskFlow tf = tff.find( id );
            if ( tf != null )
                return tf;
        }

        throw new NoSuchResourceException( );
    }


    public TFExecutor getExecutorService() {
        return executorService;
    }


    @Autowired
    public void setExecutorService( TFExecutor executorService ) {
        this.executorService = executorService;
    }


    public void setServiceUrl( String serviceUrl ) {
        this.serviceUrl = serviceUrl;
    }


    @Autowired
    public void setTaskFlowProvider( TaskFlowProvider taskFlowProvider ) {
        this.taskFlowProvider = taskFlowProvider;
    }


    @Autowired
    public void setTaskClient( TaskClient taskClient ) {
        this.taskClient = taskClient;
    }
}
