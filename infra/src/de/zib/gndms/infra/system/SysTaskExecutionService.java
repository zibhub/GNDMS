package de.zib.gndms.infra.system;
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

import de.zib.gndms.logic.action.ActionConfigurer;
import de.zib.gndms.logic.model.*;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.neomodel.common.Dao;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * A SysTaskExecutionService submits {@link EntityAction}s to an {@link ExecutorService}.
 *
 * Before the action is submitted to the executor, using a suitable {@code submitAction(..)} method,
 * {@link #submit_(de.zib.gndms.logic.model.EntityAction)} will automatically
 * prepare the action using certain setters.
 *
 * When the executor is shutted down, using {@link #shutdown()},
 * the system will wait {@link de.zib.gndms.infra.system.GNDMSystem#EXECUTOR_SHUTDOWN_TIME} milliseconds.
 *
 */
public final class SysTaskExecutionService extends ActionConfigurer implements TaskExecutionService, ThreadFactory, ModelUUIDGen {

    private final ThreadPoolExecutor executorService;
    private volatile boolean terminating;

    private static final long EXECUTOR_SHUTDOWN_TIME = 5000L;
    private GNDMSystem system;


    /**
     * Initializes the ExecutorService
     *
     */
    public SysTaskExecutionService() {
        super();
        executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executorService.prestartCoreThread();
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Returns whether {@link #shutdown()} has already been invoked or not.
     *
     * @return whether {@link #shutdown()} has already been invoked or not.
     */
    public boolean isTerminating() {
        return terminating;
    }


    /**
     * Stopps all currently executed and waiting tasks on the Executor {@code getExecutorService()}.
     * Sets {@link #terminating} to {@code true}.
     * The method waits {@link GNDMSystem#EXECUTOR_SHUTDOWN_TIME} milliseconds until it returns.
     *
     */
    @SuppressWarnings({ "BusyWait" })
    public void shutdown() {
        terminating = true;
        getExecutorService().shutdownNow();
        /* truely sleep for EXECUTOR_SHUTDOWN_TIME */
        long now = System.currentTimeMillis();
        long stop = now + EXECUTOR_SHUTDOWN_TIME;
        do {
            try {
                Thread.sleep(stop - now);
            }
            catch (InterruptedException e) {
                // intentionally
            }
            now = System.currentTimeMillis();
        }
        while (stop - now > 0L);

    }


    public final @NotNull <R> Future<R> submitAction( final @NotNull EntityAction<R> action ) {

        return  submit_( configureAction( action ) );
    }


    public final @NotNull <R> Future<R> submitDaoAction( final @NotNull ModelDaoAction<?, R> action ) {

        return submit_( configureDaoAction( action ) );
    }


    @SuppressWarnings({ "FeatureEnvy" })
    public @NotNull <R> Future<R> submitAction( final @NotNull EntityManager em,
                                                final @NotNull EntityAction<R> action ) {

        return submit_( configureAction( em, action ) );
    }


    public @NotNull <R> Future<R> submitDaoAction( final @NotNull EntityManager em,
                                                   final @NotNull Dao dao,
                                                   final @NotNull ModelDaoAction<?, R> action ) {

        return submit_( configureDaoAction( em, dao, action ) );
    }


    /**
     * Prepares {@code action} using certain setters, before it is submitted to the Executor.
     *
     * <p>
     * If {@code action} is a
     * <ul>
     *  <li>{@link SystemHolder}, the GNDMSystem ({@code GNDMSystem.this}) will be stored</li>
     *  <li>{@link AbstractEntityAction}, its UUID generator will be set to {@link GNDMSystem#uuidGenDelegate}</li>
     *  <li>{@link TaskAction}, its service will be set to {@code this}.</li>
     * </ul>
     *
     * If {@code action} does not already have postponed actions,
     * they are set to a new {@link de.zib.gndms.logic.model.DefaultBatchUpdateAction}.
     * If {@code action} does not already have listerns for the postponed actions,
     * they are set to {@link GNDMSystem#getEntityUpdateListener()}.
     *
     *
     * @param action the EntityAction which should be executed
     * @return A Future Object holding the result of action's computation
     */
    @SuppressWarnings({ "FeatureEnvy" })
    private <R> Future<R> submit_( final EntityAction<R> action ) {

         return getExecutorService().submit(action);
    }


    @Override
    protected <R> EntityAction<R> configure_( final EntityAction<R> action ) {

        if (action instanceof SystemHolder )
            ((SystemHolder)action).setSystem( system );
        if (action instanceof TaskAction ) {
            final TaskAction taskAction = (TaskAction) action;
            taskAction.setService(this);
        }
        return super.configure_( action );
    }


    public Thread newThread(final Runnable r) {
        return Executors.defaultThreadFactory().newThread(r);
    }


    @NotNull
    public String nextUUID() {
        return UUID.randomUUID().toString();
    }

    @Inject
    public void setSystem( GNDMSystem system ) {
        this.system = system;
    }
}

