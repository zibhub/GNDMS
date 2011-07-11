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
import java.util.GregorianCalendar;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import de.zib.gndms.common.model.dspace.SliceConfiguration;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * Tests the SliceConfiguration.
 * @author Ulrike Golas
 *
 */
public class SliceTest {
	
	/**
	 * Tests the method getSliceConfiguration.
	 * 
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	@Test
    public final void testGetSliceConfiguration() throws IOException, UpdateRejectedException {
		Slice dummy = new Slice();
		
		String directory = "testdir";
		dummy.setDirectoryId(directory);
		String owner = "me";
		dummy.setOwner(owner);
		final long termination = 20000;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(termination);
		dummy.setTerminationTime(cal);
				
		ConfigHolder config = Slice.getSliceConfiguration(dummy);

		AssertJUnit.assertEquals(true, SliceConfiguration.checkSliceConfiguration(config));
		AssertJUnit.assertEquals(directory, SliceConfiguration.getDirectory(config));
		AssertJUnit.assertEquals(owner, SliceConfiguration.getOwner(config));
		AssertJUnit.assertEquals(cal, SliceConfiguration.getTerminationTime(config));
	}

}
