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

import de.zib.gndms.common.dspace.service.DSpaceService;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
	public final ResponseEntity<List<Specifier<Void>>> listSubspaceSpecifiers(
			final String dn) {
		return (ResponseEntity<List<Specifier<Void>>>) (Object) unifiedGet(List.class,
				getServiceURL() + "/dspace/", dn);
	}

}
