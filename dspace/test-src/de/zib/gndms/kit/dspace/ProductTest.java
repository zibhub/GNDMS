package de.zib.gndms.kit.dspace;

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

import de.zib.gndms.common.kit.dspace.Product;

/**
 * Tests the Product.
 * 
 * @author Ulrike Golas
 */

public class ProductTest {

	/**
	 * Tests constructor, getter and setter of Product.
	 */
	@Test
    public final void testProduct() {		
		String a = "test";
		final long b = 2000;
		
		Product<String, Long> prod = new Product<String, Long>(a, b);
		
       	AssertJUnit.assertEquals(a, prod.getFirst());
       	AssertJUnit.assertEquals(b, prod.getSecond().longValue());
       	
       	String aa = "another";
       	final long bb = 3;
       	
       	prod.setFirst(aa);
       	prod.setSecond(bb);
       	
       	AssertJUnit.assertEquals(aa, prod.getFirst());
       	AssertJUnit.assertEquals(bb, prod.getSecond().longValue());
   	}

}
