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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.zib.gndms.GORFX.service.TaskServiceAux;
import de.zib.gndms.common.dspace.service.SubspaceService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.logic.model.TaskExecutionService;
import de.zib.gndms.logic.model.dspace.DeleteSubspaceAction;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SetupSubspaceAction;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SliceKindProviderImpl;
import de.zib.gndms.logic.model.dspace.SubspaceConfiguration;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.model.dspace.Subspace;

/**
 * The subspace service implementation.
 * 
 * @author Ulrike Golas
 */

public class SubspaceServiceImpl implements SubspaceService {
	/**
	 * The logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * The base url, something like \c http://my.host.org/gndms/grid_id.
	 */
	private String baseUrl;
	/**
	 * Provider of available subspaces.
	 */
	private SubspaceProvider subspaceProvider;
	/**
	 * The uri factory.
	 */
	private UriFactory uriFactory;
	/**
	 * The facets of a subspace.
	 */
	private Facets subspaceFacets;
	/**
	 * The task execution service.
	 */
	private TaskExecutionService executor;
	/**
	 * The auxiliary for task services.
	 */
	private TaskServiceAux taskServiceAux;

	// TODO: initialization of subspaceProvider and executor
	/**
	 * Initialization of the dspace service.
	 */
	@PostConstruct
	public final void init() {
		uriFactory = new UriFactory(baseUrl);
		taskServiceAux = new TaskServiceAux(executor);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.GET)
	public final ResponseEntity<Facets> listAvailableFacets(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (subspaceProvider.exists(subspace)) {
			return new ResponseEntity<Facets>(subspaceFacets, headers,
					HttpStatus.OK);
		}
		logger.warn("Subspace " + subspace + " not found");
		return new ResponseEntity<Facets>(null, headers, HttpStatus.NOT_FOUND);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.PUT)
	public final ResponseEntity<Facets> createSubspace(
			@PathVariable final String subspace,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		try {
			SubspaceConfiguration subspaceConfig = SubspaceConfiguration.checkSubspaceConfig(config);

			if (subspaceProvider.exists(subspace)
					|| subspaceConfig.getMode() != SetupMode.CREATE) {
				logger.warn("Subspace " + subspace + " cannot be created");
				return new ResponseEntity<Facets>(null, headers,
						HttpStatus.FORBIDDEN);
			}

			SetupSubspaceAction action = new SetupSubspaceAction(subspaceConfig);

			logger.info("Calling action for setting up the supspace "
					+ subspace + ".");

			// TODO  Do I need the EntityManager (which is already in the action ...)
			action.call();
			return new ResponseEntity<Facets>(subspaceFacets, headers,
					HttpStatus.CREATED);
		} catch (WrongConfigurationException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Facets>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.DELETE)
	public final ResponseEntity<Specifier<Void>> deleteSubspace(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaceProvider.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		DeleteSubspaceAction action = new DeleteSubspaceAction();
		action.setPath(subspaceProvider.getSubspace(subspace).getPath());
		action.setMode(SetupMode.DELETE);

		logger.info("Calling action for deleting the supspace " + subspace
				+ ".");
		// TODO as above: EntityManager?
		action.call();
		return new ResponseEntity<Specifier<Void>>(null, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/slicekinds", method = RequestMethod.GET)
	public final ResponseEntity<List<Specifier<Void>>> listSliceKinds(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaceProvider.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<List<Specifier<Void>>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
		SliceKindProvider sliceKindProvider = new SliceKindProviderImpl();
		sliceKindProvider.init(subspaceProvider);
		try {
		List<String> sliceKinds = sliceKindProvider.listSliceKindIds(subspace);

		List<Specifier<Void>> list = new ArrayList<Specifier<Void>>(
				sliceKinds.size());
		HashMap<String, String> urimap = new HashMap<String, String>(2);
		urimap.put("service", "dspace");
		for (String sk : sliceKinds) {
			Specifier<Void> spec = new Specifier<Void>();
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.addMapping(UriFactory.SLICEKIND, sk);
			// TODO does the String has to be hard-coded?
			spec.setURL(uriFactory.subspaceUri(urimap, "slicekinds"));
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

		Subspace sub = subspaceProvider.getSubspace(subspace);
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

			SetupSubspaceAction action = new SetupSubspaceAction(subspaceConfig);

			logger.info("Calling action for updating the supspace "
					+ subspace + ".");

			// TODO  Do I need the EntityManager (which is already in the action ...)
			action.call();
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.CREATED);
		} catch (WrongConfigurationException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
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
	 * Returns the task executor of this subspace service.
	 * @return the executor
	 */
	public final TaskExecutionService getExecutor() {
		return executor;
	}

	/**
	 * Sets the task executor of this subspace service.
	 * @param executor the executor to set
	 */
	public final void setExecutor(final TaskExecutionService executor) {
		this.executor = executor;
	}
}
