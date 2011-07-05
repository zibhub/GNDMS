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

import de.zib.gndms.logic.model.config.SetupAction.SetupMode;
import de.zib.gndms.model.dspace.MockSubspaceConfiguration;
import de.zib.gndms.stuff.confuror.ConfigHolder;

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
		String subspace = "testSubspace";
		ResponseEntity<?> res;

		res = dcl.listSubspaceSpecifiers("me");
       	AssertJUnit.assertNotNull(res);
       	
		res = dcl.listAvailableFacets(subspace, dn);
       	AssertJUnit.assertNotNull(res);
       	
		String path = "testpath";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long value = 6000;
		SetupMode mode = SetupMode.valueOf("UPDATE");
       	ConfigHolder config = new MockSubspaceConfiguration(path, gsiftp, visible, value, mode);
		res = dcl.createSubspace(subspace, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = dcl.deleteSubspace(subspace, dn);
       	AssertJUnit.assertNotNull(res);

		res = dcl.listSubspaceConfiguration(subspace, dn);
       	AssertJUnit.assertNotNull(res);

		res = dcl.setSubspaceConfiguration(subspace, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = dcl.listSliceKinds(subspace, dn);
       	AssertJUnit.assertNotNull(res);
   	}
}