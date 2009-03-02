package de.zib.gndms.logic.util;

/**
 * This class provides tools for the databasse configuration.
 *
 * 
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

    /**
     * Changes derby's configuration, so that its logfile
     * will not be deleted and recreated after a reboot.
     * Furthermore the text and parameter values of all
     * executed statements will be logged.
     * The log severity is set to 20000,
     * so that errors which cause a statement to be rolled back are logged.
     *
     * 
     */
    public static void setDerbyToDebugMode() {
        System.setProperty("derby.infolog.append", "true");
        System.setProperty("derby.language.logStatementText", "true");
        System.setProperty("derby.stream.error.logSeverityLevel", "20000");
    }
}
