package logic.model.config;

import de.zib.gndms.logic.action.ActionTestTools;
import de.zib.gndms.logic.model.config.EchoOptionsAction;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:27:15
 */
public class EchoOptionsActionTest {

    @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed", "MethodMayBeStatic" })
    @Test(groups = {"util", "action"})
    public void runDummyAction() {
        EchoOptionsAction action = new EchoOptionsAction();
        action.setRequiringEntityManager(false);
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        ActionTestTools.setupConfigAction(action, pwriter);
        action.call();
    }
}
