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

import java.util.HashMap;
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

import de.zib.gndms.common.dspace.service.SliceKindService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.logic.dspace.NoSuchElementException;
import de.zib.gndms.logic.dspace.SliceKindProvider;
import de.zib.gndms.logic.dspace.SubspaceProvider;
import de.zib.gndms.logic.model.dspace.SliceKindConfiguration;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;

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
	 * The base url, something like \c http://my.host.org/gndms/grid_id.
	 */
	private String baseUrl;
	/**
	 * All available subspaces.
	 */
	private SubspaceProvider subspaces;
	/**
	 * All available slice kinds.
	 */
	private SliceKindProvider sliceKinds;
	/**
	 * The uri factory.
	 */
	private UriFactory uriFactory;

	/**
	 * Initialization of the slice kind service.
	 */
	@PostConstruct
	public final void init() {
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.GET)
	public final ResponseEntity<Configuration> getSliceKindInfo(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);

		try {
			SliceKind sliceK = findSliceKind(subspace, sliceKind);
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
	public final ResponseEntity<Specifier<Void>> setSliceKindConfig(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);

		try {
			SliceKind sliceK = findSliceKind(subspace, sliceKind);
			
			SliceKindConfiguration sliceKindConfig = checkSliceKindConfig(config);

			// TODO: sliceK.setSliceKindConfiguration(sliceKindConfig)

			Specifier<Void> spec = new Specifier<Void>();

			HashMap<String, String> urimap = new HashMap<String, String>(2);
			urimap.put("service", "dspace");
			urimap.put(UriFactory.SUBSPACE, subspace);
			urimap.put(UriFactory.SLICEKIND, sliceKind);
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.setURL(uriFactory.quoteUri(urimap));

			return new ResponseEntity<Specifier<Void>>(spec, headers,
					HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn("The slice kind " + sliceKind + "does not exist within the subspace" + subspace);
  			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		} catch (WrongConfigurationException e) {
			logger.warn("Wrong slice kind configuration");
  			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}", method = RequestMethod.PUT)
	public final ResponseEntity<Void> createSliceKind(final String subspace,
			final String sliceKind, final Configuration config, final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, dn);
		
		try {
			SliceKindConfiguration sliceKindConfig = checkSliceKindConfig(config);


			// TODO: create slice kind
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.OK);
			
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

			Subspace sub = subspaces.getSubspace(subspace);

			// TODO: sub.deleteSliceKind(sliceKind);
			return new ResponseEntity<Specifier<Void>>(null, headers, HttpStatus.OK);
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
	 * Returns a specific slice kind, if it exists in the subspace.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param sliceKind
	 *            The slice kind id.
	 * @return The slice kind.
	 * @throws NoSuchElementException
	 *             If no such slice exists.
	 */
	private SliceKind findSliceKind(final String subspace,
			final String sliceKind) throws NoSuchElementException {
		Subspace sub = subspaces.getSubspace(subspace);
		Set<SliceKind> allSliceKinds = sub.getMetaSubspace()
				.getCreatableSliceKinds();

		SliceKind sliceK = null;
		for (SliceKind s : allSliceKinds) {
			if (s.equals(sliceKinds.getSliceKind(sliceKind))) {
				sliceK = s;
				break;
			}
		}
		if (sliceK == null) {
			throw new NoSuchElementException();
		} else {
			return sliceK;
		}
	}

	/**
	 * Converts a Configuration into a SliceKindConfiguration, if possible, and
	 * returns it, if valid.
	 * 
	 * @param config
	 *            The given configuration.
	 * @return The valid SliceKindConfiguration.
	 */
	private SliceKindConfiguration checkSliceKindConfig(final Configuration config) {
		try {
			SliceKindConfiguration sliceKindConfig = (SliceKindConfiguration) config;
			if (sliceKindConfig.isValid()) {
				return sliceKindConfig;
			} else {
				throw new WrongConfigurationException(
						"Wrong slice kind configuration");
			}
		} catch (ClassCastException e) {
			throw new WrongConfigurationException(
					"Wrong slice kind configuration");
		}

	}

}
