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

import de.zib.gndms.GORFX.service.TaskFlowService;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
		return unifiedGet(Facets.class, serviceURL + "/gorfx/_" + type + "/_"
				+ id, dn);
	}

	@Override
	public final ResponseEntity<Void> deleteTaskflow(final String type, final String id, final String dn, final String wid) {
		return unifiedDelete(serviceURL + "/gorfx/_" + type + "/_" + id, dn, wid);
	}

	@Override
	public final ResponseEntity<AbstractTF> getOrder(final String type, final String id, final String dn, final String wid) {
		return unifiedGet(AbstractTF.class, serviceURL + "/gorfx/_" + type + "/_"
				+ id + "/order", dn, wid);
	}

	@Override
	public final ResponseEntity<Void> setOrder(final String type, final String id,
			final AbstractTF orq, final String dn, final String wid) {
		return unifiedPut(Void.class, orq, serviceURL + "/gorfx/_" + type + "/_"
				+ id + "/order", dn, wid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<Specifier<Quote>>> getQuotes(final String type, final String id,
			final String dn, final String wid) {
		return ( ResponseEntity<List<Specifier<Quote>>> ) (Object) unifiedGet(List.class,
				serviceURL + "/gorfx/_" + type + "/_" + id + "/quote", dn, wid);
	}

	@Override
	public final ResponseEntity<Void> setQuote(final String type, final String id, final Quote cont,
			final String dn, final String wid) {
		return unifiedPost(Void.class, cont, serviceURL + "/gorfx/_" + type + "/_"
				+ id + "/quote", dn, wid);
	}


	public final ResponseEntity<Quote> getQuote(final String type, final String id, final int idx,
			final String dn, final String wid) {
		return unifiedGet(Quote.class, serviceURL + "/gorfx/_" + type + "/_" + id
				+ "/quote/_" + idx, dn, wid);
	}


	public final ResponseEntity<Void> deleteQuotes(final String type, final String id, final int idx,
			final String dn, final String wid) {
		return unifiedDelete(serviceURL + "/gorfx/_" + type + "/_" + id
				+ "/quote/_" + idx, dn, wid);
	}


    @SuppressWarnings("unchecked")
	public final ResponseEntity<Specifier<Facets>> getTask( final String type, final String id, final String dn, final String wid ) {
		return ( ResponseEntity<Specifier<Facets>> ) (Object) unifiedGet(Specifier.class, serviceURL + "/gorfx/_" + type + "/_"
				+ id + "/task", dn, wid);
	}


    @SuppressWarnings("unchecked")
	public final ResponseEntity<Specifier<Facets>> createTask( final String type, final String id,
                                                               final String quoteId, final String dn, final String wid ) {
		return ( ResponseEntity<Specifier<Facets>> ) (Object) unifiedPut(Specifier.class, quoteId, serviceURL + "/gorfx/_" + type
				+ "/_" + id + "/task", dn, wid);
	}


	public final ResponseEntity<TaskFlowStatus> getStatus(final String type, final String id,
			final String dn, final String wid) {
		return unifiedGet(TaskFlowStatus.class, serviceURL + "/gorfx/_" + type
				+ "/_" + id + "/status", dn, wid);
	}


    @SuppressWarnings("unchecked")
	public final ResponseEntity<Specifier<TaskResult>> getResult( final String type, final String id,
                                                                  final String dn, final String wid ) {
		return ( ResponseEntity<Specifier<TaskResult>> ) (Object) unifiedGet(Specifier.class, serviceURL + "/gorfx/_" + type
				+ "/_" + id + "/result", dn, wid);
	}


    @SuppressWarnings("unchecked")
	public final ResponseEntity<Specifier<TaskFailure>> getErrors( final String type, final String id,
                                                                   final String dn, final String wid ) {
		return ( ResponseEntity<Specifier<TaskFailure>> ) (Object) unifiedGet(Specifier.class, serviceURL + "/gorfx/_" + type
				+ "/_" + id + "/errors", dn, wid);
	}

}
