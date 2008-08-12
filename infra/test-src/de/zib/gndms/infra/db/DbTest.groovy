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
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */

@RunWith(Suite.class) @SuiteClasses([SingletonTest.class])
class DbTestSuite {} 

abstract class DbTest {
	private static GridConfigMockup mockupConfig
	private static String gridName = "c3grid";
	private static boolean setupEnvironment
	private static Log logger = LogFactory.getLog(DbTest.class)

	GNDMSystem sys
	Runnable sysDestructor
	GNDMServiceHome home


	@BeforeClass
	static void setupEnvironment() {
		mockupConfig = new GridConfigMockup(gridName)
		File path = new File(mockupConfig.getGridPath())
		if (! (path.exists() && path.isDirectory()))
			System.out.println("Invalid or missing \$GLOBUS_LOCATION")
		else
			setupEnvironment = true
	}

	protected void assertEnvironmentSetup() {
		assertTrue("Invalid environment setup", setupEnvironment)
	}

	protected synchronized void runDatabase()  throws ResourceException {
		SysFactory factory = new SysFactory(logger, mockupConfig);
		sys = factory.getInstance(false);
		sysDestructor = factory.createShutdownAction()
		home = new GNDMServiceHomeMockup(sys);
	}


	protected synchronized void shutdownDatabase() {
		try {
			sysDestructor.run()
		}
		finally {
			sys = null
			sysDestructor = null
			home = null
		}
	}

	protected synchronized void eraseDatabase() {
		File path = new File(new File(mockupConfig.getGridPath(), "db"), mockupConfig.getGridName())
		if (path.exists())
			rmDirRecursively(path)
	}

	private static void rmDirRecursively(File fileParam) {
		fileParam.eachFile { File it -> it.delete() }
		fileParam.eachDir { File it -> rmDirRecursively(it) }
		fileParam.delete()
	}
}
