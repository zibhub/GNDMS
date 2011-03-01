package de.zib.gndms.gndmc.gorfx;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.GORFX.service.TaskFlowService;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.Quote;
import de.zib.gndms.model.gorfx.types.TaskFlowFailure;
import de.zib.gndms.model.gorfx.types.TaskFlowResult;
import de.zib.gndms.model.gorfx.types.TaskFlowStatus;
import de.zib.gndms.rest.Facets;

/**
 * The task flow client implementing the task flow service.
 * 
 * @author Ulrike Golas
 */
public class TaskFlowClient extends AbstractClient implements TaskFlowService {

	/**
	 * The constructor.
	 */
	public TaskFlowClient() {
	}

	/**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public TaskFlowClient(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

	@Override
	public final ResponseEntity<Facets> getFacets(final String type, final String id, final String dn) {
		return unifiedGet(Facets.class, serviceURL + "/gorfx/" + type + "/"
				+ id, dn);
	}

	@Override
	public final ResponseEntity<Void> deleteTaskflow(final String type, final String id, final String dn) {
		return unifiedDelete(serviceURL + "/gorfx/" + type + "/" + id, dn);
	}

	@Override
	public final ResponseEntity<AbstractTF> getOrder(final String type, final String id, final String dn) {
		return unifiedGet(AbstractTF.class, serviceURL + "/gorfx/" + type + "/"
				+ id + "/order", dn);
	}

	@Override
	public final ResponseEntity<Void> setOrder(final String type, final String id,
			final AbstractTF orq, final String dn) {
		return unifiedPut(Void.class, orq, serviceURL + "/gorfx/" + type + "/"
				+ id + "/order", dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<Quote>> getQuotes(final String type, final String id,
			final String dn) {
		return (ResponseEntity<List<Quote>>) (Object) unifiedGet(List.class,
				serviceURL + "/gorfx/" + type + "/" + id + "/quote", dn);
	}

	@Override
	public final ResponseEntity<Void> setQuote(final String type, final String id, final Quote cont,
			final String dn) {
		return unifiedPost(Void.class, cont, serviceURL + "/gorfx/" + type + "/"
				+ id + "/quote", dn);
	}

	@Override
	public final ResponseEntity<Quote> getQuote(final String type, final String id, final int idx,
			final String dn) {
		return unifiedGet(Quote.class, serviceURL + "/gorfx/" + type + "/" + id
				+ "/quote/" + idx, dn);
	}

	@Override
	public final ResponseEntity<Void> deleteQuotes(final String type, final String id, final int idx,
			final String dn) {
		return unifiedDelete(serviceURL + "/gorfx/" + type + "/" + id
				+ "/quote/" + idx, dn);
	}

	@Override
	public final ResponseEntity<String> getTask(final String type, final String id, final String dn) {
		return unifiedGet(String.class, serviceURL + "/gorfx/" + type + "/"
				+ id + "/task", dn);
	}

	@Override
	public final ResponseEntity<String> createTask(final String type, final String id,
			final String quoteId, final String dn) {
		return unifiedPut(String.class, quoteId, serviceURL + "/gorfx/" + type
				+ "/" + id + "/task", dn);
	}

	@Override
	public final ResponseEntity<TaskFlowStatus> getStatus(final String type, final String id,
			final String dn) {
		return unifiedGet(TaskFlowStatus.class, serviceURL + "/gorfx/" + type
				+ "/" + id + "/status", dn);
	}

	@Override
	public final ResponseEntity<TaskFlowResult> getResult(final String type, final String id,
			final String dn) {
		return unifiedGet(TaskFlowResult.class, serviceURL + "/gorfx/" + type
				+ "/" + id + "/result", dn);
	}

	@Override
	public final ResponseEntity<TaskFlowFailure> getErrors(final String type, final String id,
			final String dn) {
		return unifiedGet(TaskFlowFailure.class, serviceURL + "/gorfx/" + type
				+ "/" + id + "/errors", dn);
	}

}
