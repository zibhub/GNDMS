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
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

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

import de.zib.gndms.logic.dspace.SubspaceProvider;
import de.zib.gndms.logic.dspace.WrongConfigurationException;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.dspace.SubspaceConfiguration;
import de.zib.gndms.model.gorfx.types.Task;
import de.zib.gndms.rest.Facet;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import de.zib.gndms.rest.Specifier;
import de.zib.gndms.rest.UriFactory;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * The dspace service implementation.
 * 
 * @author Ulrike Golas
 */

@Controller
@RequestMapping(value = "/dspace")
public class DSpaceServiceImpl implements DSpaceService {
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
	 * Facet names of a subspace.
	 */
	private List<String> dspaceFacets = new ArrayList<String>(2);

	/**
	 * Initialization of the dspace service.
	 */
	@PostConstruct
	public final void init() {
		uriFactory = new UriFactory(baseUrl);
	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public final ResponseEntity<List<Specifier<Subspace>>> listSubspaceSpecifiers(
			@RequestHeader("DN") final String dn) {
		if (subspaces == null) {
			throw new IllegalStateException("Provider is null");
		}
		GNDMSResponseHeader headers = new GNDMSResponseHeader();
		headers.setResourceURL(baseUrl + "/dspace/");
		headers.setParentURL(baseUrl);
		if (dn != null) {
			headers.setDN(dn);
		}
		List<Specifier<Subspace>> list = new ArrayList<Specifier<Subspace>>();
		HashMap<String, String> urimap = new HashMap<String, String>(2);
		urimap.put("service", "dspace");
		for (String s : subspaces.listSubspaces()) {
			Specifier<Subspace> spec = new Specifier<Subspace>();
			
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.addMapping(UriFactory.SUBSPACE, s);
			spec.setURL(uriFactory.quoteUri(urimap));
			list.add(spec);
		}
		return new ResponseEntity<List<Specifier<Subspace>>>(
				list, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.GET)
	public final ResponseEntity<Facets> listAvailableFacets(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (subspaces.exists(subspace)) {
			Facets facets = listSubspaceFacets(subspace);
			return new ResponseEntity<Facets>(facets, headers, HttpStatus.OK);
		}
		return new ResponseEntity<Facets>(null, headers, HttpStatus.NOT_FOUND);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.PUT)
	public final ResponseEntity<Facets> createSubspace(
			@PathVariable final String subspace,
			@RequestBody final ConfigHolder config,
			@RequestHeader("DN") final String dn) {

		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (subspaces.exists(subspace)) {
			return new ResponseEntity<Facets>(null, headers,
					HttpStatus.FORBIDDEN);
		}
		try {
			SubspaceConfiguration subConfig = new SubspaceConfiguration(config);
		} catch (WrongConfigurationException e) {
			return new ResponseEntity<Facets>(null, headers,
					HttpStatus.BAD_REQUEST);
		}

		// TODO: create subspace according to subConfig, add to provider
		Facets facets = listSubspaceFacets(subspace);
		return new ResponseEntity<Facets>(facets, headers, HttpStatus.CREATED);
	}

	@Override
	@RequestMapping(value = "/_{subspace}", method = RequestMethod.DELETE)
	public final ResponseEntity<Specifier<Task>> deleteSubspace(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			return new ResponseEntity<Specifier<Task>>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		// TODO delete subspace, create deletion task
		return new ResponseEntity<Specifier<Task>>(null, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.GET)
	public final ResponseEntity<ConfigHolder> listSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			return new ResponseEntity<ConfigHolder>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		Subspace sub = subspaces.getSubspace(subspace);
		ConfigHolder config = new SubspaceConfiguration(sub);
		return new ResponseEntity<ConfigHolder>(
				config, headers, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/config", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setSubspaceConfiguration(
			@PathVariable final String subspace,
			@RequestBody final ConfigHolder config,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		}

		try {
			SubspaceConfiguration subConfig = new SubspaceConfiguration(config);

			Subspace sub = subspaces.getSubspace(subspace);
			// TODO: sub.setSubspaceConfiguration(subConfig);
			return new ResponseEntity<Void>(null, headers, HttpStatus.OK);
		} catch (WrongConfigurationException e) {
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/slicekinds", method = RequestMethod.GET)
	public final ResponseEntity<List<Specifier<SliceKind>>> listSliceKinds(
			@PathVariable final String subspace,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setSubspaceHeaders(subspace, dn);

		if (!subspaces.exists(subspace)) {
			return new ResponseEntity<List<Specifier<SliceKind>>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
		Subspace sub = subspaces.getSubspace(subspace);
		Set<SliceKind> sliceKinds = sub.getMetaSubspace().getCreatableSliceKinds();
		
		List<Specifier<SliceKind>> list = new ArrayList<Specifier<SliceKind>>(sliceKinds.size());
		HashMap<String, String> urimap = new HashMap<String, String>(2);
		urimap.put("service", "dspace");
		for (String s : subspaces.listSubspaces()) {
			Specifier<SliceKind> spec = new Specifier<SliceKind>();
			
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.addMapping(UriFactory.SUBSPACE, s);
			spec.setURL(uriFactory.quoteUri(urimap));
			list.add(spec);
		}
		
		return new ResponseEntity<List<Specifier<SliceKind>>>(list, headers, HttpStatus.OK);
	}

	/**
	 * Sets the GNDMS response header for a given subspace and dn using the base URL.
	 * 
	 * @param subspace The subspace id.
	 * @param dn The dn.
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
	 * Returns the facets of a subspace.
	 * @param subspace the subspace id.
	 * @return The facets for this subspace.
	 */
	private Facets listSubspaceFacets(final String subspace) {
		Map<String, String> uriargs = new HashMap<String, String>(2);
		uriargs.put(UriFactory.SUBSPACE, subspace);
		uriargs.put(UriFactory.SERVICE, "dspace");

		ArrayList<Facet> fl = new ArrayList<Facet>(dspaceFacets.size());
		for (String f : dspaceFacets) {
			String fn = uriFactory.subspaceUri(uriargs, f);
			fl.add(new Facet(f, fn));
		}
		return new Facets(fl);
	}

}
