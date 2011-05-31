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

import de.zib.gndms.logic.action.LogAction;
import de.zib.gndms.logic.model.*;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.neomodel.common.Dao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * A SysTaskExecutionService submits {@link EntityAction}s to an {@link ExecutorService}.
 *
 * Before the action is submitted to the executor, using a suitable {@code submitAction(..)} method,
 * {@link #submit_(de.zib.gndms.logic.model.EntityAction, org.apache.commons.logging.Log)} will automatically
 * prepare the action using certain setters.
 *
 * When the executor is shutted down, using {@link #shutdown()},
 * the system will wait {@link de.zib.gndms.infra.system.GNDMSystem#EXECUTOR_SHUTDOWN_TIME} milliseconds.
 *
 */
public final class SysTaskExecutionService implements TaskExecutionService, ThreadFactory, ModelUUIDGen {

    private final ThreadPoolExecutor executorService;
    private volatile boolean terminating;

    private EntityManagerFactory entityManagerFactory;
    private ModelUpdateListener<GridResource> entityUpdateListener = new NoWSDontNeedModelUpdateListener();
    private Dao dao;
    private static final long EXECUTOR_SHUTDOWN_TIME = 5000L;


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


    public final @NotNull <R> Future<R> submitAction(final @NotNull EntityAction<R> action, final @NotNull Log log) {
        final EntityManager ownEm = action.getOwnEntityManager();
        if (ownEm != null)
            return submit_(action, log);
        else {
            final @NotNull EntityManager em = entityManagerFactory.createEntityManager();
            return submitAction(em, action, log);
        }
    }

    public final @NotNull <R> Future<R> submitDaoAction(final @NotNull ModelDaoAction<?, R> action,
                                                        final @NotNull Log log) {
        final Dao dao = action.getOwnDao();
        if (dao != null)
            return submitAction(action, log);
        else {
            action.setOwnDao( getDao() );
            return submitAction(action, log);
        }
    }

    @SuppressWarnings({ "FeatureEnvy" })
    public @NotNull <R> Future<R> submitAction(final @NotNull EntityManager em,
                                               final @NotNull EntityAction<R> action,
                                               final @NotNull Log log) {
        return submit_(action, log);
    }

    public @NotNull <R> Future<R> submitDaoAction(final @NotNull EntityManager em,
                                                  final @NotNull Dao dao,
                                                  final @NotNull ModelDaoAction<?, R> action,
                                                  final @NotNull Log log) {
        action.setOwnDao( dao );
        return submitAction(em, action, log);
    }


    /**
     * Prepares {@code action} using certain setters, before it is submitted to the Executor.
     *
     * <p>
     * If {@code action} is a
     * <ul>
     *  <li>{@link LogAction}, its logger will be set to {@code log} </li>
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
     * @param action the EntityAction which should be executed
     * @param log A logger, which can be added to the action, if it's a LogAction
     * @param <R> the return type of the action
     * @return A Future Object holding the result of action's computation
     */
    @SuppressWarnings({ "FeatureEnvy" })
    private <R> Future<R> submit_(final EntityAction<R> action, final @NotNull Log log) {
        if (action instanceof LogAction )
            ((LogAction)action).setLog(log);
        if (action instanceof SystemHolder)
            ((SystemHolder)action).setSystem( null );
        if (action.getPostponedEntityActions() == null)
            action.setOwnPostponedEntityActions(new DefaultBatchUpdateAction<GridResource>());
        if (action.getPostponedEntityActions().getListener() == null)
            action.getPostponedEntityActions().setListener(getEntityUpdateListener());
        if (action instanceof ModelDaoAction)
            ((ModelDaoAction ) action).setOwnDao( getDao() );
        if (action instanceof AbstractEntityAction)
            ((AbstractEntityAction<?> )action).setUUIDGen( this );
        if (action instanceof TaskAction) {
            final TaskAction taskAction = (TaskAction) action;
            taskAction.setService(this);
        }
        return getExecutorService().submit(action);
    }


    public Thread newThread(final Runnable r) {
        return Executors.defaultThreadFactory().newThread(r);
    }


    @PersistenceContext
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }


   public ModelUpdateListener<GridResource> getEntityUpdateListener() {
        return entityUpdateListener;
    }


    public Dao getDao() {
        return dao;
    }


    @Autowired
    public void setDao( Dao dao ) {
        this.dao = dao;
    }


    @NotNull
    public String nextUUID() {
        return UUID.randomUUID().toString();
    }
}

