package de.zib.gndms.infra.db

import de.zib.gndms.infra.system.GNDMSystem
import de.zib.gndms.infra.service.GNDMServiceHome
import de.zib.gndms.infra.model.GridEntityModelHandler
import de.zib.gndms.model.dspace.DSpace
import de.zib.gndms.infra.system.GridConfigMockup
import de.zib.gndms.infra.service.GNDMServiceHomeMockup
import de.zib.gndms.infra.model.DefaultModelInitializer
import de.zib.gndms.infra.model.SingletonGridResourceModelHandler
import de.zib.gndms.infra.system.GNDMSystem.SysFactory

import org.junit.*
import static org.junit.Assert.*
import static org.junit.Assume.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */

class DbTest {
	private static GridConfigMockup mockupConfig
	private static String gridName = "c3grid";
	private static boolean setupEnvironment
	private static Log logger = LogFactory.getLog(DbTest.class)

	GNDMSystem sys
	Runnable sysDestructor
	GNDMServiceHome home


	@BeforeClass
	def static void setupEnvironment() {
		mockupConfig = new GridConfigMockup(gridName)
		File path = new File(mockupConfig.getGridPath())
		if (! (path.exists() && path.isDirectory()))
			System.out.println("Invalid or missing \$GLOBUS_LOCATION")
		else
			setupEnvironment = true
	}

	private def assertEnvironmentSetup() {
		assertTrue("Invalid environment setup", setupEnvironment)
	}

	def synchronized void runDatabase()  throws ResourceException {
		SysFactory factory = new SysFactory(logger, mockupConfig);
		sys = factory.getInstance();
		sysDestructor = factory.createShutdownAction()
		home = new GNDMServiceHomeMockup(sys);
	}


	def synchronized void shutdownDatabase() {
		try {
			sysDestructor.run()
		}
		finally {
			sys = null
			sysDestructor = null
			home = null
		}
	}

	def DSpace getSingletonInstance() {
		SingletonGridResourceModelHandler mH =
			new SingletonGridResourceModelHandler(DSpace.class, home);
		DSpace model = (DSpace) mH.getSingleModel(null, "findDSpaceInstances",
		                                          new DefaultModelInitializer<DSpace>())
		return model
	}


	@Test
	def void assertHasDSpaceSingleton() {
		assertEnvironmentSetup()
		try {
			runDatabase()
			DSpace instance = getSingletonInstance()
			assertNotNull(instance)
		}
		finally { shutdownDatabase() }
	}


}
