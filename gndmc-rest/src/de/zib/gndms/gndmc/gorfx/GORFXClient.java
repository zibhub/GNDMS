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

import de.zib.gndms.GORFX.service.GORFXServiceEssentials;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.rest.Facets;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * 
 * @brief
 */
public class GORFXClient extends AbstractClient implements
		GORFXServiceEssentials {

	/**
	 * The constructor.
	 */
	public GORFXClient() {
	}

	/**
	 * The constructor.
	 * 
	 * @param serviceURL
	 *            The base url of the grid.
	 */
	public GORFXClient(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

	@Override
	public final ResponseEntity<Facets> listAvailableFacets(final String dn) {
		return unifiedGet(Facets.class, serviceURL + "/gorfx/", dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<String>> listTaskFlows(final String dn) {
		return (ResponseEntity<List<String>>) (Object) unifiedGet(List.class,
				serviceURL + "/gorfx/taskflows/", dn);
	}

	@Override
	public final ResponseEntity<TaskFlowInfo> getTaskFlowInfo(final String type, final String dn) {
		return unifiedGet(TaskFlowInfo.class, serviceURL + "/gorfx/" + type, dn);
	}

	@Override
	public final ResponseEntity<String> createTaskFlow(final String type, final AbstractTF order,
			final String dn, final String wid) {
		return unifiedPost(String.class, AbstractTF.class, serviceURL
				+ "/gorfx/" + type, wid, dn);
	}

}
