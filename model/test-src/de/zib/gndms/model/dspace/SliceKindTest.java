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

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.common.model.dspace.SliceKindConfiguration;

/**
 * Tests the SubspaceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceKindTest {
		
	/**
	 * Tests the method getSliceKindConfiguration.
	 */
	@Test
    public final void testGetSliceKindConfiguration() {
		SliceKind dummy = new SliceKind();
		
		String uri = "testuri";
		dummy.setURI(uri);
		final long permission = 345;
		AccessMask perm = AccessMask.fromString((new Long(permission)).toString());
		dummy.setPermission(permission);
		MetaSubspace dummyMeta = new MetaSubspace();
		Set<MetaSubspace> metaSubspaces = new HashSet<MetaSubspace>();
		metaSubspaces.add(dummyMeta);		
		dummy.setMetaSubspaces(metaSubspaces);
						
		SliceKindConfiguration config = dummy.getSliceKindConfiguration();

		AssertJUnit.assertEquals(true, config.isValid());
		AssertJUnit.assertEquals(uri, config.getUri());
		AssertJUnit.assertEquals(perm, config.getPermission());
		
		// TODO test for meta-subspaces
		// AssertJUnit.assertEquals(metaSubspaces, SliceKindConfiguration.getMetaSubspaces(config));

	}

}
