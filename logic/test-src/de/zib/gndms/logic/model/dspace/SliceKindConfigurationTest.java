package de.zib.gndms.logic.model.dspace;

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

import de.zib.gndms.common.dspace.SliceKindConfiguration;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;

/**
 * Tests the SliceKindConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceKindConfigurationTest {
	
	/**
	 * Tests equals() and hashcode().
	 */
	@Test
    public final void testEquals() {
		String uri = "testuri";
		final String permission = "345";
       	String sub = "testSubspace";

		SliceKindConfiguration slconfig = new SliceKindConfiguration(uri, permission, sub);
		SliceKindConfiguration slconfig2 = new SliceKindConfiguration(uri, permission, sub);
		
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig2));
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig));
       	AssertJUnit.assertEquals(true, slconfig2.equals(slconfig));
       	AssertJUnit.assertEquals(slconfig.hashCode(), slconfig2.hashCode());

		uri = "test22";
		slconfig2 = new SliceKindConfiguration(uri, permission, sub);

       	AssertJUnit.assertEquals(false, slconfig.equals(slconfig2));
       	AssertJUnit.assertEquals(false, slconfig2.equals(slconfig));
       	AssertJUnit.assertEquals(false, slconfig.equals(null));
       	AssertJUnit.assertEquals(false, slconfig.equals(uri));
	}

	/**
	 * Tests the constructor and getters, setters, and isValid() with valid arguments.
	 */
	@Test
    public final void testConstructors() {
		String uri = "testuri";
		final String perm = "345";
		AccessMask permission = AccessMask.fromString(perm);
       	String sub = "testSubspace";

		SliceKindConfiguration slconfig = new SliceKindConfiguration(uri, permission, sub);
		       	
       	String testUri = slconfig.getUri();
       	AccessMask testPermission = slconfig.getPermission();
       	String testPerm = slconfig.getPermissionAsString();
       	String testSub = slconfig.getSubspaces();
       	boolean valid = slconfig.isValid();
       	
       	AssertJUnit.assertEquals(uri, testUri);
       	AssertJUnit.assertEquals(permission, testPermission);
       	AssertJUnit.assertEquals(perm, testPerm);
       	AssertJUnit.assertEquals(sub, testSub);
       	AssertJUnit.assertEquals(true, valid);

		SliceKindConfiguration slconfig2 = new SliceKindConfiguration(uri, perm, sub);
       	testPermission = slconfig2.getPermission();
       	valid = slconfig2.isValid();
		
       	AssertJUnit.assertEquals(slconfig.getStringRepresentation(), slconfig2.getStringRepresentation());
       	AssertJUnit.assertEquals(permission, testPermission);
       	AssertJUnit.assertEquals(true, valid);

		String uri2 = "uritest";
		String perm2 = "116";
		AccessMask permission2 = AccessMask.fromString(perm2);
      	String sub2 = "testSubspace2";

		slconfig2.setUri(uri2);
		slconfig2.setPermission(permission2);
		slconfig2.setSubspaces(sub2);

       	String testUri2 = slconfig2.getUri();
       	String testSub2 = slconfig2.getSubspaces();
       	AccessMask testPermission2 = slconfig2.getPermission();
       	String testPerm2 = slconfig2.getPermissionAsString();
       	boolean valid2 = slconfig2.isValid();

       	AssertJUnit.assertEquals(uri2, testUri2);
       	AssertJUnit.assertEquals(sub2, testSub2);
       	AssertJUnit.assertEquals(perm2, testPerm2);
       	AssertJUnit.assertEquals(permission2, testPermission2);
       	AssertJUnit.assertEquals(true, valid2);

       	String perm3 = "254";
       	AccessMask permission3 = AccessMask.fromString(perm3);

		slconfig2.setPermission(perm3);
		
       	AccessMask testPermission3 = slconfig2.getPermission();
       	String testPerm3 = slconfig2.getPermissionAsString();

       	AssertJUnit.assertEquals(perm3, testPerm3);
       	AssertJUnit.assertEquals(permission3, testPermission3);
	}

	/**
	 * Tests the setters and isValid with invalid configurations.
	 */
	@Test
    public final void testSetters() {
		AccessMask nn = null;
		SliceKindConfiguration slconfig = new SliceKindConfiguration(null, nn, null);

		// all properties missing
		boolean valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);

		String uri = "testuri";
		final String perm = "345";
		AccessMask permission = AccessMask.fromString(perm);
       	String sub = "testSub";

		// permission, subspaces missing
       	slconfig.setUri(uri);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);
		
       	// ubspaces missing, but mandatory
       	slconfig.setPermission(permission);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(true, valid);
       	
       	// complete
       	slconfig.setSubspaces(sub);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(true, valid);

       	try {
       		slconfig.setPermission("12");
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
       	try {
       		slconfig.setPermission("183");
       		AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
       	try {
       		String nnull = null;
       		slconfig.setPermission(nnull);
       		AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
       	try {
       		String nnull = null;
       		slconfig = new SliceKindConfiguration(null, nnull, null);
       		AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
	}
	
	/**
	 * Tests displayConfiguration().
	 */
	@Test
    public final void testDisplayConfiguration() {
		String uri = "testuri";
		final String perm = "345";
		AccessMask permission = AccessMask.fromString(perm);
      	// TODO add metasubspaces and test for it
       	String meta = "testSub";

		SliceKindConfiguration slconfig = new SliceKindConfiguration(uri, permission, meta);

		String s = "uri : '" + uri + "'; permission : '" + perm + "'; subspaces : '" + meta + "'; ";
		
       	AssertJUnit.assertEquals(s, slconfig.getStringRepresentation());		
       	AssertJUnit.assertEquals(s, slconfig.toString());		
	}
	
	/**
	 * Tests the method getSliceKindConfiguration.
	 */
	@Test
    public final void testGetSliceKindConfiguration() {
		SliceKind dummy = new SliceKind();
		
		String uri = "testuri";
		dummy.getId(uri);
		final long permission = 345;
		AccessMask perm = AccessMask.fromString((new Long(permission)).toString());
		dummy.setPermission(permission);
		Subspace dummySub = new Subspace();
		Set<Subspace> subspaces = new HashSet<Subspace>();
		subspaces.add(dummySub);		
		dummy.setSubspaces(subspaces);
						
		SliceKindConfiguration config = SliceKindConfiguration.getSliceKindConfiguration(dummy);

		AssertJUnit.assertEquals(true, config.isValid());
		AssertJUnit.assertEquals(uri, config.getUri());
		AssertJUnit.assertEquals(perm, config.getPermission());
		AssertJUnit.assertEquals(subspaces.toString(), config.getSubspaces());

	}

}
