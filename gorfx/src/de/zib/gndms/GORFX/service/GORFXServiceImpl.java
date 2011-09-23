package de.zib.gndms.GORFX.service;

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


import de.zib.gndms.common.GORFX.service.GORFXService;
import de.zib.gndms.common.logic.action.ActionMeta;
import de.zib.gndms.common.logic.config.ConfigMeta;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.infra.legacy.LegacyConfigActionProvider;
import de.zib.gndms.infra.legacy.SettableConfigMeta;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.logic.action.ActionProvider;
import de.zib.gndms.logic.action.NoSuchActionException;
import de.zib.gndms.logic.model.config.ConfigActionProvider;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowFactory;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowProvider;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author try ma ik jo rr a zib
 * @date: 26.01.11, 12:29
 *
 * @brief Controller class for the gorfx service.
 *
 * For detailed method documentation @see GORFXService.
 */
@Controller
@RequestMapping( value = "/gorfx" )
public class GORFXServiceImpl implements GORFXService {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
    private Facets gorfxFacets; ///< List of facets under /gorfx/
    private String baseUrl; ///< The base url something like: \c http://my.host.org/gndms/grid_id
    private ActionProvider configProvider; ///< List of config actions
    private TaskFlowProvider taskFlowProvider; ///< List of config actions
    private TaskFlowClient taskFlowClient;
    private UriFactory uriFactory;
    private ConfigActionProvider configActionProvider;


    @PostConstruct
    public void init( ) {
        taskFlowClient.setServiceURL( baseUrl );
        uriFactory = new UriFactory( baseUrl );
    }


    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ResponseEntity<Facets> listAvailableFacets( @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/" );
        responseHeaders.setParentURL( baseUrl );
        if( dn != null ) responseHeaders.setDN( dn );

        return new ResponseEntity<Facets>( gorfxFacets, responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/config", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listConfigActions( @RequestHeader( value="DN", required=false ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "config" ).getUrl() );
        responseHeaders.setParentURL( baseUrl );
        if( dn != null ) responseHeaders.setDN( dn );

        return new ResponseEntity<List<String>>( configProvider.listAvailableActions(), responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/config/_{actionName}", method = RequestMethod.GET )
    public ResponseEntity<ConfigMeta> getConfigActionInfo( @PathVariable String actionName,
                                                           @RequestHeader( value="DN", required=false ) String dn ) {

        HttpStatus hs = HttpStatus.NOT_FOUND ;
        GNDMSResponseHeader responseHeaders =
            new GNDMSResponseHeader( gorfxFacets.findFacet( "config" ).getUrl(), null, baseUrl + "/gorfx/", dn, null );

        ConfigMeta configMeta = null;
        try {
            configMeta = configActionProvider.getMeta( actionName );
            hs = HttpStatus.OK;
        } catch ( NoSuchActionException e ) {
            logger.info( "", e );
        }

        return new ResponseEntity<ConfigMeta>( configMeta, responseHeaders, hs );
    }


    @RequestMapping( value = "/config/_{actionName}", method = RequestMethod.POST )
    public ResponseEntity<String> callConfigAction( @PathVariable String actionName,
                                                    @RequestBody String args, @RequestHeader( "DN" ) String dn ) {
        GNDMSResponseHeader responseHeaders =
            new GNDMSResponseHeader( gorfxFacets.findFacet( "config" ).getUrl(), null, baseUrl + "/gorfx/", dn, null );
        configProvider.getAction( actionName );
        return null;
    }


    @RequestMapping( value = "/batch", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listBatchActions( String dn ) {
        // Fires a batch action
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "batch" ).getUrl() );
        responseHeaders.setParentURL( baseUrl );
        return new ResponseEntity<List<String>>( new ArrayList<String>( 1 ) {{ add( "mockup" ); }},
            responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/batch/_{actionName}", method = RequestMethod.GET )
    public ResponseEntity<ActionMeta> getBatchActionInfo( @PathVariable String actionName,
                                                          @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader headers = new GNDMSResponseHeader();
        headers.setResourceURL( gorfxFacets.findFacet( "batch" ).getUrl() );
        headers.setParentURL( baseUrl + "/gorfx/" );
        if( dn != null ) headers.setDN( dn );

        if (! actionName.equals( "mockup" ) )
            return new ResponseEntity<ActionMeta>( null, headers, HttpStatus.NOT_FOUND );

        return new ResponseEntity<ActionMeta>(
            new ActionMeta() {
                public String getName() {
                    return "mockup";
                }
                public String getHelp() {
                    return "Sorry, I can't even help myself";
                }
                public String getDescription() {
                    return "I just mock you";
                }


                public Action getAction() {
                    return null;  // not required here
                }
            }, headers, HttpStatus.OK );
    }


    @RequestMapping( value = "/batch/_{actionName}/_{id}", method = RequestMethod.GET )
    public ResponseEntity<Specifier<Facets>> getBatchAction( @PathVariable String actionName, @PathVariable String id,
                                                             @RequestHeader String dn ) {
        return null;  // not required here
    }


    @RequestMapping( value = "/batch/_{actionName}", method = RequestMethod.POST )
    public ResponseEntity<Specifier> callBatchAction( @PathVariable String actionName, @RequestBody String args,
                                                   @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader headers =
            new GNDMSResponseHeader( gorfxFacets.findFacet( "batch" ).getUrl(), null, baseUrl + "/gorfx/", dn, null );

        if (! actionName.equals( "mockup" ) )
            return new ResponseEntity<Specifier>( null, headers, HttpStatus.NOT_FOUND );

        Specifier<String> res = new Specifier<String>();
        res.addMapping( "actionName", actionName );
        res.setURL( baseUrl + "/batch/_" + actionName );
        res.setPayload( "Feeling lucky?" );

        return (ResponseEntity<Specifier>) (Object) new ResponseEntity<Specifier<String>>( res, headers, HttpStatus.OK );
    }



    @RequestMapping( value = "/taskflows/", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listTaskFlows( @RequestHeader( "DN" ) String dn ) {

        if( taskFlowProvider == null )
            throw new IllegalStateException( "provider is null" );

        GNDMSResponseHeader responseHeaders =
            new GNDMSResponseHeader( baseUrl + "/gorfx/", gorfxFacets.findFacet( "taskflows" ).getUrl(), baseUrl, dn, null );

        return new ResponseEntity<List<String>>( taskFlowProvider.listTaskFlows(), responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/_{type}", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowInfo> getTaskFlowInfo( @PathVariable String type, @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader headers = new GNDMSResponseHeader(
            gorfxFacets.findFacet( "taskflows" ).getUrl(), null, baseUrl + "/gorfx/", dn, null );

        if(! taskFlowProvider.exists( type  ) )
            return new ResponseEntity<TaskFlowInfo>( null, headers, HttpStatus.NOT_FOUND );

        taskFlowProvider.getTaskFlowInfo( type );

        return new ResponseEntity<TaskFlowInfo>( taskFlowProvider.getTaskFlowInfo( type ), headers, HttpStatus.OK );

    }


    @RequestMapping( value = "/_{type}", method = RequestMethod.POST )
    public ResponseEntity<Specifier<Facets>> createTaskFlow( @PathVariable String type, @RequestBody Order order,
                                                  @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {

        GNDMSResponseHeader headers = new GNDMSResponseHeader(
            gorfxFacets.findFacet( "taskflows" ).getUrl(), null, baseUrl + "/gorfx/", dn, wid );

        if(! taskFlowProvider.exists( type  ) )
            return new ResponseEntity<Specifier<Facets>>( null, headers, HttpStatus.NOT_FOUND );

        TaskFlowFactory tff = taskFlowProvider.getFactoryForTaskFlow( type );
        TaskFlow tf = tff.create();
        TaskFlowServiceAux.setOrderAsDelegate( order, tf, tff );
        Specifier<Facets> spec = new Specifier<Facets>();
        spec.addMapping( "id", tf.getId() );
        spec.addMapping( "type", type );
        HashMap<String,String> hm = new HashMap<String, String>( spec.getUriMap() );
        hm.put( "service", "gorfx" );
        spec.setURL( uriFactory.taskFlowUri( hm, null )  );

        ResponseEntity<Facets> re = taskFlowClient.getFacets( type, tf.getId(), dn );
        spec.setPayload( re.getBody() );

        return new ResponseEntity<Specifier<Facets>>( spec, headers, HttpStatus.CREATED );
    }


    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
    }


    public String getBaseUrl() {
        return baseUrl;
    }


    public void setGorfxFacets( Facets gorfxFacets ) {
        this.gorfxFacets = gorfxFacets;
    }


    public Facets getGorfxFacets() {
        return gorfxFacets;
    }


    public ActionProvider getConfigProvider() {
        return configProvider;
    }


    @Autowired
    public void setConfigProvider( ConfigActionProvider configProvider ) {
        this.configProvider = configProvider;
    }


    @Autowired
    public void setTaskFlowProvider( TaskFlowProvider taskFlowProvider ) {
        logger.debug( "Taskflow provider injected: " + taskFlowProvider.getClass().getName() );
        this.taskFlowProvider = taskFlowProvider;
    }


    @Autowired
    public void setTaskFlowClient( TaskFlowClient taskFlowClient ) {
        this.taskFlowClient = taskFlowClient;
    }
}

