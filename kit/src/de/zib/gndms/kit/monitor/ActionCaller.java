package de.zib.gndms.kit.monitor;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;


/**
 * Decoupling interface between GNDMSystem and GroovyMMoniServer
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:02:48
 */
public interface ActionCaller {
    Object callAction(final @NotNull String className, final @NotNull String opts,
                      final @NotNull PrintWriter writer) throws Exception;

}
