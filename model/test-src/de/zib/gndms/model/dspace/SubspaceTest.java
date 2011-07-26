package de.zib.gndms.model.dspace;

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

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.common.model.dspace.SubspaceConfiguration;

/**
 * Tests the SubspaceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SubspaceTest {
	
	/**
	 * Tests the method getSubspaceConfiguration.
	 */
	@Test
    public final void testGetSubspaceConfiguration()  {
		Subspace dummy = new Subspace();
		MetaSubspace dummyMeta = new MetaSubspace();
		dummy.setMetaSubspace(dummyMeta);
		
		String path = "testpath";
		dummy.setPath(path);
		String gsiftp = "testgsiftp";
		dummy.setGsiFtpPath(gsiftp);
		boolean visible = true;
		dummyMeta.setVisibleToPublic(visible);
		final long size = 2000;
		dummy.setAvailableSize(size);
		String mode = "UPDATE";		
				
		SubspaceConfiguration config = dummy.getSubspaceConfiguration();

		AssertJUnit.assertEquals(true, config.isValid());
		AssertJUnit.assertEquals(path, config.getPath());
		AssertJUnit.assertEquals(gsiftp, config.getGsiFtpPath());
		AssertJUnit.assertEquals(visible, config.isVisible());
		AssertJUnit.assertEquals(size, config.getSize());
		AssertJUnit.assertEquals(mode, config.getMode().name());

	}

}
