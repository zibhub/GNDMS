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

package de.zib.gndms.dspace.service;

import de.zib.gndms.common.dspace.service.SubspaceService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.model.dspace.*;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;

@Controller
@RequestMapping(value = "/dspace")
public class SubspaceServiceImpl implements SubspaceService {
    private final Logger logger = LoggerFactory.getLogger( this.getClass() );
	private EntityManagerFactory emf;
	private EntityManager em;
	private String baseUrl;
	private SubspaceProvider subspaceProvider;
    private SliceKindProvider slicekindProvider;
    private SliceProvider sliceProvider;

	private UriFactory uriFactory;
	private Facets subspaceFacets;



	@PostConstruct
	public final void init() {
        setUriFactory( new UriFactory() );
	}

    public void setUriFactory(UriFactory uriFactory) {
        this.uriFactory = uriFactory;
    }

	@Override
	@RequestMapping( value = "/_{subspace}", method = RequestMethod.GET )
	public final ResponseEntity<Facets> listAvailableFacets(
			@PathVariable final String subspace,
			@RequestHeader( "DN" ) final String dn ) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if( subspaceProvider.exists( subspace ) ) {
			return new ResponseEntity< Facets >( subspaceFacets, headers, HttpStatus.OK );
		}
		logger.info("Illegal Access: subspace " + subspace + " not found");
		return new ResponseEntity< Facets >(null, headers, HttpStatus.NOT_FOUND);
	}

	@Override
	@RequestMapping( value = "/_{subspace}", method = RequestMethod.PUT )
    public final ResponseEntity< Facets > createSubspace(
            @PathVariable final String subspace,
            @RequestBody final String config,
            @RequestHeader( "DN" ) final String dn) {

        GNDMSResponseHeader headers = setSubspaceHeaders( subspace, dn );

        if( subspaceProvider.exists( subspace ) ) {
            logger.info("Subspace " + subspace + " cannot be created because it already exists.");
            return new ResponseEntity< Facets >( null, headers, HttpStatus.FORBIDDEN );
        }

        // TODO: catch creation errors and return appropriate HttpStatus
        logger.info( "Creating supspace " + subspace + "." );
        subspaceProvider.create( subspace, config );

        return new ResponseEntity< Facets >( subspaceFacets, headers, HttpStatus.CREATED );
	}

	@Override
	@RequestMapping( value = "/_{subspace}", method = RequestMethod.DELETE )
	public final ResponseEntity< Specifier< Void > > deleteSubspace(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaceProvider.exists(subspace)) {
			logger.warn( "Subspace " + subspace + " not found" );
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		}

	   	em = emf.createEntityManager();
       	TxFrame tx = new TxFrame(em);
       	try {

       		DeleteSubspaceAction action = new DeleteSubspaceAction();
       		action.setPath(subspaceProvider.get(subspace).getPath() );
       		action.setMode( SetupMode.DELETE );
       		action.setOwnEntityManager( em );
       		
       		logger.info("Calling action for deleting the supspace " + subspace
       				+ ".");
       		action.call();
       		tx.commit();

			Specifier<Void> spec = new Specifier<Void>();
       		// TODO get the task specifier from the action - something like this:
//			Taskling task = new Taskling(action.getDao(), action.nextUUID());
//			HashMap<String, String> urimap = new HashMap<String, String>(2);
//			urimap.put(UriFactory.SERVICE, "dspace");
//			urimap.put(UriFactory.TASK_ID, task.getId());
//			spec.setUriMap(new HashMap<String, String>(urimap));
     		
			return new ResponseEntity<Specifier<Void>>(spec, headers, HttpStatus.OK);
       	} finally {
       		tx.finish();
       		if (em != null && em.isOpen()) {
       			em.close();
       		}
       	}
	}

	@Override
	@RequestMapping( value = "/_{subspace}/slicekinds", method = RequestMethod.GET )
	public final ResponseEntity<List<Specifier<Void>>> listSliceKinds(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if ( !subspaceProvider.exists( subspace ) ) {
			logger.info( "Illegal Access: subspace " + subspace + " not found" );
			return new ResponseEntity<List<Specifier<Void>>>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		try {
            List< SliceKind > sliceKinds = this.slicekindProvider.list( subspace );
            List<Specifier<Void>> list = new ArrayList<Specifier<Void>>( sliceKinds.size() );
            HashMap<String, String> urimap = new HashMap<String, String>(2);
            urimap.put( UriFactory.SERVICE, "dspace" );
            for( SliceKind sk : sliceKinds ) {
                Specifier<Void> spec = new Specifier<Void>();
                spec.setUriMap(new HashMap<String, String>(urimap));
                spec.addMapping( UriFactory.SLICEKIND, sk.getId() );
                // TODO does the String have to be hard-coded?
                spec.setUrl(uriFactory.subspaceUri(urimap, "slicekinds"));
                list.add(spec);
            }

            return new ResponseEntity<List<Specifier<Void>>>(list, headers,
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.warn("Subspace " + subspace + " not found");
            return new ResponseEntity<List<Specifier<Void>>>(null, headers,
                    HttpStatus.NOT_FOUND);

        }
	}

    @Override
    @RequestMapping( value = "/_{subspace}/_{slicekind}", method = RequestMethod.PUT )
    public final ResponseEntity<List<Specifier<Void>>> createSliceKind(
            @PathVariable final String subspace,
            @PathVariable final String slicekind,
            final String config,
            @RequestHeader("DN") final String dn) {
        GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

        if ( !subspaceProvider.exists( subspace ) ) {
            logger.info("Illegal Access: subspace " + subspace + " not found");
            return new ResponseEntity<List<Specifier<Void>>>(null, headers,
                    HttpStatus.NOT_FOUND);
        }
        if( slicekindProvider.exists( subspace, slicekind ) ) {
            logger.info("Illegal Access: slicekind " + slicekind + " could not be created because it already exists.");
            return new ResponseEntity<List<Specifier<Void>>>(null, headers,
                    HttpStatus.BAD_REQUEST);
        }

        slicekindProvider.create( slicekind, "subspace:" + subspace + "; " + config );

        return new ResponseEntity<List<Specifier<Void>>>(null, headers,
                                                         HttpStatus.OK);
    }

    @Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.GET)
	public final ResponseEntity<Configuration> listSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);
		
		if (!subspaceProvider.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<Configuration>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		Subspace sub = subspaceProvider.get( subspace );
    	SubspaceConfiguration config = SubspaceConfiguration.getSubspaceConfiguration(sub);
		return new ResponseEntity<Configuration>(config, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		try {
			SubspaceConfiguration subspaceConfig = SubspaceConfiguration.checkSubspaceConfig(config);

			if (subspaceProvider.exists(subspace)
					|| subspaceConfig.getMode() != SetupMode.UPDATE) {
				logger.warn("Subspace " + subspace + " cannot be updated");
				return new ResponseEntity<Void>(null, headers,
						HttpStatus.FORBIDDEN);
			}
		   	em = emf.createEntityManager();
	       	TxFrame tx = new TxFrame(em);

	       	try {

	       		SetupSubspaceAction action = new SetupSubspaceAction(subspaceConfig);
	       		action.setOwnEntityManager(em);
	       		logger.info("Calling action for updating the supspace "
					+ subspace + ".");

	       		action.call();
	      	} finally {
	       		tx.finish();
	       		if (em != null && em.isOpen()) {
	       			em.close();
	       		}
	       	}
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.CREATED);
		} catch (WrongConfigurationException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
	}

    @Override
    @RequestMapping( value = "/_{subspace}/_{sliceKind}", method = RequestMethod.POST )
    public final ResponseEntity< Specifier< Void > > createSlice(
            @PathVariable final String subspace,
            @PathVariable final String sliceKind,
            @RequestBody final String config,
            @RequestHeader( "DN" ) final String dn ) {
        GNDMSResponseHeader headers = setHeaders( subspace, sliceKind, dn );

        try {
            Map< String, String > parameters = new HashMap< String, String >( );
            ParameterTools.parseParameters( parameters, config, null );
            if( !parameters.containsKey( "deadline" ) )
                throw new WrongConfigurationException( "Missing configuration option" );
            if( !parameters.containsKey( "sliceSize" ) )
                throw new WrongConfigurationException( "Missing configuration option" );

            // use provider to create slice
            DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
            DateTime deadline = fmt.parseDateTime( parameters.get( "deadline" ) );
            String slice = sliceProvider.createSlice( subspace, sliceKind, dn, deadline.toGregorianCalendar(), Long.parseLong( parameters.get( "sliceSize" ) ) );

            // generate specifier and return it
            Specifier<Void> spec = new Specifier<Void>();

            HashMap<String, String> urimap = new HashMap<String, String>( 2 );
            urimap.put( "service", "dspace" );
            urimap.put( UriFactory.SUBSPACE, subspace );
            urimap.put( UriFactory.SLICEKIND, sliceKind );
            urimap.put( UriFactory.SLICE, slice );
            spec.setUriMap( new HashMap<String, String>( urimap ) );
            spec.setUrl( uriFactory.sliceUri( urimap, null ) );

            return new ResponseEntity< Specifier< Void > >( spec, headers,
                                                        HttpStatus.OK );
        }
        catch( WrongConfigurationException e ) {
            logger.warn( e.getMessage() );
            return new ResponseEntity<Specifier<Void>>( null, headers,
                                                        HttpStatus.BAD_REQUEST );
        }
        catch( NoSuchElementException e ) {
            logger.warn( e.getMessage() );
            return new ResponseEntity<Specifier<Void>>( null, headers,
                                                        HttpStatus.NOT_FOUND );
        }
        catch( ParameterTools.ParameterParseException e ) {
            logger.info( "Illegal request: Could not parse paramter string \"" + ParameterTools.escape( config ) + "\". " + e.getMessage() );
            return new ResponseEntity<Specifier<Void>>( null, headers,
                                                        HttpStatus.BAD_REQUEST );
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
    private GNDMSResponseHeader setHeaders( final String subspace,
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
	 * Sets the GNDMS response header for a given subspace and dn using the base
	 * URL.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param dn
	 *            The dn.
	 * @return The response header for this subspace.
	 */
	private GNDMSResponseHeader setSubspaceHeaders(final String subspace,
			final String dn) {
		GNDMSResponseHeader headers = new GNDMSResponseHeader();
		headers.setResourceURL(baseUrl + "/dspace/_" + subspace);
		headers.setParentURL(baseUrl);
		if (dn != null) {
			headers.setDN(dn);
		}
		return headers;
	}

	/**
	 * Returns the base url of this subspace service.
	 * @return the baseUrl
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base url of this subspace service.
	 * @param baseUrl the baseUrl to set
	 */
	public final void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the subspace provider of this subspace service.
	 * @return the subspaceProvider
	 */
	public final SubspaceProvider getSubspaceProvider() {
		return subspaceProvider;
	}

	/**
	 * Sets the subspace provider of this subspace service.
	 * @param subspaceProvider the subspaceProvider to set
	 */
    @Inject
	public final void setSubspaceProvider(final SubspaceProvider subspaceProvider) {
		this.subspaceProvider = subspaceProvider;
	}

	/**
	 * Returns the facets of this subspace service.
	 * @return the dspaceFacets
	 */
	public final Facets getSubspaceFacets() {
		return subspaceFacets;
	}

	/**
	 * Sets the facets of this subspace service.
	 * @param subspaceFacets the subspaceFacets to set
	 */
	public final void setSubspaceFacets(final Facets subspaceFacets) {
		this.subspaceFacets = subspaceFacets;
	}

	/**
	 * Returns the entity manager factory.
	 * @return the factory.
	 */
	public final EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets the entity manager factory.
	 * @param emf the factory to set.
	 */
	@PersistenceUnit
	public final void setEmf(final EntityManagerFactory emf) {
		this.emf = emf;
	}

    public void setSliceProvider( SliceProviderImpl sliceProvider ) {
        this.sliceProvider = sliceProvider;
    }

    public void setSliceKindProvider( SliceKindProviderImpl sliceKindProvider ) {
        this.slicekindProvider = sliceKindProvider;
    }
}
