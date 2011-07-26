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

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.common.logic.config.SetupMode;


/**
 * Tests the SubspaceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SubspaceConfigurationTest {
	
	/**
	 * Tests equals() and hashcode().
	 */
	@Test
    public final void testEquals() {
		String path = "testpath";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long size = 6000;
		String mode = "UPDATE";

		SubspaceConfiguration suconfig = new SubspaceConfiguration(path, gsiftp, visible, size, mode);
		SubspaceConfiguration suconfig2 = new SubspaceConfiguration(path, gsiftp, visible, size, mode);
		
       	AssertJUnit.assertEquals(false, suconfig == suconfig2);
       	AssertJUnit.assertEquals(true, suconfig.equals(suconfig2));
       	AssertJUnit.assertEquals(true, suconfig.equals(suconfig));
       	AssertJUnit.assertEquals(true, suconfig2.equals(suconfig));
       	AssertJUnit.assertEquals(suconfig.hashCode(), suconfig2.hashCode());

		visible = false;
		suconfig2 = new SubspaceConfiguration(path, gsiftp, visible, size, mode);

       	AssertJUnit.assertEquals(false, suconfig.equals(suconfig2));
       	AssertJUnit.assertEquals(false, suconfig2.equals(suconfig));
       	AssertJUnit.assertEquals(false, suconfig.equals(null));
       	AssertJUnit.assertEquals(false, suconfig.equals(path));
	}

	/**
	 * Tests the constructor and getters, setters, and isValid() with valid arguments.
	 */
	@Test
    public final void testConstructors() {
		String path = "testpath";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long size = 6000;
		String mode = "UPDATE";
		SetupMode setup = SetupMode.UPDATE;
		
		SubspaceConfiguration suconfig = new SubspaceConfiguration(path, gsiftp, visible, size, setup);
		       	
       	String testPath = suconfig.getPath();
       	String testGsiftp = suconfig.getGsiFtpPath();
       	boolean testVisible = suconfig.isVisible();
       	long testSize = suconfig.getSize();
       	SetupMode testSetup = suconfig.getMode();
       	boolean valid = suconfig.isValid();
       	
       	AssertJUnit.assertEquals(path, testPath);
       	AssertJUnit.assertEquals(gsiftp, testGsiftp);
       	AssertJUnit.assertEquals(visible, testVisible);
       	AssertJUnit.assertEquals(size, testSize);
       	AssertJUnit.assertEquals(setup, testSetup);
       	AssertJUnit.assertEquals(true, valid);

		SubspaceConfiguration suconfig2 = new SubspaceConfiguration(path, gsiftp, visible, size, mode);
       	testSetup = suconfig2.getMode();
       	valid = suconfig2.isValid();
		
       	AssertJUnit.assertEquals(suconfig.displayConfiguration(), suconfig2.displayConfiguration());
       	AssertJUnit.assertEquals(setup, testSetup);

		String path2 = "pathtest";
		String gsiftp2 = "testgsiftp";
		boolean visible2 = false;
		final long size2 = 8000;
		SetupMode setup2 = SetupMode.CREATE;

		suconfig2.setPath(path2);
		suconfig2.setGsiFtpPath(gsiftp2);
		suconfig2.setVisible(visible2);
		suconfig2.setSize(size2);
		suconfig2.setMode(setup2);

       	String testPath2 = suconfig2.getPath();
       	String testGsiftp2 = suconfig2.getGsiFtpPath();
       	boolean testVisible2 = suconfig2.isVisible();
       	long testSize2 = suconfig2.getSize();
       	SetupMode testSetup2 = suconfig2.getMode();
       	boolean valid2 = suconfig2.isValid();

       	AssertJUnit.assertEquals(path2, testPath2);
       	AssertJUnit.assertEquals(gsiftp2, testGsiftp2);
       	AssertJUnit.assertEquals(visible2, testVisible2);
       	AssertJUnit.assertEquals(size2, testSize2);
       	AssertJUnit.assertEquals(setup2, testSetup2);
       	AssertJUnit.assertEquals(true, valid2);

		String mode3 = "DELETE";

		suconfig2.setMode(mode3);
		
       	SetupMode testSetup3 = suconfig2.getMode();

       	AssertJUnit.assertEquals(SetupMode.DELETE, testSetup3);
	}

	/**
	 * Tests the setters and isValid with invalid configurations.
	 */
	@Test
    public final void testSetters() {
		SubspaceConfiguration suconfig = new SubspaceConfiguration(null, null, true, 0, SetupMode.CREATE);

		// path and gsiftp missing
		boolean valid = suconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);

		String path = "testpath";
		String gsiftp = "gsiftp";

		// gsiftp missing
       	suconfig.setPath(path);
		
		valid = suconfig.isValid();
       	AssertJUnit.assertEquals(false, valid);
		       	
       	// complete
       	suconfig.setGsiFtpPath(gsiftp);
		
		valid = suconfig.isValid();
       	AssertJUnit.assertEquals(true, valid);

       	try {
       		suconfig.setMode("test");
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
       	try {
       		String nn = null;
       		suconfig.setMode(nn);
		} catch (WrongConfigurationException e) {
			AssertJUnit.assertNotNull(e);
		}
}
	
	/**
	 * Tests displayConfiguration().
	 */
	@Test
    public final void testDisplayConfiguration() {
		String path = "testpath";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long size = 6000;
		SetupMode setup = SetupMode.UPDATE;

		SubspaceConfiguration suconfig = new SubspaceConfiguration(path, gsiftp, visible, size, setup);

		String s = "path : '" + path + "'; gsiftppath : '" + gsiftp 
			+ "'; visible : '" + visible + "'; size : '" + size + "'; mode : '" + setup + "'; ";
		
       	AssertJUnit.assertEquals(s, suconfig.displayConfiguration());		
       	AssertJUnit.assertEquals(s, suconfig.toString());		
	}
}
