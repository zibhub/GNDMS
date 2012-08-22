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

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.zib.gndms.common.dspace.SliceConfiguration;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.model.dspace.Slice;

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
		final long size = 4000;
		GregorianCalendar cal = new GregorianCalendar();

		SliceConfiguration slconfig = new SliceConfiguration(size, cal);
		SliceConfiguration slconfig2 = new SliceConfiguration(size, cal);
		
       	AssertJUnit.assertEquals(false, slconfig == slconfig2);
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig2));
       	AssertJUnit.assertEquals(true, slconfig.equals(slconfig));
       	AssertJUnit.assertEquals(true, slconfig2.equals(slconfig));
       	AssertJUnit.assertEquals(slconfig.hashCode(), slconfig2.hashCode());

		final long size2 = 6000;
		slconfig2 = new SliceConfiguration(size2, cal);

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
		final long size = 4000;
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();

		SliceConfiguration slconfig = new SliceConfiguration(size, cal);
		       	
       	long testSize = slconfig.getSize();
       	Calendar testTime = slconfig.getTerminationTime();
       	long testValue = slconfig.getTerminationTimeAsLong();
       	boolean valid = slconfig.isValid();
       	
       	AssertJUnit.assertEquals(size, testSize);
       	AssertJUnit.assertEquals(value, testValue);
       	AssertJUnit.assertEquals(cal, testTime);
       	AssertJUnit.assertEquals(true, valid);

		SliceConfiguration slconfig2 = new SliceConfiguration(size, value);
       	testTime = slconfig2.getTerminationTime();
       	valid = slconfig2.isValid();
		
       	AssertJUnit.assertEquals(slconfig.getStringRepresentation(), slconfig2.getStringRepresentation());
       	AssertJUnit.assertEquals(cal, testTime);
       	AssertJUnit.assertEquals(true, valid);

		final long size2 = 8000;
		GregorianCalendar cal2 = new GregorianCalendar();
		final long value2 = cal2.getTimeInMillis();

		slconfig2.setSize(size2);
		slconfig2.setTerminationTime(cal2);

       	long testSize2 = slconfig2.getSize();
       	Calendar testTime2 = slconfig2.getTerminationTime();
       	long testValue2 = slconfig2.getTerminationTimeAsLong();
       	boolean valid2 = slconfig2.isValid();

       	AssertJUnit.assertEquals(size2, testSize2);
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
		SliceConfiguration slconfig = new SliceConfiguration(0, null);

		// termination missing
		boolean valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);

		GregorianCalendar cal = new GregorianCalendar();

       	// complete
       	slconfig.setTerminationTime(cal);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(true, valid);

		// size negative
        final long size = -3000;
       	slconfig.setSize(size);
		
		valid = slconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);
	}
	
	/**
	 * Tests displayConfiguration().
	 */
	@Test
    public final void testDisplayConfiguration() {
        final long size = 4000;
		GregorianCalendar cal = new GregorianCalendar();
		final long value = cal.getTimeInMillis();

		SliceConfiguration slconfig = new SliceConfiguration(size, cal);

		String s = "size : '" + size + "'; termination : '" + value + "'; ";
		
       	AssertJUnit.assertEquals(s, slconfig.getStringRepresentation());		
       	AssertJUnit.assertEquals(s, slconfig.toString());		
	}
	
	/**
	 * Tests the method getSliceConfiguration.
	 */
	@Test
    public final void testGetSliceConfiguration() {
		Slice dummy = new Slice(null, null, null, null);
		
		long size = 4000;
		dummy.setTotalStorageSize(size);
		final long termination = 20000;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(termination);
		dummy.setTerminationTime(cal);
				
		SliceConfiguration config = SliceConfiguration.getSliceConfiguration(dummy);

		AssertJUnit.assertEquals(true, config.isValid());
		AssertJUnit.assertEquals(size, config.getSize());
		AssertJUnit.assertEquals(cal, config.getTerminationTime());
	}

}
