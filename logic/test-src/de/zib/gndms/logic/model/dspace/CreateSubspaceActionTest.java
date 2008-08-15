package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.CommandAction;
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
@SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed", "UseOfSystemOutOrSystemErr" })
public class CreateSubspaceActionTest extends ConfigActionTestBase {
    @Parameters({"dbPath", "dbName"})
    public CreateSubspaceActionTest(final String dbPath, @Optional("c3grid") final String Name) {
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
    public void testExecute() throws CommandAction.ParameterTools.ParameterParseException {
        CreateSubspaceAction action = new CreateSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "scope:http://www.c3grid.de/G2; name:Staging; size:10; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }

    }
}
