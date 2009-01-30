package de.zib.gndms.infra.action;

import de.zib.gndms.kit.monitor.ActionCaller;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.01.2009, Time: 18:28:25
 */
public interface WSActionCaller extends ActionCaller {

    // public actions must implement the PublicAccess interface.
    Object callPublicAction(final @NotNull String className, final @NotNull String opts,
                            final @NotNull PrintWriter writer) throws Exception;

}
