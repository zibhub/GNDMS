package de.zib.gndms.logic.action;

import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.util.SimpleModelUUIDGen;

import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:22:56
 */
public final class ActionTestTools {
    private ActionTestTools() {}


    @SuppressWarnings({ "FeatureEnvy" })
    public static void setupConfigAction(ConfigAction<?> cfgA, PrintWriter pwriter) {
        cfgA.setClosingWriterOnCleanUp(false);
        cfgA.setWriteResult(true);
        cfgA.setPrintWriter(pwriter);
        cfgA.setUUIDGen(SimpleModelUUIDGen.getInstance());
    }
}
