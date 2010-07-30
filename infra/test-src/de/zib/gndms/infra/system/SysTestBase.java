package de.zib.gndms.infra.system;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMServiceHomeMockup;
import de.zib.gndms.infra.service.GridConfigMockup;
import de.zib.gndms.infra.system.GNDMSystem.SysFactory;
import de.zib.gndms.model.test.ModelEntityTestBase;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.EntityAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;


/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */

public abstract class SysTestBase {
	private GridConfigMockup mockupConfig;
	private String gridName;
	private boolean setupEnvironment;
	private Log logger = LogFactory.getLog(SysTestBase.class);

	private GNDMSystem sys;
	private Runnable sysDestructor;
	protected GNDMServiceHome home;

    @Parameters({"gridName"})
    public SysTestBase(@Optional("c3grid") String gridName) {
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
		SysFactory factory = new SysFactory(logger, mockupConfig, true);
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
        try {
            ModelEntityTestBase.erasePath(new File(new File(mockupConfig.getGridPath(), "db"), mockupConfig.getGridName()));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setupEntityAction(EntityAction<?> etA) {
        etA.setOwnEntityManager(sys.getEntityManagerFactory().createEntityManager());
        etA.setOwnPostponedActions(new DefaultBatchUpdateAction());
    }


    public GridConfigMockup getMockupConfig() {
        return mockupConfig;
    }


    public GNDMSystem getSys() {
        return sys;
    }


    public GNDMServiceHome getHome() {
        return home;
    }
}
