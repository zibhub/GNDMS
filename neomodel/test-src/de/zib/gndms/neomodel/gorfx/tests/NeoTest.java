package de.zib.gndms.neomodel.gorfx.tests;

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
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 07.02.11
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
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
