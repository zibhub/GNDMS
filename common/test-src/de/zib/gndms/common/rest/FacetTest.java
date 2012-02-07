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

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the facet.
 * 
 * @author Ulrike Golas
 */

public class FacetTest {
	/**
	 * Tests the constructors, getter and setter.
	 */
	@Test
    public final void testConstructorGetAndSet() {
		Facet facet = new Facet();

		String name = "facet1";
		String url = "http://www.zib.de/gndms/thisfacet";

		facet.setName(name);
		facet.setUrl(url);
				
		AssertJUnit.assertEquals(name, facet.getName());
		AssertJUnit.assertEquals(url, facet.getUrl());

		facet = new Facet(name, url);
		
		AssertJUnit.assertEquals(name, facet.getName());
		AssertJUnit.assertEquals(url, facet.getUrl());
		
	}

}
