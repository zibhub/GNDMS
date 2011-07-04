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
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.model.dspace.SliceConfiguration;
import de.zib.gndms.model.dspace.WrongConfigurationException;
import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * Tests the SliceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceConfigurationTest{
	
	/**
	 * Tests the method checkSliceConfiguration(ConfigHolder) with a valid configuration
	 * and the access to the slice configuration fields.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 * @throws WrongConfigurationException 
	 */
	@Test
    public final void testCheckAndGet1() throws IOException, UpdateRejectedException, WrongConfigurationException {
		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();

		ConfigHolder testConfig = new MockSliceConfiguration(directory, owner, value);

       	AssertJUnit.assertEquals(true, SliceConfiguration.checkSliceConfiguration(testConfig));
       	
       	String testDirectory = SliceConfiguration.getDirectory(testConfig);
       	String testOwner = SliceConfiguration.getOwner(testConfig);
       	Calendar testTime = SliceConfiguration.getTerminationTime(testConfig);
       	long testValue = testTime.getTimeInMillis();

       	AssertJUnit.assertEquals(directory, testDirectory);
       	AssertJUnit.assertEquals(owner, testOwner);
       	AssertJUnit.assertEquals(value, testValue);
       	AssertJUnit.assertEquals(cal, testTime);

	}

	/**
	 * Tests the method checkSliceConfiguration(ConfigHolder) with invalid configurations.
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
		
		GregorianCalendar cal = new GregorianCalendar();
		final long testValue = cal.getTimeInMillis();
		
		// empty node
       	AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getDirectory(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}
       	
       	// only directory set - owner path is missing
		JsonNode node = ConfigHolder.parseSingle(factory, SliceConfiguration.createSingleEntry(SliceConfiguration.DIRECTORY, "slice"));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getOwner(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// only directory and owner set - termination time is missing
       	node = ConfigHolder.parseSingle(factory, SliceConfiguration.createSingleEntry(SliceConfiguration.OWNER, "me"));
		testConfig.update(editor, node);

       	AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getTerminationTime(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// wrong type of directory value - number instead of string
		node = ConfigHolder.parseSingle(factory, SliceConfiguration.createSingleEntry(SliceConfiguration.DIRECTORY, testValue));
		testConfig.update(editor, node);

		AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getDirectory(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// wrong type of owner value - number instead of string
		node = ConfigHolder.parseSingle(factory, SliceConfiguration.createSingleEntry(SliceConfiguration.OWNER, testValue));
		testConfig.update(editor, node);

		AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getOwner(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

       	// wrong type of termination time value - string instead of number
		node = ConfigHolder.parseSingle(factory, SliceConfiguration.createSingleEntry(SliceConfiguration.TERMINATION, "test"));
		testConfig.update(editor, node);

		AssertJUnit.assertEquals(false, SliceConfiguration.checkSliceConfiguration(testConfig));
       	try {
			SliceConfiguration.getTerminationTime(testConfig);
			AssertJUnit.fail();
		} catch (WrongConfigurationException e) {
		}

	}
	
	/**
	 * Tests the method getSliceConfiguration.
	 */
	@Test
    public final void testGetSliceConfiguration() throws IOException, UpdateRejectedException {
		Slice dummy = new Slice();
		
		String directory = "testdir";
		dummy.setDirectoryId(directory);
		String owner = "me";
		dummy.setOwner(owner);
		long termination = 20000;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(termination);
		dummy.setTerminationTime(cal);
				
		ConfigHolder config = SliceConfiguration.getSliceConfiguration(dummy);

		AssertJUnit.assertEquals(true, SliceConfiguration.checkSliceConfiguration(config));
		AssertJUnit.assertEquals(directory, SliceConfiguration.getDirectory(config));
		AssertJUnit.assertEquals(owner, SliceConfiguration.getOwner(config));
		AssertJUnit.assertEquals(cal, SliceConfiguration.getTerminationTime(config));
	}

}
