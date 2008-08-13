package de.zib.gndms.infra.db;

import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMServiceHomeMockup;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.GNDMSystem.SysFactory;
import de.zib.gndms.infra.system.GridConfigMockup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;


/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */

public abstract class DbTest {
	private GridConfigMockup mockupConfig;
	private String gridName;
	private boolean setupEnvironment;
	private Log logger = LogFactory.getLog(DbTest.class);

	private GNDMSystem sys;
	private Runnable sysDestructor;
	protected GNDMServiceHome home;

    @Parameters({"gridName"})
    public DbTest(@Optional("c3grid") String gridName) {
        this.gridName = gridName;
        setupEnvironment();
    }

	public void setupEnvironment() {
        mockupConfig = new GridConfigMockup(gridName);
        File path = null;
        try {
            path = new File(mockupConfig.getGridPath());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (! (path.exists() && path.isDirectory())) {
            System.out.println("Invalid or missing $GLOBUS_LOCATION");
            throw new IllegalStateException("No globus toolkit found");
        }
	}

	protected void runDatabase()  throws ResourceException {
		SysFactory factory = new SysFactory(logger, mockupConfig);
		sys = factory.getInstance(false);
		sysDestructor = factory.createShutdownAction();
		home = new GNDMServiceHomeMockup(sys);
	}


	protected void shutdownDatabase() {
		try {
			sysDestructor.run();
		}
		finally {
			sys = null;
			sysDestructor = null;
			home = null;
		}
	}

	protected synchronized void eraseDatabase() {
        File path = null;
        try {
            path = new File(new File(mockupConfig.getGridPath(), "db"), mockupConfig.getGridName());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (path.exists())
			rmDirRecursively(path);
	}

	private static void rmDirRecursively(File fileParam) {
        for (File file : fileParam.listFiles()) {
            if (file.isDirectory())
                rmDirRecursively(file);
            else
                file.delete();
        }
        fileParam.delete();
	}
}
