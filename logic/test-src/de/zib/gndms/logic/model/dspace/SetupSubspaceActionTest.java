package de.zib.gndms.logic.model.dspace;

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



import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.model.config.ConfigActionTestBase;
import org.testng.annotations.*;

import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 16:11:02
 */
@SuppressWarnings({
        "IOResourceOpenedButNotSafelyClosed", "UseOfSystemOutOrSystemErr",
        "HardcodedFileSeparator" })
public class SetupSubspaceActionTest extends ConfigActionTestBase {
    @Parameters({"dbPath", "dbName"})
    public SetupSubspaceActionTest(final String dbPath, @Optional("c3grid") final String Name) {
        super(dbPath, Name);
    }


    @BeforeClass(groups={"db", "action", "config"})
    public void init() {
        removeDbPath();
    }

    @AfterClass(groups={"db", "action", "config"})
    public void shutdownDb() {
        tryCloseEMF();
    }

    @Test(groups={"db", "action", "config"})
    public void testExecute() throws ParameterTools.ParameterParseException {
        SetupSubspaceAction action;

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:create; subspace:'{http://www.c3grid.de/G2}Staging'; size:10; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:update; subspace:'{http://www.c3grid.de/G2}Staging'; size:20; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:delete; subspace:'{http://www.c3grid.de/G2}Staging'; size:20; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }
    }
}
