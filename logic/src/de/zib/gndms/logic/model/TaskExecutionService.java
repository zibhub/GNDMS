package de.zib.gndms.logic.model;

import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 30.09.2008 Time: 15:04:26
 */
public interface TaskExecutionService {
    ExecutorService getExecutorService();

    @NotNull <R> Future<R> submitAction(final @NotNull EntityAction<R> action, final @NotNull Log logger);

    @NotNull <R> Future<R> submitAction(final @NotNull EntityManager em,
                                        final @NotNull EntityAction<R> action,
                                        final @NotNull Log logger);
    
    boolean isTerminating();

    void shutdown();
}
