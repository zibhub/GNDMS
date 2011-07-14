package de.zib.gndms.gndmc.dspace;

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

import org.springframework.http.ResponseEntity;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.zib.gndms.common.model.dspace.MockSubspaceConfiguration;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class SubspaceClientTest {
	/**
	 * Tests the constructors.
	 */
	@Test
    public final void testConstructor() {		
		
		SubspaceClient scl = new SubspaceClient();
       	AssertJUnit.assertNotNull(scl);
		
		String a = "test";
		scl = new SubspaceClient(a);
		
       	AssertJUnit.assertEquals(a, scl.getServiceURL());
   	}

	/**
	 * Tests the request methods.
	 */
	@Test
    public final void testBehavior() {				
		String a = "test";
		SubspaceClient scl = new SubspaceClient(a);
		
		MockRestTemplate mockTemplate = new MockRestTemplate();
		mockTemplate.setServiceURL(a);
		scl.setRestTemplate(mockTemplate);
		
		String dn = "me";
		String subspace = "testSubspace";
		ResponseEntity<?> res;
       	
		res = scl.listAvailableFacets(subspace, dn);
       	AssertJUnit.assertNotNull(res);
       	
		String path = "testpath";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long value = 6000;
		String mode = "UPDATE";
       	ConfigHolder config = new MockSubspaceConfiguration(path, gsiftp, visible, value, mode);
		res = scl.createSubspace(subspace, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.deleteSubspace(subspace, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.listSubspaceConfiguration(subspace, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.setSubspaceConfiguration(subspace, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.listSliceKinds(subspace, dn);
       	AssertJUnit.assertNotNull(res);
   	}
}
