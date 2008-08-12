package de.zib.gndms.infra.db

import de.zib.gndms.model.dspace.DSpace
import de.zib.gndms.infra.model.SingletonGridResourceModelHandler
import de.zib.gndms.infra.model.DefaultModelInitializer

import org.junit.*
import static org.junit.Assert.*
import static org.junit.Assume.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 12.08.2008 Time: 16:06:58
 */
class SingletonTest extends DbTest {
	DSpace getSingletonInstance() {
		SingletonGridResourceModelHandler mH =
			new SingletonGridResourceModelHandler(DSpace.class, home);
		DSpace model = (DSpace) mH.getSingleModel(null, "findDSpaceInstances",
		                                          new DefaultModelInitializer<DSpace>())
		return model
	}

	@Test
	void assertHasDSpaceSingleton() {
		assertEnvironmentSetup()
		String id1 = runForSingleton(true)
		String id2 = runForSingleton(false)
		String id3 = runForSingleton(true)

		assert id1 == id2 : "Detected overwritten singleton"
		assert id3 != id2 : "Same id after recreation of database (RNG issue?)"
	}


	private String runForSingleton(boolean eraseDb) {
		try {
			if (eraseDb)
				eraseDatabase()
			runDatabase()
			DSpace instance = getSingletonInstance()
			String id = instance.getId()
			assertNotNull(instance)
			return id
		}
		finally { shutdownDatabase() }
	}
}