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

import de.zib.gndms.logic.model.dspace.SliceKindConfiguration;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class SliceKindClientTest {
	/**
	 * Tests the constructors.
	 */
	@Test
    public final void testConstructor() {		
		
		SliceKindClient scl = new SliceKindClient();
       	AssertJUnit.assertNotNull(scl);
		
		String a = "test";
		scl = new SliceKindClient(a);
		
       	AssertJUnit.assertEquals(a, scl.getServiceURL());
   	}

	/**
	 * Tests the request methods.
	 */
	@Test
    public final void testBehavior() {				
		String a = "test";
		SliceKindClient scl = new SliceKindClient(a);
		
		MockRestTemplate mockTemplate = new MockRestTemplate();
		mockTemplate.setServiceURL(a);
		scl.setRestTemplate(mockTemplate);
		
		String dn = "me";
		String subspace = "testSubspace";
		String sliceKind = "testSliceKind";
		ResponseEntity<?> res;

		res = scl.getSliceKindInfo(subspace, sliceKind, dn);
       	AssertJUnit.assertNotNull(res);
       	       	
		String uri = "testuri";
		String permission = "345";
       	SliceKindConfiguration config = new SliceKindConfiguration(uri, permission, null);
		res = scl.createSlice(subspace, sliceKind, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.createSlice(subspace, sliceKind, config, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.deleteSliceKind(subspace, sliceKind, dn);
       	AssertJUnit.assertNotNull(res);
   	}
}
