package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * Wrapper around ExecutorService to submit only EntityActions to an Executor.
 *
 * @author: try ste fan pla nti kow zib
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
     * @param <R>  the return type of the action
     * @return A Future Object holding the result of action's computation
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
     * @param <R> the return type of the action
     * @return A Future Object holding the result of action's computation
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
     * Stopps the ExecturService.
     * 
     * @see ExecutorService#shutdown()
     * @see ExecutorService#shutdownNow() 
     */
    void shutdown();
}
