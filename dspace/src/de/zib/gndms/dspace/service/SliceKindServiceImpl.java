package de.zib.gndms.dspace.service;

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

import de.zib.gndms.common.dspace.service.SliceKindService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.dspace.service.utils.UnauthorizedException;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceKindConfiguration;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * The slice kind service implementation.
 *
 * @author Ulrike Golas
 */

@Controller
@RequestMapping( value = "/dspace" )
public class SliceKindServiceImpl implements SliceKindService {
    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger( this.getClass() );
    /**
     * The entity manager factory.
     */
    private EntityManagerFactory emf;

    /**
     * The base url, something like \c http://my.host.org/gndms/grid_id.
     */
    private String baseUrl;
    /**
     * All available subspaces.
     */
    private SubspaceProvider subspaceProvider;

    @Inject
    public void setSliceKindProvider( SliceKindProvider sliceKindProvider ) {
        this.sliceKindProvider = sliceKindProvider;
    }


    public SliceKindProvider getSliceKindProvider( ) {
        return sliceKindProvider;
    }

    /**
     * All available slice kinds.
     */
    private SliceKindProvider sliceKindProvider;

    public void setUriFactory( UriFactory uriFactory ) {
        this.uriFactory = uriFactory;
    }

    /**
     * The uri factory.
     */
    private UriFactory uriFactory;

    // TODO: initialization of subspaceProvider

    /**
     * Initialization of the slice kind service.
     */
    @PostConstruct
    public final void init() {
        setUriFactory( new UriFactory() );
    }

    @Override
    // @RequestMapping( value = "/_{subspace}/_{sliceKind}", method = RequestMethod.GET )
    // delegated by SubspaceService
    public final ResponseEntity<Configuration> getSliceKindInfo(
            @PathVariable final String subspace,
            @PathVariable final String sliceKind,
            @RequestHeader( "DN" ) final String dn ) {
        GNDMSResponseHeader headers = getResponseHeaders( subspace, sliceKind, dn );

        try {
            SliceKind sliceK = getSliceKindProvider().get( subspace, sliceKind );
            SliceKindConfiguration config = SliceKindConfiguration.getSliceKindConfiguration( sliceK );
            return new ResponseEntity<Configuration>( config, headers,
                                                      HttpStatus.OK );
        }
        catch( NoSuchElementException ne ) {
            logger.warn( "The slice kind " + sliceKind + "does not exist within the subspace" + subspace + "." );
            return new ResponseEntity<Configuration>( null, headers,
                                                      HttpStatus.NOT_FOUND );
        }
    }


    @Override
    @RequestMapping( value = "/_{subspace}/_{sliceKind}/config", method = RequestMethod.GET )
    @Secured( "ROLE_USER" )
    public final ResponseEntity<Configuration> getSliceKindConfig(  @PathVariable final String subspace,
                                                                    @PathVariable final String sliceKind,
                                                                    @RequestHeader( "DN" ) final String dn )
    {
        // todo implement me, pretty please!!!
        return new ResponseEntity<Configuration>( null, new GNDMSResponseHeader(),
                HttpStatus.NOT_IMPLEMENTED );
    }


    @Override
    @RequestMapping( value = "/_{subspace}/_{sliceKind}/config", method = RequestMethod.POST )
    @Secured( "ROLE_ADMIN" )
    public final ResponseEntity<Void> setSliceKindConfig( @PathVariable final String subspace,
                                                          @PathVariable final String sliceKind, final Configuration config, final String dn ) {
        GNDMSResponseHeader headers = getResponseHeaders( subspace, sliceKind, dn );

        try {
            SliceKind sliceK = sliceKindProvider.get( subspace, sliceKind );
            SliceKindConfiguration sliceKindConfig = SliceKindConfiguration.checkSliceKindConfig( config );

            sliceK.setPermission( sliceKindConfig.getPermission() );

            // TODO: sliceK.setSliceKindConfiguration(sliceKindConfig)

            Specifier<Void> spec = new Specifier<Void>();

            HashMap<String, String> urimap = new HashMap<String, String>( 2 );
            urimap.put( "service", "dspace" );
            urimap.put( UriFactory.SUBSPACE, subspace );
            urimap.put( UriFactory.SLICE_KIND, sliceKind );
            spec.setUriMap( new HashMap<String, String>( urimap ) );
            spec.setUrl( uriFactory.quoteUri( urimap ) );

            return new ResponseEntity<Void>( null, headers,
                                             HttpStatus.OK );
        }
        catch( NoSuchElementException ne ) {
            logger.warn( "The slice kind " + sliceKind + "does not exist within the subspace" + subspace );
            return new ResponseEntity<Void>( null, headers,
                                             HttpStatus.NOT_FOUND );
        }
        catch( WrongConfigurationException e ) {
            logger.warn( "Wrong slice kind configuration" );
            return new ResponseEntity<Void>( null, headers,
                                             HttpStatus.BAD_REQUEST );
        }

    }

    @Override
    // delegated by SubspaceService
    public final ResponseEntity<Specifier<Void>> deleteSliceKind(
            @PathVariable final String subspace,
            @PathVariable final String sliceKind,
            @RequestHeader( "DN" ) final String dn ) {
        GNDMSResponseHeader headers = getResponseHeaders( subspace, sliceKind, dn );
        try {
            SliceKind sliceK = sliceKindProvider.get( subspace, sliceKind );
            Subspace sub = subspaceProvider.get( subspace );

            // TODO: AssignSliceKindAction zum lschen
            return new ResponseEntity<Specifier<Void>>( null, headers, HttpStatus.OK );
        }
        catch( NoSuchElementException ne ) {
            logger.warn( "The slice kind " + sliceKind + "does not exist within the subspace" + subspace );
            return new ResponseEntity<Specifier<Void>>( null, headers,
                                                        HttpStatus.NOT_FOUND );
        }
    }



    /**
     * Sets the GNDMS response header for a given subspace, slice kind and dn
     * using the base URL.
     *
     * @param subspace  The subspace id.
     * @param sliceKind The slice kind id.
     * @param dn        The dn.
     * @return The response header for this subspace.
     */
    private GNDMSResponseHeader getResponseHeaders( final String subspace,
                                                    final String sliceKind, final String dn ) {
        GNDMSResponseHeader headers = new GNDMSResponseHeader();
        headers.setResourceURL( baseUrl + "/dspace/_" + subspace + "/_"
                                        + sliceKind );
        headers.setParentURL( baseUrl + "/dspace/_" + subspace );
        if( dn != null ) {
            headers.setDN( dn );
        }
        return headers;
    }

    /**
     * Returns the base url of this slice kind service.
     *
     * @return the baseUrl
     */
    public final String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url of this slice kind service.
     *
     * @param baseUrl the baseUrl to set
     */
    public final void setBaseUrl( final String baseUrl ) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns the subspace provider of this slice kind service.
     *
     * @return the subspaceProvider
     */
    public final SubspaceProvider getSubspaceProvider() {
        return subspaceProvider;
    }

    /**
     * Sets the subspace provider of this slice kind service.
     *
     * @param subspaceProvider the subspaceProvider to set
     */
    public final void setSubspaceProvider( final SubspaceProvider subspaceProvider ) {
        this.subspaceProvider = subspaceProvider;
    }

    /**
     * Returns the entity manager factory.
     *
     * @return the factory.
     */
    public final EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * Sets the entity manager factory.
     *
     * @param emf the factory to set.
     */
    @PersistenceUnit
    public final void setEmf( final EntityManagerFactory emf ) {
        this.emf = emf;
    }


    @ExceptionHandler( NoSuchResourceException.class )
    public ResponseEntity<Void> handleNoSuchResourceException(
            NoSuchResourceException ex,
            HttpServletResponse response )
            throws IOException
    {
        logger.debug("handling exception for: " + ex.getMessage());
        response.setStatus( HttpStatus.NOT_FOUND.value() );
        response.sendError( HttpStatus.NOT_FOUND.value() );
        return new ResponseEntity<Void>( null, getResponseHeaders(ex.getMessage(), null, null), HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler( UnauthorizedException.class )
    public ResponseEntity<Void> handleUnAuthorizedException(
            UnauthorizedException ex,
            HttpServletResponse response )
            throws IOException
    {
        logger.debug( "handling exception for: " + ex.getMessage() );
        response.setStatus( HttpStatus.UNAUTHORIZED.value() );
        response.sendError(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<Void>( null, getResponseHeaders(ex.getMessage(), null, null), HttpStatus.UNAUTHORIZED );
    }
}
