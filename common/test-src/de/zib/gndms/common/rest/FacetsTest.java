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

import java.util.ArrayList;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the facets.
 * 
 * @author Ulrike Golas
 */

public class FacetsTest {

	/**
	 * Tests the constructors, getter and setter.
	 */
	@Test
    public final void testConstructorGetAndSet() {
		Facets facets = new Facets();

		Facet facet1 = new Facet();
		Facet facet2 = new Facet();
		List<Facet> list = new ArrayList<Facet>();
		list.add(facet1);
		list.add(facet2);
		
		facets.setFacets(list);
		
		Facets facets2 = new Facets(list);
				
		AssertJUnit.assertEquals(list, facets2.getFacets());
		AssertJUnit.assertEquals(list, facets.getFacets());

		list = facets.getFacets();
		AssertJUnit.assertTrue(list.contains(facet1));
		AssertJUnit.assertTrue(list.contains(facet2));
		
	}

	/**
	 * Tests the constructors, getter and setter.
	 */
	@Test
    public final void testFindFacet() {
	
		String name1 = "facet1";
		String name2 = "facet2";
		Facet facet1 = new Facet(name1, null);
		Facet facet2 = new Facet(name2, null);
		List<Facet> list = new ArrayList<Facet>();
		list.add(facet1);
		list.add(facet2);
		Facets facets = new Facets(list);
		
		AssertJUnit.assertEquals(facet1, facets.findFacet(name1));
		AssertJUnit.assertEquals(facet2, facets.findFacet(name2));
		AssertJUnit.assertNull(facets.findFacet("noname"));
	
	}
}
