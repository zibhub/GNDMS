package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * Wrapper around ExecutorService to submit only EntityActions to an Executor.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 30.09.2008 Time: 15:04:26
 */
public interface TaskExecutionService {
    ExecutorService getExecutorService();

    /**
     * Submits an EntityAction to an {@code ExecutorService}.
     * If the action does not have an EntityManager, a new one will be created.
     * 
     *
     * @param action the EntityAction which should be executed
     * @return A Future Object holding the result of action's computation
     * @see Future
     */
    @NotNull <R> Future<R> submitAction( final @NotNull EntityAction<R> action );

    /**
     * Submits an EntityAction to an {@code ExecutorService}.
     * Sets the {@code action}'s EntityManager as {@code em}.
     * 
     *
     * @param em an EntityManger for the EntityAction
     * @param action the EntityAction which should be executed
     * @return A Future Object holding the result of action's computation
     * @see Future
     */
    @NotNull <R> Future<R> submitAction( final @NotNull EntityManager em,
                                         final @NotNull EntityAction<R> action );


    /**
     * Sets action's dao before calling {@link #submitAction(javax.persistence.EntityManager, EntityAction}
     *
     * @see {@link #submitAction(javax.persistence.EntityManager, EntityAction}
     */
    @NotNull <R> Future<R> submitDaoAction( final @NotNull EntityManager em,
                                                   final @NotNull Dao dao,
                                                   final @NotNull ModelDaoAction<?, R> action );

    @NotNull <R> Future<R> submitDaoAction( final @NotNull ModelDaoAction<?, R> action );

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


    /**
     * Creates a task and taskling, initializes the task action and submits it.
     *
     * @param dao The dao under which the task ist submitted.
     * @param taskAction The action which should be submitted.
     * @param order The order, i.e. input for the task.
     * @param wid The workflow id, used to keep trak of the logging massages of the task action.
     *
     * @return The taskling representing the newly created task.
     */
    Taskling submitTaskAction( Dao dao, TaskAction taskAction, Serializable order, String wid );


    /**
     * Creates a task and taskling, initializes the task action and submits it using the default
     * dao.
     *
     * Convenience method. Behaves exactly like the above method, but ueses the system dao,
     * instead of a custom one.
     *
     * @param taskAction The action which should be submitted.
     * @param order The order, i.e. input for the task.
     * @param wid The workflow id, used to keep trak of the logging massages of the task action.
     *
     * @return The taskling representing the newly created task.
     */
    Taskling submitTaskAction( TaskAction taskAction, Serializable order, String wid );
}
