package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.ActionTestTools;
import de.zib.gndms.logic.action.CommandAction;
import de.zib.gndms.logic.action.ModelEntityTestBase;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Optional;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 14:38:39
 */
public abstract class ConfigActionTestBase extends ModelEntityTestBase {

    public ConfigActionTestBase(final String dbPath, @Optional("c3grid") final String Name) {
        super(dbPath, Name);
    }

    protected void prepareConfigAction(
            final @NotNull ConfigAction<?> actionParam, final PrintWriter pwriterParam,
            final String options)
            throws CommandAction.ParameterTools.ParameterParseException {
        actionParam.setEntityManager(createEntityManager());
        ActionTestTools.setupConfigAction(actionParam, pwriterParam);
        actionParam.parseLocalOptions(options);
    }


    protected final @NotNull EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
}
