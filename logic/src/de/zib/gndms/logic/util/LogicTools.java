package de.zib.gndms.logic.util;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 20.08.2008 Time: 16:27:32
 */
public final class LogicTools {
    private LogicTools() {
        super();
    }


    public static void setDerbyToDebugMode() {
        System.setProperty("derby.infolog.append", "true");
        System.setProperty("derby.language.logStatementText", "true");
        System.setProperty("derby.stream.error.logSeverityLevel", "20000");
    }
}
