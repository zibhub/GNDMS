package de.zib.gndms.infra.system;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.infra.model.DefaultModelInitializer;
import de.zib.gndms.infra.model.ModelInitializer;
import de.zib.gndms.infra.model.SingletonGridResourceModelHandler;
import de.zib.gndms.model.dspace.DSpace;
import org.globus.wsrf.ResourceException;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/**
 * ThingAMagic.
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 12.08.2008 Time: 16:06:58
 */
public class SingletonTest extends SysTestBase {
	public DSpace getSingletonInstance() throws ResourceException {
		SingletonGridResourceModelHandler mH =
			new SingletonGridResourceModelHandler(DSpace.class, home);
		DSpace model = (DSpace) mH.getSingleModel(null, "findDSpaceInstances",
                                                  (ModelInitializer)
                                                          new DefaultModelInitializer<DSpace>());
		return model;
	}


    @Parameters({"gridName"})
    public SingletonTest(@Optional("c3grid") final String gridName) {
        super(gridName);
    }


    @Test(groups={"sys"})
	void assertHasDSpaceSingleton() throws ResourceException {
		String id1 = runForSingleton(true);
		String id2 = runForSingleton(false);
		String id3 = runForSingleton(true);

		assert id1.equals(id2) : "Detected overwritten singleton";
		assert !id3.equals(id2) : "Same id after recreation of database (RNG issue?)";
	}


	private String runForSingleton(boolean eraseDb) throws ResourceException {
		try {
			if (eraseDb)
				eraseDatabase();
			runDatabase();
			DSpace instance = getSingletonInstance();
			String id = instance.getId();
			assertNotNull(instance);
			return id;
		}
		finally { shutdownDatabase(); }
	}
}
