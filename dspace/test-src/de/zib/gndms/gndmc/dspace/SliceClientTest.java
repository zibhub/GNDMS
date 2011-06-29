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

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.springframework.http.ResponseEntity;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.zib.gndms.model.dspace.MockSliceConfiguration;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.rest.Specifier;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class SliceClientTest {
	/**
	 * Tests the constructors.
	 */
	@Test
    public final void testConstructor() {		
		
		SliceClient scl = new SliceClient();
       	AssertJUnit.assertNotNull(scl);
		
		String a = "test";
		scl = new SliceClient(a);
		
       	AssertJUnit.assertEquals(a, scl.getServiceURL());
   	}

	/**
	 * Tests the request methods.
	 */
	@Test
    public final void testBehavior() {				
		String a = "test";
		SliceClient scl = new SliceClient(a);
		
		MockRestTemplate mockTemplate = new MockRestTemplate();
		mockTemplate.setServiceURL(a);
		scl.setRestTemplate(mockTemplate);
		
		String dn = "me";
		String subspace = "testSubspace";
		String sliceKind = "testSliceKind";
		String slice = "testSlice";
		String fileName = "testFile";
		ResponseEntity<?> res;

		res = scl.listSliceFacets(subspace, sliceKind, slice, dn);
       	AssertJUnit.assertNotNull(res);
       	       	
		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();
       	ConfigHolder config = new MockSliceConfiguration(directory, owner, value);
		res = scl.setSliceConfiguration(subspace, sliceKind, slice, config, dn);
       	AssertJUnit.assertNotNull(res);

       	Specifier<SliceKind> spec = new Specifier<SliceKind>();
		res = scl.transformSlice(subspace, sliceKind, slice, spec, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.deleteSlice(subspace, sliceKind, slice, dn);
       	AssertJUnit.assertNotNull(res);

       	Vector<String> attr = new Vector<String>();
       	attr.add("filename");
		res = scl.listFiles(subspace, sliceKind, slice, attr, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.deleteFiles(subspace, sliceKind, slice, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.getGridFtpUrl(subspace, sliceKind, slice, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.listFileContent(subspace, sliceKind, slice, fileName, dn);
       	AssertJUnit.assertNotNull(res);

		File file = new File("testPath");
       	res = scl.setFileContent(subspace, sliceKind, slice, fileName, file, dn);
       	AssertJUnit.assertNotNull(res);

		res = scl.deleteFile(subspace, sliceKind, slice, fileName, dn);
       	AssertJUnit.assertNotNull(res);
   	}
}
