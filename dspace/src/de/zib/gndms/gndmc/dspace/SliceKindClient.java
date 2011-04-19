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

import org.springframework.http.ResponseEntity;

import de.zib.gndms.dspace.service.SliceKindService;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.rest.Specifier;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * The slice kind client implementing the slice kind service.
 * 
 * @author Ulrike Golas
 */

public class SliceKindClient extends AbstractClient implements SliceKindService {

	/**
	 * The constructor.
	 */
	public SliceKindClient() {
	}

    /**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public SliceKindClient(final String serviceURL) {
		this.setServiceURL(serviceURL);
	}

	@Override
	public final ResponseEntity<ConfigHolder> getSliceKindInfo(final String subspace,
			final String sliceKind, final String dn) {
		return unifiedGet(ConfigHolder.class, getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<Specifier<SliceKind>> setSliceKindConfig(final String subspace,
			final String sliceKind, final ConfigHolder config, final String dn) {
		return (ResponseEntity<Specifier<SliceKind>>) (Object) unifiedPost(Specifier.class, config, getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind, dn);
	}

	@Override
	public final ResponseEntity<Void> createSliceKind(final String subspace,
			final String sliceKind, final ConfigHolder config, final String dn) {
		return unifiedPut(Void.class, config, getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind, dn);
	}

	@Override
	public final ResponseEntity<Void> deleteSliceKind(final String subspace,
			final String sliceKind, final String dn) {
		return unifiedDelete(getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind, dn);
	}

}
