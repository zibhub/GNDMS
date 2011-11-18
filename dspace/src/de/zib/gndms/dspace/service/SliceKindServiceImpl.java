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

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.zib.gndms.common.dspace.service.SliceKindService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceConfiguration;
import de.zib.gndms.logic.model.dspace.SliceKindConfiguration;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SliceKindProviderImpl;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;

/**
 * The slice kind service implementation.
 * 
 * @author Ulrike Golas
 */

@Controller
@RequestMapping(value = "/dspace")
public class SliceKindServiceImpl implements SliceKindService {
	/**
	 * The logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * The entity manager factory.
	 */
	private EntityManagerFactory emf;
	/**
	 * The entity manager.
	 */
	private EntityManager em;

	/**
	 * The base url, something like \c http://my.host.org/gndms/grid_id.
	 */
	private String baseUrl;
	/**
	 * All available subspaces.
	 */
	private SubspaceProvider subspaceProvider;
	/**
	 * All available slice kinds.
	 */
	private SliceKindProvider sliceKindProvider;
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
		uriFactory = new UriFactory(baseUrl);
		sliceKindProvider = new SliceKindProviderImpl();
		sliceKindProvider.init(subspaceProvider);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.GET)
	public final ResponseEntity<Configuration> getSliceKindInfo(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);

		try {
			SliceKind sliceK = sliceKindProvider.getSliceKind(subspace, sliceKind);
			SliceKindConfiguration config = SliceKindConfiguration.getSliceKindConfiguration(sliceK);
			return new ResponseEntity<Configuration>(config, headers,
					HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn("The slice kind " + sliceKind + "does not exist within the subspace" + subspace + ".");
			return new ResponseEntity<Configuration>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.POST)
	public final ResponseEntity<Specifier<Void>> createSlice(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);

		try {
			if (!sliceKindProvider.exists(subspace, sliceKind)) {
				logger.warn("Slice kind " + sliceKind + " in subspace " + subspace + " not available");
				return new ResponseEntity<Specifier<Void>>(null, headers,
						HttpStatus.FORBIDDEN);
			}
			
			SliceConfiguration sliceConfig = SliceConfiguration.checkSliceConfig(config);

			Subspace sub = subspaceProvider.getSubspace(subspace);
		   	em = emf.createEntityManager();
	       	TxFrame tx = new TxFrame(em);

	        try {
//	            Long ssize = null;
//
//	            if( sliceCreationSpecifier.getTotalStorageSize() != null )
//	                ssize = sliceCreationSpecifier.getTotalStorageSize().longValue();
//	            else
//	                ssize = 0l;
//
//	            srh = getSliceResourceHome( );
//	            rk = srh.createResource( );
//	            SliceResource sr = srh.getResource( rk );
//
//	            GNDMSystem system = subref.getResourceHome( ).getSystem( );
//
//
//
//	            String id =  subref.getID();
//
//	            Query q = em.createNamedQuery( "getMetaSubspaceKey" );
//	            q.setParameter( "idParam", id );
//	            ImmutableScopedName isn = (ImmutableScopedName) q.getSingleResult();
//
//	            final MetaSubspace msp = em.find( MetaSubspace.class, isn );
//
//	            id = sliceCreationSpecifier.getSliceKind( ).toString( );
//	            final SliceKind sk = em.find( SliceKind.class, id );
//
//	            if( sk == null )
//	                throw new IllegalArgumentException( "Slice kind doesn't exist: " + id );
//
//
//	            final CreateSliceAction csa =
//	                    new CreateSliceAction( (String) sr.getID(),
//	                            LogAux.getLocalName(),
//	                            sliceCreationSpecifier.getTerminationTime(),
//	                            system.getModelUUIDGen(),
//	                            sk,
//	                            ssize
//	                    );
//	            csa.setClosingEntityManagerOnCleanup( false );
//	            csa.setOwnEntityManager( em );
//	            csa.setModel( msp.getInstance( ) );
//	            DefaultBatchUpdateAction bua = new DefaultBatchUpdateAction<GridResource>();
//	            bua.setListener( system );
//	            csa.setOwnPostponedEntityActions(bua);
//
//	            final Slice ns = csa.call();
//
//	            csa.getPostponedEntityActions().call( );
//
//	            sr.loadFromModel( ns );
//	            sref = srh.getResourceReference( rk );
//	            tx.commit( );
//	        } catch ( OutOfSpace e ) {
//	            logger.debug(e);
//	            throw e;
//	        } catch ( Exception e ) {
//	            logger.debug(e);
//	            if( srh != null && rk != null )
//	                srh.remove( rk );
//	            throw new RemoteException( e.toString(), e );
	        } finally  {
	            if (tx != null) {
	                tx.finish();       
	            }
	            if (em != null && em.isOpen()) {
	                em.close( );
	            }
	        }

			// TODO: create slice kind
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.OK);
			
			
		} catch (WrongConfigurationException e) {
			logger.warn("Wrong slice kind configuration");
  			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setSliceKindConfig(@PathVariable final String subspace,
			@PathVariable final String sliceKind, final Configuration config, final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);

		try {
			SliceKind sliceK = sliceKindProvider.getSliceKind(subspace, sliceKind);
			SliceKindConfiguration sliceKindConfig = SliceKindConfiguration.checkSliceKindConfig(config);

			// TODO: sliceK.setSliceKindConfiguration(sliceKindConfig)

			Specifier<Void> spec = new Specifier<Void>();

			HashMap<String, String> urimap = new HashMap<String, String>(2);
			urimap.put("service", "dspace");
			urimap.put(UriFactory.SUBSPACE, subspace);
			urimap.put(UriFactory.SLICEKIND, sliceKind);
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.setUrl(uriFactory.quoteUri(urimap));

			return new ResponseEntity<Void>(null, headers,
					HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn("The slice kind " + sliceKind + "does not exist within the subspace" + subspace);
  			return new ResponseEntity<Void>(null, headers,
					HttpStatus.NOT_FOUND);
		} catch (WrongConfigurationException e) {
			logger.warn("Wrong slice kind configuration");
  			return new ResponseEntity<Void>(null, headers,
					HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.DELETE)
	public final ResponseEntity<Specifier<Void>> deleteSliceKind(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);
		try {
			SliceKind sliceK = sliceKindProvider.getSliceKind(subspace, sliceKind);
			Subspace sub = subspaceProvider.getSubspace(subspace);

			// TODO: sub.deleteSliceKind(sliceK);
			return new ResponseEntity<Specifier<Void>>(null, headers, HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn("The slice kind " + sliceKind + "does not exist within the subspace" + subspace);
				return new ResponseEntity<Specifier<Void>>(null, headers,
						HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Sets the GNDMS response header for a given subspace, slice kind and dn
	 * using the base URL.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param sliceKind
	 *            The slice kind id.
	 * @param dn
	 *            The dn.
	 * @return The response header for this subspace.
	 */
	private GNDMSResponseHeader setHeaders(final String subspace,
			final String sliceKind, final String dn) {
		GNDMSResponseHeader headers = new GNDMSResponseHeader();
		headers.setResourceURL(baseUrl + "/dspace/_" + subspace + "/_"
				+ sliceKind);
		headers.setParentURL(baseUrl + "/dspace/_" + subspace);
		if (dn != null) {
			headers.setDN(dn);
		}
		return headers;
	}

	/**
	 * Returns the base url of this slice kind service.
	 * @return the baseUrl
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base url of this slice kind service.
	 * @param baseUrl the baseUrl to set
	 */
	public final void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the subspace provider of this slice kind service.
	 * @return the subspaceProvider
	 */
	public final SubspaceProvider getSubspaceProvider() {
		return subspaceProvider;
	}

	/**
	 * Sets the subspace provider of this slice kind service.
	 * @param subspaceProvider the subspaceProvider to set
	 */
	public final void setSubspaceProvider(final SubspaceProvider subspaceProvider) {
		this.subspaceProvider = subspaceProvider;
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
	
}
