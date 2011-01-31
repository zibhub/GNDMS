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


import com.sun.istack.internal.NotNull;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.rest.Facet;
import de.zib.gndms.gritserv.rest.Facets;
import de.zib.gndms.gritserv.rest.GNDMSResponseHeader;
import de.zib.gndms.gritserv.typecon.util.AxisTypeFromToXML;
import de.zib.gndms.gritserv.typecon.util.ContextTAux;
import de.zib.gndms.gritserv.util.LogAux;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.WSMaintenance;
import de.zib.gndms.kit.config.ConfigMeta;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.model.gorfx.repository.ORQDao;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.List;


/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *
 *          Date: 26.01.11, Time: 12:29
 *
 * Controller class for the gorfx service.
 */
@Controller
@RequestMapping( value = "/gorfx" )
public class GORFXService {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );
    private Facets gorfxFacets; ///< List of facets under /gorfx/
    private String baseUrl; ///< The base url something like: \c http://my.host.org/gndms/grid_id

    private List<String> configActions; ///< List of config actions, todo uncertain who provided these.


    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ResponseEntity<Facets> listAvailableFacets( @RequestHeader( "DN" ) String dn ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx" );
        responseHeaders.setFacetURL( baseUrl + "/gorfx" );
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

        return new ResponseEntity<List<String>>( configActions, responseHeaders, HttpStatus.OK );
    }


    @RequestMapping( value = "/config/{actionName}", method = RequestMethod.GET )
    public ResponseEntity<ConfigMeta> getContigActionInfo( @PathVariable String actionName,
                                                           @RequestHeader( "DN" ) String dn  ) {

        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/config" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "config" ).getUrl() );
        responseHeaders.setParentURL( baseUrl + "/gorfx" );
        if( dn != null ) responseHeaders.setDN( dn );
        return null;
    }


    @RequestMapping( value = "/config/{actionName}", method = RequestMethod.POST )
    public ModelAndView callConfigAction( @PathVariable String actionName,
                                          @RequestHeader( "DN" ) String dn,
                                          @RequestBody String args ) {
        // Fires a config action
        return null;
    }


    @RequestMapping( value = "/batch", method = RequestMethod.GET )
    public ModelAndView listActions( ) {
        // Fires a batch action
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        responseHeaders.setResourceURL( baseUrl + "/gorfx/batch" );
        responseHeaders.setFacetURL( gorfxFacets.findFacet( "batch" ).getUrl() );
        responseHeaders.setParentURL( baseUrl );
        if( dn != null ) responseHeaders.setDN( dn );
        return null;
    }

    @RequestMapping( value = "/batch/{actionName}", method = RequestMethod.GET )
    public ModelAndView getBatchActionInfo( @PathVariable String actionName ) {
        // delivers info to the action
        return null;
    }

    @RequestMapping( value = "/batch/{actionName}", method = RequestMethod.POST )
    public ModelAndView callBatchAction( @PathVariable String actionName, @RequestBody String args ) {
        // Fires a batch action
        return null;
    }



    @RequestMapping( value = "/taskflows/", method = RequestMethod.GET )
    public ModelAndView listTaskFlows( @RequestHeader( "DN" ) String dn, @RequestHeader( "wid" ) String wid ) {
        // list possible task flows
        return null;
    }


    @RequestMapping( value = "/taskflows/{kind}", method = RequestMethod.GET )
    public ModelAndView createTaskFlow( @PathVariable String kind ) {
        // could return id of foo
        return null;
    }



/*

    @RequestMapping( value = "/GORFX/createORQ", method = RequestMethod.POST)
    public View createOfferRequest(@RequestBody AbstractORQ orq,
            @RequestParam( value = "wid", required = false )  String wid ) {

        try{
            ContextTAux.initWid(getSystem().getModelUUIDGen(), wid);
            LogAux.logSecInfo( logger, "createOfferRequest" );

            @NotNull ExtORQResourceHome home = getORQResourceHome();
            @NotNull ResourceKey key = home.createResource();
            ORQResource orqr = (ORQResource) home.find( key );
            orqr.setCachedWid(WidAux.getWid());

            EndpointReferenceType et = DelegationAux.extractDelegationEPR( context );
            orqr.setDelegateEPR( et );
            orqr.setOfferRequestArguments( offerRequestArguments, context);

	        StringWriter wr = new StringWriter();
	        AxisTypeFromToXML.toXML(wr, offerRequestArguments, false, true);
            wr.write( "\nassigned GORFXId: " + orqr.getID() );
	        logger.info("ORQ is: " + wr.toString());

            return home.getResourceReference( key ).getEndpointReference();
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new RemoteException( e.getMessage(), e );
        }
        finally {
            WidAux.removeWid();
        }
    }


    public org.apache.axis.types.URI[] getSupportedOfferTypes(types.ContextT context) throws RemoteException {

        try{
            ContextTAux.initWid(getSystem().getModelUUIDGen(), context);
            return getResourceHome().getAddressedResource().getSupportedOfferTypes( );
        } catch ( Exception e ) {
            throw new RemoteException(e.getMessage(), e);
        }
        finally {
            WidAux.removeWid();
        }
    }


    public java.lang.Object callMaintenanceAction(java.lang.String action, types.ContextT context) throws RemoteException {
        
        logger.debug( "called with: " + action );
        try {
            ContextTAux.initWid(getSystem().getModelUUIDGen(), context);

                if( maintenance == null  )
                    maintenance = new WSMaintenance( system );

                return maintenance.callMaintenenceAction( action );
        } catch ( Exception e ) {
            logger.warn( e.getMessage(), e );
            throw new RemoteException( e.getMessage(), e );
        }
        finally {
            WidAux.removeWid();
        }
    }


    public @NotNull GNDMSystem getSystem() {
        return system;
    }


    @Autowired
    public void setSystem( GNDMSystem system ) {
        this.system = system;
    }
    */
}

