package de.zib.gndms.common.rest;

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
import java.util.Map;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the uri factory.
 * 
 * @author Ulrike Golas
 */
public class UriFactoryTest {

	/**
	 * Tests the constructed uris.
	 */
	@Test
    public final void testUris() {
		String testBaseUrl = "http://www.zib.de/gndms/grid000";
		UriFactory factory = new UriFactory(testBaseUrl);
		Map<String, String> urimap = new HashMap<String, String>();
		String facet = "test";

		try {
			factory.serviceUri(urimap);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String service = "dspace";
		urimap.put(UriFactory.SERVICE, service);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service, factory.serviceUri(urimap));

		try {
			factory.subspaceUri(urimap, facet);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String subspaceId = "mySubspace";
		urimap.put(UriFactory.SUBSPACE, subspaceId);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + subspaceId,
				factory.subspaceUri(urimap, null));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + subspaceId + "/" + facet,
				factory.subspaceUri(urimap, facet));

		urimap.clear();
		try {
			factory.taskServiceUri(urimap);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		service = "gorfx";
		urimap.put(UriFactory.SERVICE, service);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/tasks",
				factory.taskServiceUri(urimap, null));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/tasks/" + facet,
				factory.taskServiceUri(urimap, facet));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/tasks",
				factory.taskServiceUri(urimap));

		try {
			factory.taskFlowTypeUri(urimap, facet);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String type = "myTaskFlowType";
		urimap.put(UriFactory.TASKFLOW_TYPE, type);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + type,
				factory.taskFlowTypeUri(urimap, null));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + type + "/" + facet,
				factory.taskFlowTypeUri(urimap, facet));

		try {
			factory.taskFlowUri(urimap, facet);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String taskFlowId = "myTaskFlow";
		urimap.put(UriFactory.TASKFLOW_ID, taskFlowId);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + type + "/_" + taskFlowId,
				factory.taskFlowUri(urimap, null));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + type + "/_" + taskFlowId + "/" + facet,
				factory.taskFlowUri(urimap, facet));

		try {
			factory.quoteUri(urimap);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String quoteId = "myQuote";
		urimap.put(UriFactory.QUOTE_ID, quoteId);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/_" + type + "/_" + taskFlowId + "/quotes/" + quoteId,
				factory.quoteUri(urimap));

		try {
			factory.taskUri(urimap, facet);
			AssertJUnit.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		String taskId = "myTask";
		urimap.put(UriFactory.TASK_ID, taskId);
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/tasks/_" + taskId,
				factory.taskUri(urimap, null));
		AssertJUnit.assertEquals(testBaseUrl + "/" + service + "/tasks/_" +  taskId + "/" + facet,
				factory.taskUri(urimap, facet));

	}
	
	/**
	 * Tests the constructor, getter and setter.
	 */
	@Test
    public final void testConstructor() {
		String testBaseUrl = "http://www.zib.de/gndms/grid000";
		UriFactory factory = new UriFactory();
		factory.setBaseUrl(testBaseUrl);
		AssertJUnit.assertEquals(testBaseUrl, factory.getBaseUrl());
		
	}
}
