package de.zib.gndms.common.model.common;

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
import org.testng.Assert;
import org.testng.AssertJUnit;

import de.zib.gndms.common.model.common.AccessMask.AccessFlags;
import de.zib.gndms.common.model.common.AccessMask.Ugo;

/**
 * Tests the AccessMask.
 * 
 * @author Ulrike Golas
 */
public class AccessMaskTest {

	/**
	 * Tests the AccessFlags.
	 */
	@Test
    public final void testAccessFlags() {
		final int flagValue0 = 0;
		final int flagValue1 = 1;
		final int flagValue2 = 2;
		final int flagValue3 = 3;
		final int flagValue4 = 4;
		final int flagValue5 = 5;
		final int flagValue6 = 6;
		final int flagValue7 = 7;
		final int flagValue8 = 8;
		final int flagValueNeg = -1;
		
		AssertJUnit.assertEquals(AccessFlags.NONE.getMask(), flagValue0);
		AssertJUnit.assertEquals(AccessFlags.EXECUTABLE.getMask(), flagValue1);
		AssertJUnit.assertEquals(AccessFlags.WRITABLE.getMask(), flagValue2);
		AssertJUnit.assertEquals(AccessFlags.WRITEEXEC.getMask(), flagValue3);
		AssertJUnit.assertEquals(AccessFlags.READABLE.getMask(), flagValue4);
		AssertJUnit.assertEquals(AccessFlags.READEXECUT.getMask(), flagValue5);
		AssertJUnit.assertEquals(AccessFlags.READWRITE.getMask(), flagValue6);
		AssertJUnit.assertEquals(AccessFlags.ALL.getMask(), flagValue7);

		String value = "0";
		
		AccessFlags  flags = AccessFlags.fromString(value);
		AssertJUnit.assertEquals(flags.getMask(), flagValue0);
		AssertJUnit.assertEquals(flags.toString(), value);

		char val = '0';
		
		flags = AccessFlags.fromChar(val);
		AssertJUnit.assertEquals(flags.getMask(), flagValue0);
		
		flags = AccessFlags.flagsForMask(flagValue0);
		AssertJUnit.assertEquals(flags.getMask(), flagValue0);

		try {
			flags = AccessFlags.flagsForMask(flagValue8);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}
		
		try {
			flags = AccessFlags.flagsForMask(flagValueNeg);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);			
		}
	}

	/**
	 * Tests the Ugo.
	 */
	@Test
    public final void testUgo() {
		final int flagValue0 = 0;
		final int flagValue1 = 1;
		final int flagValue2 = 2;
		
		AssertJUnit.assertEquals(Ugo.USER.ordinal(), flagValue0);
		AssertJUnit.assertEquals(Ugo.GROUP.ordinal(), flagValue1);
		AssertJUnit.assertEquals(Ugo.OTHER.ordinal(), flagValue2);		
	}

	/**
	 * Tests the AccessMask.
	 */
	@Test
    public final void testAccessMask() {
		final int flagValue0 = 5;
		final int flagValue1 = 1;
		final int flagValue2 = 3;
		final String flagValue = "513";
		AccessMask mask = AccessMask.fromString(flagValue);

		AssertJUnit.assertEquals(mask.toString(), flagValue);
		AssertJUnit.assertEquals(mask.getAsString(), flagValue);
		AssertJUnit.assertNull(mask.getSpecial());

		AccessFlags testValue = mask.getUserAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue0), testValue);

		testValue = mask.getGroupAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue1), testValue);
		
		testValue = mask.getOtherAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue2), testValue);

		final int flagValue3 = 2;
		final int flagValue4 = 6;
		final int flagValue5 = 4;

		mask.setUserAccess(flagValue3);
		testValue = mask.getUserAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue3), testValue);

		mask.setGroupAccess(flagValue4);
		testValue = mask.getGroupAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue4), testValue);

		mask.setOtherAccess(flagValue5);
		testValue = mask.getOtherAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue5), testValue);

		mask.setUserAccess(AccessFlags.READEXECUT);
		testValue = mask.getUserAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue0), testValue);

		mask.setGroupAccess(AccessFlags.EXECUTABLE);
		testValue = mask.getGroupAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue1), testValue);

		mask.setOtherAccess(AccessFlags.WRITEEXEC);
		testValue = mask.getOtherAccess();
		AssertJUnit.assertEquals(AccessFlags.flagsForMask(flagValue2), testValue);

		final Integer special = 3;
		final String flagValueSpecial = "3513";
		mask = AccessMask.fromString(flagValueSpecial);

		AssertJUnit.assertEquals(mask.toString(), flagValueSpecial);
		AssertJUnit.assertEquals(mask.getAsString(), flagValueSpecial);
		AssertJUnit.assertEquals(mask.getSpecial(), special);

		final String flagValueExtra = "33513";
		try {
			mask = AccessMask.fromString(flagValueExtra);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertNotNull(e);
		}

		final int max = 3;
		final AccessFlags[] allFlags = new AccessFlags[max];
		allFlags[0] = AccessFlags.READEXECUT;
		allFlags[1] = AccessFlags.EXECUTABLE;
		allFlags[2] = AccessFlags.WRITEEXEC;
		
		mask.setAccess(allFlags);
		testValue = mask.getUserAccess();
		AssertJUnit.assertEquals(AccessFlags.READEXECUT, testValue);
		testValue = mask.getGroupAccess();
		AssertJUnit.assertEquals(AccessFlags.EXECUTABLE, testValue);
		testValue = mask.getOtherAccess();
		AssertJUnit.assertEquals(AccessFlags.WRITEEXEC, testValue);

		final AccessFlags[] allFlags2 = new AccessFlags[max + 1];
		allFlags2[0] = AccessFlags.READEXECUT;
		allFlags2[1] = AccessFlags.EXECUTABLE;
		allFlags2[2] = AccessFlags.WRITEEXEC;
		allFlags2[max] = AccessFlags.WRITABLE;

		mask.setAccess(allFlags2);
	}
	
	/**
	 * Tests handling of flags and ugo rights.
	 */
	@Test
    public final void testUgoFlags() {
		final String flagValue = "512";
		AccessMask mask = AccessMask.fromString(flagValue);

		AssertJUnit.assertEquals(true, mask.queryFlagsOn(Ugo.USER, AccessFlags.READABLE));
		AssertJUnit.assertEquals(false, mask.queryFlagsOn(Ugo.USER, AccessFlags.WRITABLE));
		AssertJUnit.assertEquals(true, mask.queryFlagsOn(Ugo.USER, AccessFlags.EXECUTABLE));
		AssertJUnit.assertEquals(false, mask.queryFlagsOn(Ugo.GROUP, AccessFlags.READABLE));
		AssertJUnit.assertEquals(false, mask.queryFlagsOn(Ugo.GROUP, AccessFlags.WRITABLE));
		AssertJUnit.assertEquals(true, mask.queryFlagsOn(Ugo.GROUP, AccessFlags.EXECUTABLE));
		AssertJUnit.assertEquals(true, mask.queryFlagsOff(Ugo.OTHER, AccessFlags.READABLE));
		AssertJUnit.assertEquals(false, mask.queryFlagsOff(Ugo.OTHER, AccessFlags.WRITABLE));
		AssertJUnit.assertEquals(true, mask.queryFlagsOff(Ugo.OTHER, AccessFlags.EXECUTABLE));

		mask.addFlag(Ugo.GROUP, AccessFlags.EXECUTABLE);
		AssertJUnit.assertEquals(flagValue, mask.toString());
		
		mask.addFlag(Ugo.GROUP, AccessFlags.WRITABLE);
		AssertJUnit.assertEquals("532", mask.toString());

		mask.removeFlag(Ugo.GROUP, AccessFlags.WRITABLE);
		AssertJUnit.assertEquals(flagValue, mask.toString());
}
	
	/**
	 * Tests equals() and hashcode().
	 */
	@Test
    public final void testEquals() {
		final String flagValue = "513";

		AccessMask mask = AccessMask.fromString(flagValue);
		AccessMask mask2 = AccessMask.fromString(flagValue);
		
       	AssertJUnit.assertEquals(false, mask == mask2);
       	AssertJUnit.assertEquals(true, mask.equals(mask2));
       	AssertJUnit.assertEquals(true, mask.equals(mask));
       	AssertJUnit.assertEquals(true, mask2.equals(mask));
       	AssertJUnit.assertEquals(mask.hashCode(), mask2.hashCode());

		final String flagValue2 = "3513";
		mask2 = AccessMask.fromString(flagValue2);

       	AssertJUnit.assertEquals(false, mask.equals(mask2));
       	AssertJUnit.assertEquals(false, mask2.equals(mask));
       	AssertJUnit.assertEquals(false, mask.equals(null));
       	AssertJUnit.assertEquals(false, mask.equals(flagValue));
	}

}
