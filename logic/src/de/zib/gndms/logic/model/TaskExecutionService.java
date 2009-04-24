package de.zib.gndms.logic.model;

import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * Wrapper around ExecutorService to submit only EntityActions to an Executor.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 30.09.2008 Time: 15:04:26
 */
public interface TaskExecutionService {
    ExecutorService getExecutorService();

    /**
     * Submits an EntityAction to an {@code ExecutorService}.
     * If the action is a LogAction, its logger will be set as {@code logger}.
     * If the action does not have an EntityManager, a new one will be created.
     * 
     * @param action the EntityAction which should be executed
     * @param logger A logger, which can be added to the action, if it's a LogAction
     * @param <R>
     * @return A Future Object representing the result of action's computation
     * @see Future
     */
    @NotNull <R> Future<R> submitAction(final @NotNull EntityAction<R> action, final @NotNull Log logger);

    /**
     * Submits an EntityAction to an {@code ExecutorService}.
     * If the action is a LogAction, its logger will be set as {@code logger}
     * Sets the {@code action}'s EntityManager as {@code em}.
     * 
     * @param em an EntityManger for the EntityAction
     * @param action the EntityAction which should be executed
     * @param logger A logger, which can be added to the action, if it's a LogAction
     * @param <R>
     * @return A Future Object representing the result of action's computation
     * @see Future
     */
    @NotNull <R> Future<R> submitAction(final @NotNull EntityManager em,
                                        final @NotNull EntityAction<R> action,
                                        final @NotNull Log logger);


    /**
     * Returns true if this is terminating or already terminated.
     * 
     * @return true if this is terminating or already terminated
     */
    boolean isTerminating();

    /**
     * Stopps the ExecturService.Depending on the implementation,either only new tasks will be rejected or
     * additionally it will attempt to stop all its running tasks
     * 
     * @see ExecutorService#shutdown()
     * @see ExecutorService#shutdownNow() 
     */
    void shutdown();
}
