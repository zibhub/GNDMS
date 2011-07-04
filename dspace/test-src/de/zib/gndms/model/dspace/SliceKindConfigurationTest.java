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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.model.dspace.WrongConfigurationException;
import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * Tests the SubspaceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceKindConfigurationTest{
	
	/**
	 * Tests the method checkSliceKindConfiguration(ConfigHolder) with a valid configuration
	 * and the access to the slice kind configuration fields.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 * @throws WrongConfigurationException 
	 */
	@Test
    public final void testCheckAndGet1() throws IOException, UpdateRejectedException, WrongConfigurationException {
		String uri = "testuri";
		final long permission = 345;
      	// TODO add metasubspaces and test for it
       	String meta = "testMetaSubspace";

		ConfigHolder testConfig = new MockSliceKindConfiguration(uri, permission, null);

       	AssertJUnit.assertEquals(true, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	
       	String testUri = SliceKindConfiguration.getUri(testConfig);
       	AccessMask testPermission = SliceKindConfiguration.getPermission(testConfig);
       	Set<MetaSubspace> testMeta = SliceKindConfiguration.getMetaSubspaces(testConfig);

       	AssertJUnit.assertEquals(uri, testUri);
       	AssertJUnit.assertEquals(Long.toString(permission), testPermission.getAsString());
       	AssertJUnit.assertEquals(null, testMeta);

       	testConfig = new MockSliceKindConfiguration(uri, permission, meta);
       	AssertJUnit.assertEquals(true, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	testMeta = SliceKindConfiguration.getMetaSubspaces(testConfig);

       	// TODO adapt
       	AssertJUnit.assertEquals(new HashSet<MetaSubspace>(), testMeta);
	}

	/**
	 * Tests the method checkSliceKindConfiguration(ConfigHolder) with invalid configurations.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	@Test
    public final void testCheckAndGet2() throws IOException, UpdateRejectedException {
		ConfigHolder testConfig = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = testConfig.newEditor(visitor);
		testConfig.setObjectMapper(objectMapper);
		
		final long testValue = 6000;
		
		// empty node
       	AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
			SliceKindConfiguration.getUri(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}
       	
       	// only uri set - permission is missing
		JsonNode node = ConfigHolder.parseSingle(factory, SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.URI,"testUri"));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
       		SliceKindConfiguration.getPermission(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// uri and permission set - but invalid permission (text)
		node = ConfigHolder.parseSingle(factory, "{ '" + SliceKindConfiguration.PERMISSION +"': 'test' }");
      	// node = ConfigHolder.parseSingle(factory, SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.PERMISSION, "test"));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
       		SliceKindConfiguration.getPermission(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// uri and permission set - but invalid permission (too long)
       	node = ConfigHolder.parseSingle(factory, SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.PERMISSION, 23456));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
       		SliceKindConfiguration.getPermission(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// uri and permission set - but invalid permission (wrong number)
       	node = ConfigHolder.parseSingle(factory, SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.PERMISSION, 914));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
       		SliceKindConfiguration.getPermission(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// wrong type of uri value - number instead of string
		node = ConfigHolder.parseSingle(factory, SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.URI, testValue));
		testConfig.update(editor, node);

		AssertJUnit.assertEquals(false, SliceKindConfiguration.checkSliceKindConfiguration(testConfig));
       	try {
       		SliceKindConfiguration.getUri(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// TODO test for wrong meta-subspace
	}
	
	/**
	 * Tests the method getSliceKindConfiguration.
	 */
	@Test
    public final void testGetSliceKindConfiguration() throws IOException, UpdateRejectedException {
		SliceKind dummy = new SliceKind();
		
		String uri = "testuri";
		dummy.setURI(uri);
		long nr = 345;
		AccessMask permission = AccessMask.fromString(Long.toString(nr));
		dummy.setPermission(permission);
		MetaSubspace dummyMeta = new MetaSubspace();
		Set<MetaSubspace> metaSubspaces = new HashSet<MetaSubspace>();
		metaSubspaces.add(dummyMeta);		
		dummy.setMetaSubspaces(metaSubspaces);
						
		ConfigHolder config = SliceKindConfiguration.getSliceKindConfiguration(dummy);

		AssertJUnit.assertEquals(true, SliceKindConfiguration.checkSliceKindConfiguration(config));
		AssertJUnit.assertEquals(uri, SliceKindConfiguration.getUri(config));
		AssertJUnit.assertEquals(permission.getAsString(), SliceKindConfiguration.getPermission(config).getAsString());
		
		// TODO test for meta-subspaces
		// AssertJUnit.assertEquals(metaSubspaces, SliceKindConfiguration.getMetaSubspaces(config));

	}

}
