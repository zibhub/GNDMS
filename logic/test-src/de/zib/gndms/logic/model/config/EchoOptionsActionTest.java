package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.model.config.ParameterTools;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Tests wether EchoOptions actually returns all the options it is given
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:27:15
 */
@SuppressWarnings(
        { "IOResourceOpenedButNotSafelyClosed", "MethodMayBeStatic", "HardcodedLineSeparator" })
public class EchoOptionsActionTest extends ConfigActionTestBase {

    @Parameters({"dbPath", "dbName"})
    public EchoOptionsActionTest(final String dbPath, @Optional("c3grid") final String Name) {
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

    @Test(groups = {"factory", "action", "config"})
    public void runPlainAction() throws ParameterTools.ParameterParseException {
        EchoOptionsAction action = new EchoOptionsAction();
        final StringWriter strWriter = new StringWriter();
        prepareConfigAction(action, new PrintWriter(strWriter), "foo:bar; florp:flurp");
        action.call();
        assertEquals(strWriter.toString(), "foo: 'bar';\nflorp: 'flurp'");
    }


    @Test(groups = {"factory", "action"})
    public void runInheritanceAction() throws ParameterTools.ParameterParseException {
        EchoOptionsAction parentAction = new EchoOptionsAction();
        final StringWriter strWriter = new StringWriter();
        prepareConfigAction(parentAction, new PrintWriter(strWriter), "foo:bar");

        EchoOptionsAction childAction = new EchoOptionsAction();
        childAction.setParent(parentAction);
        childAction.parseLocalOptions("florp:flurp");
        childAction.call();

        assertEquals(strWriter.toString(), "foo: 'bar';\nflorp: 'flurp'");
    }
}
