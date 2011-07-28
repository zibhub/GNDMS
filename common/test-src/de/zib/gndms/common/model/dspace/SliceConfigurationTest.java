package de.zib.gndms.common.model.dspace;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

/**
 * Tests the SliceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceConfigurationTest {

	/**
	 * Tests equals() and hashcode().
	 */
	@Test
    public final void testEquals() {
		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();

		SliceConfiguration slconfig = new SliceConfiguration(directory, owner, cal);
		SliceConfiguration slconfig2 = new SliceConfiguration(directory, owner, cal);
		
       	AssertJUnit.assertEquals(false, slconfig == slconfig2);
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig2));
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig));
       	AssertJUnit.assertEquals(true, slconfig2.equals(slconfig));
       	AssertJUnit.assertEquals(slconfig.hashCode(), slconfig2.hashCode());

		directory = "slice2";
		slconfig2 = new SliceConfiguration(directory, owner, cal);

       	AssertJUnit.assertEquals(false, slconfig.equals(slconfig2));
       	AssertJUnit.assertEquals(false, slconfig2.equals(slconfig));
       	AssertJUnit.assertEquals(false, slconfig.equals(null));
       	AssertJUnit.assertEquals(false, slconfig.equals(cal));
	}
       	
	/**
	 * Tests the constructor and getters, setters, and isValid() with valid arguments.
	 */
	@Test
    public final void testConstructors() {
		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();

		SliceConfiguration slconfig = new SliceConfiguration(directory, owner, cal);
		       	
       	String testDirectory = slconfig.getDirectory();
       	String testOwner = slconfig.getOwner();
       	Calendar testTime = slconfig.getTerminationTime();
       	long testValue = slconfig.getTerminationTimeAsLong();
       	boolean valid = slconfig.isValid();
       	
       	AssertJUnit.assertEquals(directory, testDirectory);
       	AssertJUnit.assertEquals(owner, testOwner);
       	AssertJUnit.assertEquals(value, testValue);
       	AssertJUnit.assertEquals(cal, testTime);
       	AssertJUnit.assertEquals(true, valid);

		SliceConfiguration slconfig2 = new SliceConfiguration(directory, owner, value);
       	testTime = slconfig2.getTerminationTime();
       	valid = slconfig2.isValid();
		
       	AssertJUnit.assertEquals(slconfig.displayConfiguration(), slconfig2.displayConfiguration());
       	AssertJUnit.assertEquals(cal, testTime);
       	AssertJUnit.assertEquals(true, valid);

		String directory2 = "sliceslice";
		String owner2 = "notme";
		GregorianCalendar cal2 = new GregorianCalendar();
		final long value2 = cal2.getTimeInMillis();

		slconfig2.setDirectory(directory2);
		slconfig2.setOwner(owner2);
		slconfig2.setTerminationTime(cal2);

       	String testDirectory2 = slconfig2.getDirectory();
       	String testOwner2 = slconfig2.getOwner();
       	Calendar testTime2 = slconfig2.getTerminationTime();
       	long testValue2 = slconfig2.getTerminationTimeAsLong();
       	boolean valid2 = slconfig2.isValid();

       	AssertJUnit.assertEquals(directory2, testDirectory2);
       	AssertJUnit.assertEquals(owner2, testOwner2);
       	AssertJUnit.assertEquals(value2, testValue2);
       	AssertJUnit.assertEquals(cal2, testTime2);
       	AssertJUnit.assertEquals(true, valid2);

		GregorianCalendar cal3 = new GregorianCalendar();
		final long value3 = cal3.getTimeInMillis();

		slconfig2.setTerminationTime(value3);
		
       	Calendar testTime3 = slconfig2.getTerminationTime();
       	long testValue3 = slconfig2.getTerminationTimeAsLong();

       	AssertJUnit.assertEquals(value3, testValue3);
       	AssertJUnit.assertEquals(cal3, testTime3);
	}

	/**
	 * Tests the setters and isValid with invalid configurations.
	 */
	@Test
    public final void testSetters() {
		SliceConfiguration slconfig = new SliceConfiguration(null, null, null);

		// all properties missing
		boolean valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);

		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();

		// owner, termination missing
       	slconfig.setDirectory(directory);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);
		
       	// termination still missing
       	slconfig.setOwner(owner);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);
       	
       	// complete
       	slconfig.setTerminationTime(cal);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(true, valid);

	}
	
	/**
	 * Tests displayConfiguration().
	 */
	@Test
    public final void testDisplayConfiguration() {
		String directory = "slice";
		String owner = "me";
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();

		SliceConfiguration slconfig = new SliceConfiguration(directory, owner, cal);

		String s = "directory : '" + directory + "'; owner : '" + owner + "'; termination : '" + value + "'; ";
		
       	AssertJUnit.assertEquals(s, slconfig.displayConfiguration());		
       	AssertJUnit.assertEquals(s, slconfig.toString());		
	}
}
