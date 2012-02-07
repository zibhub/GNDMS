package de.zib.gndms.infra.system;

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


import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.EntityAction;
import de.zib.gndms.model.test.ModelEntityTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;


/**
 * ThingAMagic.
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */

public abstract class SysTestBase {
	private GridConfigMockup mockupConfig;
	private String gridName;
	private boolean setupEnvironment;
	private Logger logger = LoggerFactory.getLogger(SysTestBase.class);

	private GNDMSystem sys;
	private Runnable sysDestructor;

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

	protected void runDatabase() {
        throw new UnsupportedOperationException( "load it from context" );
		//SysFactory factory = new SysFactory(logger, mockupConfig, true);
		//sys = factory.getInstance(false);
		//sysDestructor = factory.createShutdownAction();
	}


	protected void shutdownDatabase() {
		try {
			sysDestructor.run();
		}
		finally {
			sys = null;
			sysDestructor = null;
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
        etA.setOwnPostponedEntityActions(new DefaultBatchUpdateAction());
    }


    public GridConfigMockup getMockupConfig() {
        return mockupConfig;
    }


    public GNDMSystem getSys() {
        return sys;
    }


    private class GridConfigMockup {

        private String gridPath;
        private String gridName;


        public GridConfigMockup( final String gridName ) {
            // Implement Me. Pretty Please!!!
        }


        public String getGridPath() {

            return gridPath;
        }


        public String getGridName() {

            return gridName;
        }
    }
}
