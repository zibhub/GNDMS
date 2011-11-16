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

import java.util.Map;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the specifier.
 * 
 * @author Ulrike Golas
 */
public class SpecifierTest {

	/**
	 * Tests the  getter and setter.
	 */
	@Test
    public final void testGetAndSet() {
		Specifier<String> spec = new Specifier<String>();

		String testUrl = "http://www.zib.de/gndms/grid000";

		spec.setUrl(testUrl);
		AssertJUnit.assertEquals(testUrl, spec.getUrl());

		String load = "testLoad";
		
		AssertJUnit.assertNull(spec.getPayload());
		AssertJUnit.assertFalse(spec.hasPayload());
		
		spec.setPayload(load);
		AssertJUnit.assertEquals(load, spec.getPayload());

		Specifier<String> innerSpec = new Specifier<String>();
		AssertJUnit.assertNull(spec.getSpecifier());
		spec.setSpecifier(innerSpec);
		AssertJUnit.assertEquals(innerSpec, spec.getSpecifier());
		
		AssertJUnit.assertNull(spec.getUriMap());
		String key = "testkey";
		String value = "testValue";
		spec.addMapping(key, value);
		AssertJUnit.assertNotNull(spec.getUriMap());
		Map<String, String> map = spec.getUriMap();
		map.put("key", "value");
		spec.setUriMap(map);
		AssertJUnit.assertEquals(map, spec.getUriMap());

	}

	
}
