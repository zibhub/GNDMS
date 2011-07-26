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
import java.util.Set;

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
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.model.dspace.Configuration;
import de.zib.gndms.common.model.dspace.SubspaceConfiguration;
import de.zib.gndms.common.model.dspace.WrongConfigurationException;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.logic.dspace.SubspaceProvider;
import de.zib.gndms.logic.model.TaskExecutionService;
import de.zib.gndms.logic.model.dspace.SetupSubspaceAction;
import de.zib.gndms.model.dspace.SliceKind;
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
	private SubspaceProvider subspaces;
	/**
	 * The uri factory.
	 */
	private UriFactory uriFactory;
	/**
	 * The facets of a subspace.
	 */
	private Facets dspaceFacets;
	/**
	 * The task execution service.
	 */
	private TaskExecutionService executor;
	/**
	 * The auxiliary for task services.
	 */
	private TaskServiceAux taskServiceAux;

	/**
	 * Initialization of the dspace service.
	 */
	@PostConstruct
	public final void init() {
		uriFactory = new UriFactory(baseUrl);
		taskServiceAux = new TaskServiceAux(executor);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.PUT)
	public final ResponseEntity<Facets> createSubspace(
			@PathVariable final String subspace,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		try {
			SubspaceConfiguration subspaceConfig = checkSubspaceConfig(config);

			if (subspaces.exists(subspace)
					|| subspaceConfig.getMode() != SetupMode.CREATE) {
				logger.warn("Subspace " + subspace + " cannot be created");
				return new ResponseEntity<Facets>(null, headers,
						HttpStatus.FORBIDDEN);
			}

			SetupSubspaceAction action = new SetupSubspaceAction();
			action.setPath(subspaceConfig.getPath());
			action.setIsVisibleToPublic(subspaceConfig.isVisible());
			action.setGsiFtpPath(subspaceConfig.getGsiFtpPath());
			action.setMode(subspaceConfig.getMode());
			action.setSize(subspaceConfig.getSize());

			logger.info("Calling action for setting up the supspace "
					+ subspace + ".");

			// TODO what else to do with the action? what about the
			// EntityManager?
			action.call();
			return new ResponseEntity<Facets>(dspaceFacets, headers,
					HttpStatus.CREATED);
		} catch (WrongConfigurationException e) {
			logger.warn("Wrong subspace configuration");
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

		if (!subspaces.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		SetupSubspaceAction action = new SetupSubspaceAction();
		// TODO: path ok?
		action.setPath(subspace);
		action.setMode(SetupMode.DELETE);

		logger.info("Calling action for deleting the supspace " + subspace
				+ ".");
		// TODO what else to do with the action? what about the EntityManager?
		action.call();
		return new ResponseEntity<Specifier<Void>>(null, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.GET)
	public final ResponseEntity<Facets> listAvailableFacets(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (subspaces.exists(subspace)) {
			return new ResponseEntity<Facets>(dspaceFacets, headers,
					HttpStatus.OK);
		}
		logger.warn("Subspace " + subspace + " not found");
		return new ResponseEntity<Facets>(null, headers, HttpStatus.NOT_FOUND);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/slicekinds", method = RequestMethod.GET)
	public final ResponseEntity<List<Specifier<Void>>> listSliceKinds(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<List<Specifier<Void>>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
		Subspace sub = subspaces.getSubspace(subspace);
		Set<SliceKind> sliceKinds = sub.getMetaSubspace()
				.getCreatableSliceKinds();

		List<Specifier<Void>> list = new ArrayList<Specifier<Void>>(
				sliceKinds.size());
		HashMap<String, String> urimap = new HashMap<String, String>(2);
		urimap.put("service", "dspace");
		for (String s : subspaces.listSubspaces()) {
			Specifier<Void> spec = new Specifier<Void>();

			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.addMapping(UriFactory.SUBSPACE, s);
			spec.setURL(uriFactory.quoteUri(urimap));
			list.add(spec);
		}

		return new ResponseEntity<List<Specifier<Void>>>(list, headers,
				HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.GET)
	public final ResponseEntity<Configuration> listSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<Configuration>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		Subspace sub = subspaces.getSubspace(subspace);
		SubspaceConfiguration config;
		config = sub.getSubspaceConfiguration();
		return new ResponseEntity<Configuration>(config, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			logger.warn("Subspace " + subspace + " not found");
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		}

		try {
			SubspaceConfiguration subspaceConfig = checkSubspaceConfig(config);
			Subspace sub = subspaces.getSubspace(subspace);

			sub.updateWithConfiguration(subspaceConfig);
			return new ResponseEntity<Void>(null, headers, HttpStatus.OK);
		} catch (WrongConfigurationException e) {
			logger.warn("Wrong subspace configuration");
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
	 * Converts a Configuration into a SubspaceConfiguration, if possible, and
	 * returns it, if valid.
	 * 
	 * @param config
	 *            The given configuration.
	 * @return The valid SubspaceConfiguration.
	 */
	private SubspaceConfiguration checkSubspaceConfig(final Configuration config) {
		try {
			SubspaceConfiguration subspaceConfig = (SubspaceConfiguration) config;
			if (subspaceConfig.isValid()) {
				return subspaceConfig;
			} else {
				throw new WrongConfigurationException(
						"Wrong subspace configuration");
			}
		} catch (ClassCastException e) {
			throw new WrongConfigurationException(
					"Wrong subspace configuration");
		}

	}
}
