package de.zib.gndms.gndmc.gorfx;

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

import de.zib.gndms.GORFX.service.GORFXService;
import de.zib.gndms.logic.action.ActionMeta;
import de.zib.gndms.logic.config.ConfigMeta;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 08.02.11, Time: 12:10
 * @brief  Complete client for the gorfx service.
 *
 * @see de.zib.gndms.GORFX.service.GORFXService for details.
 * @see de.zib.gndms.gndmc.gorfx.GORFXClient for a "smaller" client.
 */
public class FullGORFXClient extends GORFXClient implements GORFXService {

	/**
	 * The constructor.
	 */
	public FullGORFXClient() {
	}

	/**
	 * The constructor.
	 * 
	 * @param serviceURL
	 *            The base url of the grid.
	 */
	public FullGORFXClient(final String serviceURL) {
		super(serviceURL);
	}

	@SuppressWarnings("unchecked")
	public final ResponseEntity<List<String>> listConfigActions(final String dn) {
		return (ResponseEntity<List<String>>) (Object) unifiedGet(List.class,
				getServiceURL() + "/gorfx/config/", dn);
	}


    public final ResponseEntity<ConfigMeta> getConfigActionInfo(final String actionName,
			final String dn) {
		return unifiedGet(ConfigMeta.class, getServiceURL() + "/gorfx/config/_"
				+ actionName, dn);
	}

	public final ResponseEntity<String> callConfigAction(final String actionName,
			final String args, final String dn) {
		return null; // not required here
	}

	public final ResponseEntity<List<String>> listBatchActions(final String dn) {
		return null; // not required here
	}

	public final ResponseEntity<ActionMeta> getBatchActionInfo(final String actionName,
			final String dn) {
		return null; // not required here
	}


    public ResponseEntity<Specifier<Facets>> getBatchAction( String actionName, String id, String dn ) {
        return null;  // not required here
    }


    public final ResponseEntity<Specifier> callBatchAction(final String actionName,
			final String args, final String dn) {
		return null; // not required here
	}
}
