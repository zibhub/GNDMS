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

import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.dspace.service.DSpaceService;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * The dspace client implementing the dspace service.
 * 
 * @author Ulrike Golas
 */
public class DSpaceClient extends AbstractClient implements DSpaceService {

	/**
	 * The constructor.
	 */
	public DSpaceClient() {
	}

    /**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public DSpaceClient(final String serviceURL) {
		this.setServiceURL(serviceURL);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final ResponseEntity<List<Specifier<Subspace>>> listSubspaceSpecifiers(
			final String dn) {
		return (ResponseEntity<List<Specifier<Subspace>>>) (Object) unifiedGet(List.class,
				getServiceURL() + "/dspace", dn);
	}

	@Override
	public final ResponseEntity<Facets> listAvailableFacets(
			final String subspace, final String dn) {
		return unifiedGet(Facets.class, getServiceURL() + "/dspace/_" + subspace, dn);
	}

	@Override
	public final ResponseEntity<Facets> createSubspace(final String subspace,
			final ConfigHolder config, final String dn) {
		return unifiedPut(Facets.class, config, getServiceURL() + "/dspace/_"
				+ subspace, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<Specifier<Task>> deleteSubspace(final String subspace,
			final String dn) {
		return (ResponseEntity<Specifier<Task>>) (Object) unifiedDelete(Specifier.class, getServiceURL() + "/dspace/_" + subspace, dn);
	}

	@Override
	public final ResponseEntity<ConfigHolder> listSubspaceConfiguration(
			final String subspace, final String dn) {
		return unifiedGet(ConfigHolder.class, getServiceURL() + "/dspace/_"
				+ subspace + "/config", dn);
	}

	@Override
	public final ResponseEntity<Void> setSubspaceConfiguration(
			final String subspace, final ConfigHolder config,
			final String dn) {
		return unifiedPut(Void.class, config, getServiceURL() + "/dspace/_"
				+ subspace + "/config", dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<Specifier<SliceKind>>> listSliceKinds(final String subspace,
			final String dn) {
		return (ResponseEntity<List<Specifier<SliceKind>>>) (Object) unifiedGet(
				List.class, getServiceURL() + "/dspace/_" + subspace + "/slicekinds",
				dn);
	}

}
