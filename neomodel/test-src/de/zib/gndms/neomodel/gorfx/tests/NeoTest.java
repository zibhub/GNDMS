package de.zib.gndms.neomodel.gorfx.tests;

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

import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;

/**
 * NeoTest
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public abstract class NeoTest {
    protected GraphDatabaseService gdb = null;
    protected NeoDao dao = null;
    protected NeoSession session = null;
    protected String dbDirName = "/tmp/neo";
    protected File dbDir = new File(dbDirName);


    @BeforeSuite
    public void killDatabase() throws IOException {
        FileUtils.deleteDirectory(dbDir);
    }

    @BeforeMethod(dependsOnGroups = "neo")
    public void initConnection() {
        assert gdb == null;
        gdb = new EmbeddedGraphDatabase(dbDirName);
        assert dao == null;
        dao = new NeoDao("gndms", gdb);
        assert session == null;
        session = dao.beginSession();

    }

    @AfterMethod(dependsOnGroups = "neo")
    public void shutdownConnection() {
        try {
            session.finish();
            gdb.shutdown();
        }
        finally {
            dao = null;
            session = null;
            gdb = null;
        }
    }
}
