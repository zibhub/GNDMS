package de.zib.gndms.gndmc.dspace.test;

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

import de.zib.gndms.gndmc.dspace.DSpaceClient;
import org.springframework.http.ResponseEntity;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class DSpaceClientTest {
	/**
	 * Tests the constructors.
	 */
	@Test
    public final void testConstructor() {		
		
		DSpaceClient dcl = new DSpaceClient();
       	AssertJUnit.assertNotNull(dcl);
		
		String a = "test";
		dcl = new DSpaceClient(a);
		
       	AssertJUnit.assertEquals(a, dcl.getServiceURL());
   	}

	/**
	 * Tests the request methods.
	 */
	@Test
    public final void testBehavior() {				
		String a = "test";
		DSpaceClient dcl = new DSpaceClient(a);
		
		MockRestTemplate mockTemplate = new MockRestTemplate();
		mockTemplate.setServiceURL(a);
		dcl.setRestTemplate(mockTemplate);
		
		String dn = "me";
		ResponseEntity<?> res;

		res = dcl.listSubspaceSpecifiers(dn);
       	AssertJUnit.assertNotNull(res);
       	   	
	}
}
