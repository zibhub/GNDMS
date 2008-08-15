package de.zib.gndms.infra.monitor;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;


/**
 * TDecoupling interface between GNDMSystem and GroovMoniServer
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:02:48
 */
public interface ActionRunner {
    Object runAction(final @NotNull String className, final @NotNull String opts, 
                     final @NotNull PrintWriter writer);
}
