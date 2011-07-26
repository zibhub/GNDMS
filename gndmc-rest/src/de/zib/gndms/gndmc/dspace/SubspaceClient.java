package de.zib.gndms.gndmc.dspace;

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

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.zib.gndms.common.dspace.service.SubspaceService;
import de.zib.gndms.common.model.dspace.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;

/**
 * The subspace client implementing the subspace service.
 * 
 * @author Ulrike Golas
 */

public class SubspaceClient extends AbstractClient implements SubspaceService {

	/**
	 * The constructor.
	 */
	public SubspaceClient() {
	}


    /**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public SubspaceClient(final String serviceURL) {
		this.setServiceURL(serviceURL);
	}

	@Override
	public final ResponseEntity<Facets> createSubspace(final String subspace,
			final Configuration config, final String dn) {
		return unifiedPut(Facets.class, config, getServiceURL() + "/dspace/_"
				+ subspace, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<Specifier<Void>> deleteSubspace(final String subspace,
			final String dn) {
		return (ResponseEntity<Specifier<Void>>) (Object) unifiedDelete(Specifier.class, getServiceURL() 
				+ "/dspace/_" + subspace, dn);
	}

	@Override
	public final ResponseEntity<Facets> listAvailableFacets(
			final String subspace, final String dn) {
		return unifiedGet(Facets.class, getServiceURL() + "/dspace/_" + subspace, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<Specifier<Void>>> listSliceKinds(final String subspace,
			final String dn) {
		return (ResponseEntity<List<Specifier<Void>>>) (Object) unifiedGet(
				List.class, getServiceURL() + "/dspace/_" + subspace + "/slicekinds",
				dn);
	}

	@Override
	public final ResponseEntity<Configuration> listSubspaceConfiguration(
			final String subspace, final String dn) {
		return unifiedGet(Configuration.class, getServiceURL() + "/dspace/_"
				+ subspace + "/config", dn);
	}

	@Override
	public final ResponseEntity<Void> setSubspaceConfiguration(
			final String subspace, final Configuration config,
			final String dn) {
		return unifiedPut(Void.class, config, getServiceURL() + "/dspace/_"
				+ subspace + "/config", dn);
	}

}
