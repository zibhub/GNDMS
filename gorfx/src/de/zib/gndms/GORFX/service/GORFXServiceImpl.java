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


import de.zib.gndms.kit.config.ActionMeta;
import de.zib.gndms.kit.config.ConfigActionProvider;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import de.zib.gndms.kit.config.ConfigMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *
 *          Date: 26.01.11, Time: 12:29
 *
 * Controller class for the gorfx service.
 *
 * For detailed method documentation @see GORFXService.
 */
@Controller
@RequestMapping( value = "/gorfx" )
public class GORFXServiceImpl implements GORFXService {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
    private Facets gorfxFacets; ///< List of facets under /gorfx/
    private String baseUrl; ///< The base url something like: \c http://my.host.org/gndms/grid_id
    private ConfigActionProvider configProvider; ///< List of config actions, todo uncertain who provided these.

    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ResponseEntity<Facets> listAvailableFacets( @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx" );
        responseHeaders.setParentURL( baseUrl );
        if( dn != null ) responseHeaders.setDN( dn );

        return new ResponseEntity<Facets>( gorfxFacets, responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/config", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listConfigActions( @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "config" ).getUrl() );
        responseHeaders.setParentURL( baseUrl );
        if( dn != null ) responseHeaders.setDN( dn );

        return new ResponseEntity<List<String>>( configProvider.listAvailableActions(), responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/config/{actionName}", method = RequestMethod.GET )
    public ResponseEntity<ConfigMeta> getContigActionInfo( @PathVariable String actionName,
                                                           @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( gorfxFacets.findFacet( "config" ).getUrl() );
        responseHeaders.setParentURL( baseUrl + "/gorfx" );
        if( dn != null ) responseHeaders.setDN( dn );
        return null;
    }


    @RequestMapping( value = "/config/{actionName}", method = RequestMethod.POST )
    public ResponseEntity<String> callConfigAction( @PathVariable String actionName,
                                                    @RequestBody String args, @RequestHeader( "DN" ) String dn ) {
        // Fires a config action
        return null;
    }


    @RequestMapping( value = "/batch", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listBatchActions( String dn ) {
        // Fires a batch action
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "batch" ).getUrl() );
        responseHeaders.setParentURL( baseUrl );
        return null;
    }

    @RequestMapping( value = "/batch/{actionName}", method = RequestMethod.GET )
    public ResponseEntity<ActionMeta> getBatchActionInfo( @PathVariable String actionName,
                                                          @RequestHeader( "DN" ) String dn ) {
        // delivers info to the action
        return null;
    }

    @RequestMapping( value = "/batch/{actionName}", method = RequestMethod.POST )
    public ResponseEntity<String> callBatchAction( @PathVariable String actionName, @RequestBody String args,
                                                   @RequestHeader( "DN" ) String dn ) {
        // Fires a batch action
        return null;
    }



    @RequestMapping( value = "/taskflows/", method = RequestMethod.GET )
    public ResponseEntity<List<String>> listTaskFlows( @RequestHeader( "DN" ) String dn ) {
        // list possible task flows
        return null;
    }


    @RequestMapping( value = "/taskflows/{type}", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowInfo> getTaskFlowInfo( @PathVariable String type, @RequestHeader( "DN" ) String dn ) {
        return null;  // not required here
    }


    @RequestMapping( value = "/taskflows/{type}", method = RequestMethod.POST )
    public ResponseEntity<String> createTaskFlow( @PathVariable String type, AbstractTF order, @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {
        return null;  // not required here
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


    public ConfigActionProvider getConfigProvider() {
        return configProvider;
    }


    @Autowired
    public void setConfigProvider( ConfigActionProvider configProvider ) {
        this.configProvider = configProvider;
    }
}

