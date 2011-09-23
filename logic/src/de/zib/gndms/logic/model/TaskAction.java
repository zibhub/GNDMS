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



import de.zib.gndms.kit.access.RequiresCredentialProvider;
import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.configlet.ConfigletProvider;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.action.LogAction;
import de.zib.gndms.logic.model.gorfx.LifetimeExceededException;
import de.zib.gndms.logic.model.gorfx.permissions.PermissionConfiglet;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.model.gorfx.types.AbstractOrder;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskAccessor;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.slf4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.GregorianCalendar;


/**
 * A TaskAction is used to execute a model of type {@link Taskling} with an underlying model of {@link de.zib.gndms.neomodel.gorfx.Task}
 *
 *
 * @author  try ste fan pla nti kow zib
 *
 * User: stepn Date: 15.09.2008 Time: 11:26:48
 */
@SuppressWarnings({ "AbstractMethodCallInConstructor" })
public abstract class TaskAction extends AbstractModelDaoAction<Taskling, Taskling>
        implements LogAction, RequiresCredentialProvider
{
    /**
     * The ExecutionService on which this TaskAction runs
     */
    private TaskExecutionService service;

    /**
     * Used for logging during task execution
     */
    private Logger log;
    private String wid;

    /**
     * An EntityManagerFactory for task actions that need it
     */
	private EntityManagerFactory emf;

    private ConfigletProvider configletProvider;

    private CredentialProvider credentialProvider;
    protected volatile ModelUpdateListener<Taskling> modelUpdateListener = null;

    public TaskAction() {
        super();
    }

    
    /**
     * Initializes a TaskAction from an EntityManager, a dao and a (shallow) model.
     *
     * The model is made persistent by the EntityManager.
     * The EntityManager and the model are stored, using {@code setOwnEntityManager()} and {@code setModelAndBackup()}.
     * A Backup of the model is done.
     *
     *
     * @param em an EntityManager, storing AbstractTasks
     * @param dao the dao for storing task nodes in the graph database
     * @param model an AbstractTask to be stored as model of {@code this} and to be stored in the database
     */
    public TaskAction(final @NotNull EntityManager em, final @NotNull Dao dao, final @NotNull Taskling model) {
        super();
        setOwnDao(dao);
        setOwnEntityManager(em);
        setModel(model);
    }


    /***
     * Sets the task's workflow id from WidAux before calling {@link #execute()} via {@code super.call()}
     * 
     * @return model after being finished
     * @throws RuntimeException
     */
    @Override
    public Taskling call() throws RuntimeException {
        final Taskling result;
        try {
            final Session session = getDao().beginSession();
            try {
                final Taskling ling = getModel();
                final Task task = ling.getTask(session);
                WidAux.initWid(task.getWID());
                if( task.getORQ() != null )
                    WidAux.initGORFXid( ( (DelegatingOrder ) task.getORQ()).getActId() );
                session.finish();
            }
            finally { session.success(); }
            result = super.call();
        }
        finally {
            WidAux.removeGORFXid();
            WidAux.removeWid();
        }
        return result;
}

    /**
     * Invokes {@link #onTransit(String,TaskState,boolean,boolean)} in a loop which is expected to change the task state.
     * This goes on until either the state FINISHED or FAILED is reached.
     *
     * All Tasks become FAILED when an unhandled exception is caught.
     * 
     * @param em the EntityManager being executed on its persistence context.
     * @return the model after the last round of execution
     */
    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "ObjectAllocationInLoop" })
    @Override
    public Taskling execute(final @NotNull EntityManager em) {
        // Cross-Loop state
        String thisWID = wid == null? "(null)" : wid;
        boolean inFirstStep   = true;
        boolean afterLastStep = false;

        while ( ! afterLastStep ) {
            // In-Loop state
            Session session;
            boolean isRestartedTask;
            TaskState state;
            boolean altTaskState;
            String descr;
        
            // Capture state snapshot from Task and prep in-loop state
            session = getDao().beginSession();
            try {
                final Task task = getTask(session);
                state              = task.getAltTaskState();
                altTaskState       = state != null;
                if (! altTaskState)
                    state = task.getTaskState();

                descr              = task.getDescription();
                isRestartedTask    = ! TaskState.CREATED.equals(state);
                if (state.isRestartedState()) {
                    // canonify state if it isn't
                    state = state.getCanonicalState();
                    task.setTaskState(state);
                }
                else
                    isRestartedTask &= inFirstStep;

                afterLastStep = task.isDone();
                session.success();
            }
            finally { session.finish(); }


            if ( inFirstStep )
                trace("execute() with " + descr, null);

            if (! afterLastStep ) {
                trace("transit(" + state.name() + ")", null);
                try {
                    if (state.isDoneState()) {
                        try {
                            onTransit(thisWID, state, isRestartedTask, altTaskState);
                        }
                        finally {
                            session = getDao().beginSession();
                            try {
                                getModel().getTask(session).setDone(true);
                                session.success();
                            }
                            finally { session.finish(); }
                        }
                    }
                    else {
                        checkTimeout(getModel());
                        onTransit(thisWID, state, isRestartedTask, altTaskState);
                    }
                }
                catch (Exception e) {
                    // Oh yes, this is possible
                    if (e == null)
                        fail(new NullPointerException());
                    else
                        fail(e);
                }
            }

            if (altTaskState) {
                // todo handle altTaskState
            }
            inFirstStep = false;
        }

        trace("return getModel()", null);
        return getModel();
    }

    protected @NotNull TaskAccessor getTaskSnapshot(@NotNull Dao dao) {
        final Session session = dao.beginSession();
        try {
            final TaskAccessor snapshot = getTask(session).getSnapshot();
            session.success();
            return snapshot;
        }
        finally { session.finish(); }
    }

    protected @NotNull Task getTask(@NotNull Session session) {
        Taskling model = getModel();
        if (model == null)
            throw new NullPointerException("Task action without model (Taskling)");
        else
            return model.getTask(session);
    }


    @Override
    protected final boolean isExecutingInsideTransaction() {
        return false;
    }

//    /**
//     *
//     * Retrieves the AbstractTask from the database, which corresponds to {@code getModel()).
//     * It is marked as done, by invoking {@code setDone(true)} on it and restored to the database.
//     *
//     * It will be set as {@code this}' the new model by calling {@code setModelAndBackup()}, which also makes a backup
//     * of the new model.
//     *
//     */
//    private void markAsDone() {
//        final @NotNull AbstractTask model = getModel();
//        if (! model.isDone()) {
//            final EntityManager em = getEntityManager();
//            try {
//                em.getTransaction().begin();
//                AbstractTask newModel = em.find(AbstractTask.class, getModel().getId());
//                newModel.setDone(true);
//                em.getTransaction().commit();
//                setModelAndBackup(newModel);
//            }  catch (RuntimeException e) {
//                trace( "Non throwable exception: ", e );
//            }
//            finally {
//                try{
//                    if (em.getTransaction().isActive())
//                        em.getTransaction().rollback();
//                } catch ( RuntimeException e ) {
//                    trace( "Non throwable exception: ", e );
//                }
//            }
//        }
//    }
//
//
//    /**
//     * Retrieves the current model and checks if its lifetime is already exceeded.
//     * In this case the model's TaskState will be set to {@code failed} and a {@link FailedException} is thrown,
//     * which will stop the the main loop in {@code execute()}.
//     *
//     * Otherwise, the model's TaskState is set to {@code newState} and {@link #transit(TaskState, AbstractTask)}
//     * will be invoked,
//     * which calls a method corresponding to the TaskState {@code newState} and throws a {@code TransitException} after its
//     * execution specifying the next TaskSate value for the model.
//     *
//     * The model is made persistent by the EntityManager and the changed model will be
//     * commited to the database.
//     * If something goes wrong while commiting the new model, a stable rollback is assured.
//     *
//     * @param newState the new TaskState for the model
//     */
//    @SuppressWarnings( { "CaughtExceptionImmediatelyRethrown", "ThrowableInstanceNeverThrown" } )
//    private void transit(final TaskState newState) {
//
//        EntityManager em = getEntityManager();
//        @NotNull AbstractTask model = getModel();
//
//        // this throws a stop exception on timeout
//        if(! ( TaskState.FINISHED.equals( newState ) || TaskState.FAILED.equals( newState ) ) )
//            checkTimeout( model, em );
//
//        try {
//            em.getTransaction().begin();
//            if (newState != null) {
//                if (! em.contains(model)) {
//                    try {
//                        try {
//                            em.persist(model);
//                        }
//                        catch (EntityExistsException e) {
//                            rewindTransaction(em.getTransaction());
//                            em.merge(model);
//                        }
//                    }
//                    catch (RuntimeException e2) {
//                            rewindTransaction(em.getTransaction());
//                            final @NotNull AbstractTask newModel = em.find(AbstractTask.class, model.getId());
//                            model.mold(newModel);
//	                        newModel.refresh(em);
//                            setModel(newModel);
//                            model = getModel();
//                        }
//                    }
//                }
//
//            model.transit(newState);
//
//            boolean commited = false;
//            try {
//                em.getTransaction().commit();
////                em.flush( );
//                commited = true;
//            } catch ( Exception e ) {
//                try {
//                    rewindTransaction(em.getTransaction());
//                    // if this point is reached s.th. is terribly foobared
//                    // restore backup and fail
//                    model = Copier.copy( false, backup );
//                    em.merge( model );
//                    // backup should be clean so commit mustn't fail.
//                    model.fail( e );
//                    em.getTransaction().commit();
//                } catch ( Exception e2 ) {
//                    // refresh em for final commit
//                    EntityManagerAux.cleanClose( em );
//                    EntityManager nem = emf.createEntityManager();
//                    TxFrame tx = new TxFrame( nem );
//                    try {
//                        model = nem.find( model.getClass( ), backup.getId() );
//                        boolean unkown = ( model == null );
//                        model = Copier.copy( false, backup );
//                        model.fail( e2 );
//                        if( unkown )
//                            nem.persist( model );
//                   //     else
//                   //         nem.merge( model );
//                        tx.commit();
//                        setModel( model );
//                    } catch ( RuntimeException e3 ) {
//                        throw e3;
//                    } finally {
//                        tx.finish();
//                        setOwnEntityManager( nem );
//                        em = nem;
//                    }
//                }
//            }
//
//            // if model could be commited it has a clean state
//            // refresh backup
//            if( commited )
//                setBackup( Copier.copy( false, model ) );
//
//            final TaskState modelState = model.getState();
//            refreshTaskResource();
//            //noinspection HardcodedFileSeparator
//            trace("on" + modelState + "()", null);
//            transit(modelState, model);
//        }
//        // for debugging
//        catch (RuntimeException e) {
//            throw e;
//        }
//        finally {
//            // for debuggin'
//            boolean ta = em.getTransaction().isActive();
//            if ( ta )
//                em.getTransaction().rollback();
//        }
//    }
//
//    /**
//     * Invokes a rollback on an entity transaction and a following {@code begin()},
//     * only if it has been marked (using {@code setRollbackOnly()}).
//     *
//     * @param txParam a transaction to be rewinded
//     */
//    private void rewindTransaction(final EntityTransaction txParam) {
//        if( txParam.isActive() ) {
//            if ( txParam.getRollbackOnly()) {
//                txParam.rollback();
//                txParam.begin();
//            }
//        } else
//            txParam.begin();
//    }
//
//
//    /**
//     * This method is used to control and change the Taskstate of an AbstractTask.
//     *
//     * It checks if the {@code model}'s TaskState can be set to {@code newState}.
//     * If it is allowed (according to the order given in {@link TaskState}),
//     * a method corresponding to the value of {@codeT newState} will be invoked.
//     *
//     * All these methods must throw a {@code TransitException} to specify the new desired state.
//     *
//     *
//     * @see TaskState
//     * @see #onCreated(AbstractTask)
//     * @see #onInitialized(AbstractTask)
//     * @see #onrestarted(AbstractTask)
//     * @see #onInProgress(AbstractTask)
//     * @see #onFailed(AbstractTask)
//     * @see #onFinished(AbstractTask)
//     *
//     * @param newState the state, the model's state should be changed to
//     * @param model the model whose state should be changed.
//     */
//    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
//    private void transit(final @NotNull TaskState newState, final @NotNull AbstractTask model) {
//        switch (model.getState().transit(newState)) {
//            case CREATED: onCreated(model); break;
//            case CREATED_restarted: onrestarted(model); break;
//            case INITIALIZED: onInitialized(model); break;
//            case INITIALIZED_restarted: onrestarted(model); break;
//            case IN_PROGRESS: onInProgress(model); break;
//            case IN_PROGRESS_restarted: onrestarted(model); break;
//            case FAILED:
//                    if (! model.isDone())
//                        onFailed(model);
//                    /* safety catch-all */
//                    throw new StopException(TaskState.FAILED);
//            case FINISHED:
//                    if (! model.isDone())
//                        onFinished(model);
//                    /* safety catch-all */
//                    throw new StopException(TaskState.FINISHED);
//            default:
//                throw new IllegalStateException("Invalid or unsupported task state");
//        }
//    }


    protected void onTransit(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        throw new UnsupportedOperationException("not yet implemented");
    }


    protected void transitWithPayload(@NotNull Serializable payload, @NotNull TaskState taskState) {
        final @NotNull Session session = getDao().beginSession();
        try {
            final Task task = session.findTask(getModel().getId());
            task.setTaskState(task.getTaskState().transit(taskState));
            task.setPayload(payload);
            if (TaskState.FINISHED.equals(taskState))
                task.setProgress(task.getMaxProgress());
            session.success();
        }
        finally { session.finish(); }
    }

    protected void transit(@NotNull TaskState taskState) {
        final @NotNull Session session = getDao().beginSession();
        try {
            final Task task = session.findTask(getModel().getId());
            task.setTaskState(task.getTaskState().transit(taskState));
            if (TaskState.FINISHED.equals(taskState))
                task.setProgress(task.getMaxProgress());
            session.success();
        }
        finally { session.finish(); }
    }

    protected void autoTransitWithPayload(Serializable payload) {
        final @NotNull Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            final TaskState taskState = task.getTaskState().getCanonicalState();
            final TaskState nextState;
            switch(taskState) {
                case CREATED: nextState = TaskState.INITIALIZED; break;
                case INITIALIZED: nextState = TaskState.IN_PROGRESS; break;
                case IN_PROGRESS: nextState = TaskState.FINISHED; break;
                case FINISHED: nextState = TaskState.FINISHED; break;
                default: nextState = TaskState.FAILED; break;
            }
            task.setTaskState(taskState.transit(nextState));
            task.setPayload(payload);
            if (TaskState.FINISHED.equals(taskState))
                task.setProgress(task.getMaxProgress());
            session.success();
        }
        finally { session.finish(); }
    }

    protected void autoTransit() {
        final @NotNull Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            final TaskState taskState = task.getTaskState();
            final TaskState nextState;
            switch(taskState.getCanonicalState()) {
                case CREATED: nextState = TaskState.INITIALIZED; break;
                case INITIALIZED: nextState = TaskState.IN_PROGRESS; break;
                case IN_PROGRESS: nextState = TaskState.FINISHED; break;
                case FINISHED: nextState = TaskState.FINISHED; break;
                default: nextState = TaskState.FAILED; break;
            }
            task.setTaskState(taskState.transit(nextState));
            if (TaskState.FINISHED.equals(taskState))
                task.setProgress(task.getMaxProgress());
            session.success();
        }
        finally { session.finish(); }
    }

    protected void failWithPayload(Serializable payload, @NotNull RuntimeException... exceptions) {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            task.setTaskState(TaskState.FAILED);
            task.setPayload(payload);
            for (RuntimeException e: exceptions)
                task.addCause(e);
            session.success();
        }
        finally { session.finish(); }
    }

    protected void fail(@NotNull Exception... exceptions) {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            task.setTaskState(TaskState.FAILED);
            for (Exception e: exceptions)
                task.addCause(e);
            session.success();
        }
        finally { session.finish(); }
    }

    protected void removeAltTaskState() {
        getDao().removeAltTaskState(getModel().getId());
    }


    @SuppressWarnings({ "HardcodedFileSeparator" })
    protected void trace(final @NotNull String userMsg, final Throwable cause) {
        final Logger log1 = getLogger();
        final Taskling model = getModel();
        final String msg;
        if (model == null)
            msg = userMsg;
        else {
            Session session = getDao().beginSession();
            try {
                final Task task = model.getTask(session);
                final TaskState state = task.getTaskState();
                final String descr = task.getDescription();
                msg = "TA of AbstractTask " + model.getId()
                        + ('/' + state.toString()) + ':'
                        + (userMsg.length() > 0 ? ' ' : "") + userMsg
                        + (" DESCR: '" + descr + '\'');
                session.finish();
            }
            finally { session.success(); }
        }
       if (cause == null)
           log1.trace(msg);
        else
           log1.trace(msg, cause);

    }


    @Override
    public Taskling getModel() {
        return super.getModel();
    }


    @Override
    public void setModel(final @NotNull Taskling ling) {
        final Session session = getDao().beginSession();
        final String newWID;
        try {
            newWID = ling.getTask(session).getWID();
            session.success();
        }
        finally { session.finish();}
        super.setModel(ling);    // Overridden method
        wid = newWID;
    }

    /**
     * Inform all listeners about the current model.
     */
    protected void refreshTaskResource() {
        getModelUpdateListener().onModelChange(getModel());
    }


    /**
     * Returns the TaskExecutionService corresponding to this TaskAction or a parent TaskAction.
     *
     * @return the TaskExecutionService corresponding to this TaskAction or a parent TaskAction.
     */
    public TaskExecutionService getService() {
        if (service == null) {
            final TaskAction taskAction = nextParentOfType(TaskAction.class);
            return taskAction == null ? null : taskAction.getService();
        }
        return service;
    }


    public void setService(final @NotNull TaskExecutionService serviceParam) {
        if (service == null)
            service = serviceParam;
        else
           throw new IllegalStateException("Can't overwrite service");
    }

    @NotNull public Logger getLogger() {
        return log;
    }


    public void setLogger(@NotNull final Logger logParam) {
        log = logParam;
    }

    
    /**
     * Stopps this action if the associated task lifetime is already exceeded.
     *
     * If the lifetime is exceeded, this sets the TaskState
     * to {@code FAILURE} by throwing an exception.
     *
     * @param ling shallow model to be checked
     * @throws LifetimeExceededException
     */
    private void checkTimeout( @NotNull Taskling ling ) {

        Session session = getDao().beginSession();
        try {
            Task model = ling.getTask(session);

            // check the task lifetime
            if(new GregorianCalendar().compareTo(model.getTerminationTime()) >= 1 ) {
                getLogger().debug(  "Task lifetime exceeded" );
                throw new LifetimeExceededException();
//                boolean containt = false;
//                try {
//                    // check if model is still there
//                    containt = em.contains( model );
//                    if( containt ) {
//                        model.fail( new RuntimeException( "Task lifetime exceeded" ) );
//                        getLogger().debug(  "Try to persist task" );
//                    }
//                    tx.commit( );
//                } catch ( Exception e ) {
//                    // exception here  doesn't really matter
//                    // task is doomed anyway
//                    e.printStackTrace(  );
//                } finally {
//                    tx.finish();
//                }
//                // interrupt this thread
//                getLogger().debug(  "Stopping task thread" );
//                //Thread.currentThread().interrupt();
//                if( containt ) refreshTaskResource();
//                fail( new RuntimeException( "Task lifetime exceeded" ) );
            }
            session.success();
        }
        finally { session.finish(); }
    }


	@Override
	public EntityManager getEntityManager() {
		return getOwnEntityManager();
	}


	public EntityManagerFactory getEmf() {
		return emf;
	}


	public void setEmf(final @NotNull EntityManagerFactory emfParam) {
		emf = emfParam;
	}

    public ConfigletProvider getConfigletProvider() {
        return configletProvider;
    }


    public void setConfigletProvider( ConfigletProvider configletProvider ) {
        this.configletProvider = configletProvider;
    }



    public void setCredentialProvider( CredentialProvider cp ) {
        this.credentialProvider = cp;
    }


    public CredentialProvider getCredentialProvider() {

        return this.credentialProvider;
    }


    /**
     * Extracts the actual permissions from a tasks permission info object
     *
     * @return the actual permissions
     */
    public FilePermissions actualPermissions( ) {
        Session session = getDao().beginSession();
        try {
            Task task = getTask(session);
            FilePermissions filePermissions = null;
            PermissionConfiglet pc = configletProvider.getConfiglet( PermissionConfiglet.class,
                    task.getPermissionInfo().getPermissionConfigletName() );
            if( pc != null ) {
                filePermissions = pc.permissionsFor(task.getPermissionInfo().getUserName());
            }

            session.success();
            return filePermissions;
        }
        finally { session.finish(); }


    }


//    // TODO Delete later below
//
//            try {
//
//                try {
//                    if (first) {
//                        try {
//	                        final AbstractOrder orq = AbstractOrder.class.cast(getModel().getOrder());
//                            transit(null);
//                        }
//                        finally {
//                            first = false;
//                        }
//                    }
//                    else {
//                        trace("transit(" + curEx.newState + ')', curEx.getCause());
//                        transit(curEx.newState);
//                    }
//                    // if we come here, transit has failed
//                    throw new IllegalStateException("No proper TransitException thrown");
//                }
//                /* On stop: finish loop and return model to caller */
//                catch (final StopException newEx) {
//                    trace("markAsDone()", null);
//                    markAsDone();
//                    curEx = null;
//                }
//                /* On transit: switch to next state according to newEx */
//                catch (final TransitException newEx) {
//                    curEx = newEx;
//                }
//                /* On shutdown: Do not set the task to failed, simply throw e */
//                catch (final ShutdownTaskActionException e) {
//                    throw e;
//                }
//                /* On runtime ex: Set task to failed */
//                catch (RuntimeException e_in) {
//	                // no joke
//	                final @NotNull RuntimeException e_out = e_in == null ? new NullPointerException() : e_in;
//                    trace("catch(RuntimeException)", e_out);
//                    /* Cant go to FAILED after FINISHED, i.e. onFinish must never fail */
//                    if (TaskState.FINISHED.equals(getModel().getState()))
//                        throw e_out;                             // todo log this one
//                    else
//                        fail(e_out);
//                }
//            }
//            /* If fail was called due to a RuntimeException in above try block, use resulting
//               exception for the next state transition.  Ensures that the failed state of the
//               task will be persisted.
//             */
//            catch (TransitException newEx) {
//                curEx = newEx;
//            }
//        } while (curEx != null);
//    /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's state is restarted (so {@code CREATED_restarted}, {@code INITIALIZED_restarted} or {@code IN_PROGRESS_restarted}).
//     *
//     * It throws an UnsupportedOperationException.
//     *
//     * @param model the model, whose TaskState is set to restarted.
//     *
//     * @return it throws an UnsupportedOperationException
//     */
//    protected TaskState onrestarted(final AbstractTask model) {
//        throw new UnsupportedOperationException();
//    }
//
//
//    /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's TaskState is set to {@code CREATED}.
//     *
//     * It throws a {@code TransitException} with {@code INITIALIZED} as its TaskState.
//     * This signalizes to change the model's TaskState to {@code INITIALIZED}.
//     *
//     *
//     * @param model the model, whose TaskState is set to {@code CREATED}
//     */
//    protected void onCreated(final AbstractTask model) {
//        transitToState(TaskState.INITIALIZED);
//
//    }
//
//
//    /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's TaskState is set to {@code INITIALIZED}.
//     *
//     * It throws a {@code TransitException} with {@code IN_PROGRESS} as its TaskState.
//     * This signalizes to change the model's TaskState to {@code IN_PROGRESS}.
//     *
//     * @param model the model, whose TaskState is set to {@code INITIALIZED}
//     */
//    protected void onInitialized(final AbstractTask model) {
//        transitToState(TaskState.IN_PROGRESS);
//    }
//
//    /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's TaskState is set to {@code IN_PROGRESS}.
//     *
//     * Define here what the action on the model, when its {@code TaskState} is set to {@code IN_PROGRESS}.
//     *
//     * An implementation of this method must throw a {@code TransitException} to define to which state the model
//     * shall be set to.
//     *
//     * @param model the model, whose TaskState is set to {@code IN_PROGRESS}
//     */
//    protected abstract void onInProgress(final @NotNull AbstractTask model);
//
//
//   /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's TaskState is set to {@code FAILED}.
//     *
//     * It makes a cleanup by calling {@code tryCleanup()} and calls {@link #stop(AbstractTask)} afterwards.
//     *
//     * @param model the model, whose TaskState is set to {@code FAILED}
//     */
//    protected final void onFailed(final @NotNull AbstractTask model) {
//        tryCleanup( model );
//        stop(model);
//    }
//
//    /**
//     * This method will be called by {@link #transit(TaskState)},
//     * if the model's TaskState is set to {@code FINISHED}
//     *
//     * It calls {@link #stop(AbstractTask)}
//     *
//     * @param model the model, whose TaskState is set to {@code FINISHED}
//     */
//    protected void onFinished(final @NotNull AbstractTask model) {
//        stop(model);
//    }
//
//    /**
//     * Throws a new {@code TransitException} with {@code newState] as its TaskState.
//     *
//     * Note: It does not check if the state change is allow (according to the given order by {@link TaskState}.
//     *
//     * @param newState the new desired TaskState for the model belonging to this
//     */
//    protected static void transitToState(final @NotNull TaskState newState) {
//        throw new TransitException(newState);
//    }
//
//
//    /**
//     * Use this method to signalize, that an error occured during the model's computation.
//     *
//     * It throws a {@link FailedException} containing the RuntimeException {@code e}.
//     *
//     * Sets the progress of the AbstractTask to {@code zero} and stores the Exception in
//     * the model's failure String {@link AbstractTask#getFaultString()}.
//     * The data of the AbstractTask will be set to {@code e} and the TaskState is set to {@code FAILED}.
//     *
//     * @param e a RuntimeException which occured during the model's computation
//     */
//    protected void fail(final @NotNull RuntimeException e) {
//        getModel().fail(e);
//		e.fillInStackTrace();
//	    // getLogger().info("About to transit(FAIL) due to:", e);
//        throw new FailedException(e);
//    }
//
//
//    /**
//     * Use this method to signalize, that an model's computation is finished.
//     *
//     * It throws a {@link FinishedException} containing the result of the computation.
//     *
//     * It sets the progress of the AbstractTask to {@link AbstractTask#maxProgress} and stores the result in
//     * {@link de.zib.gndms.model.gorfx.AbstractTask#getData()}.
//     *
//     * The model's TaskState is set to {@code FINISHED}.
//     *
//     * @param result the result of the the model's computation
//     */
//    protected void finish(final Serializable result) {
//        getModel().finish(result);
//        throw new FinishedException(result);
//    }
//
//    /**
//     * Throws a StopException with and stores the model's current state in the Exception
//     *
//     * @param model an AbstractTask, which has to be stopped.
//     */
//    @SuppressWarnings({ "MethodMayBeStatic" })
//    protected void stop(final @NotNull AbstractTask model) {
//        throw new StopException(model.getState());
//    }
//
//    /**
//     * If the {@code TaskExecutionService} this TaskAction runs on, is terminating or already terminated,
//     * a {@link ShutdownTaskActionException} is thrown.
//     *
//     * @param e an InterruptedException, which will be passed to the {@code ShutdownTaskActionException}
//     */
//    protected final void shutdownIfTerminating(InterruptedException e) {
//        if (getService().isTerminating())
//            throw new ShutdownTaskActionException(e);
//    }
//
//    @SuppressWarnings({ "MethodMayBeStatic" })
//    protected final void honorOngoingTransit(RuntimeException e) {
//        if (isTransitException(e))
//            throw e;
//    }
//
//    public static boolean isTransitException( Exception e ) {
//        return e instanceof TransitException;
//    }
//
//
//    public static boolean isFinishedTransition( Exception e ) {
//        return e instanceof FinishedException;
//    }
//
//
//    public static boolean isFailedTransition( Exception e ) {
//        return e instanceof FailedException;
//    }

//
//    /**
//     * Tries to call the {@link #cleanUpOnFail(de.zib.gndms.model.gorfx.AbstractTask)} } method for the model,
//     * catches and logs possible exceptions.
//     *
//     * @param model The model, on which a cleanup must be done.
//     */
//    public void tryCleanup( @NotNull Taskling model )  {
//
//        try{
//            cleanUpOnFail( model );
//        } catch ( Exception e ) {
//            // don' throw them again
//            getLogger().debug( "Exception on task cleanup: " + e.toString() );
//        }
//    }
//
//    /**
//     *
//     */
//    private static final class ShutdownTaskActionException extends RuntimeException {
//        private static final long serialVersionUID = 2772466358157719820L;
//
//        ShutdownTaskActionException(final Exception eParam) {
//            super(eParam);
//        }
//    }
//
//    /**
//     * A TransitException is used to store a new desired {@code TaskState}, the model (an AbstractTask) shall use.
//     * Its state can be changed using {@link AbstractTask#transit(TaskState)}.
//     * Note that there is a predefined order in wich state a TaskState can be changed.
//     *
//     * There a some special subclasses used to stop an AbstractTask in special situations.
//     *
//     * To stop an AbstractTask use the subclass {@link StopException}.
//     * If the model has finished its computation, throw a {@link FinishedException}
//     * If something went wrong, throw a {@link FailedException}
//     *
//     * @see TaskState
//     * @see AbstractTask
//     */
//    protected static class TransitException extends RuntimeException {
//        private static final long serialVersionUID = 1101501745642141770L;
//
//        /**
//         * the new, desired TaskState for the model.
//         * Its state is changed using {@link AbstractTask#transit(TaskState)}
//         */
//        protected final TaskState newState;
//
//        /**
//         * Creates a new TransitException and stores the new state, a corresponding AbstractTask shall use.
//         *
//         * @param newStateParam the new TaskState for an AbstractTask
//         */
//        protected TransitException(final @NotNull TaskState newStateParam) {
//            super();
//            newState = newStateParam;
//        }
//
//        /**
//         * Creates a new TransitException and stores the new state, a corresponding AbstractTask shall use.
//         * A RuntimeException must be denoted, which will be included in this RuntimeException.
//         *
//         * @param newStateParam the new TaskState for an AbstractTask
//         * @param cause the RuntimeException, which will be included in this RuntimeException
//         */
//        protected TransitException(final @NotNull TaskState newStateParam,
//                                   final @NotNull RuntimeException cause) {
//            super(cause);
//            newState = newStateParam;
//        }
//
//        public boolean isDemandingAbort() { return false; }
//    }
//
//    /**
//     * A TransitException used to signalize that a failure occured during the model's computation.
//     *
//     * It sets {@link TransitException#newState} to {@code FAILED} and stores a RuntimeException.
//     *
//     */
//    protected static class FailedException extends TransitException {
//        private static final long serialVersionUID = -4220356706557491625L;
//
//        protected FailedException(final @NotNull RuntimeException cause) {
//            super(TaskState.FAILED, cause);
//        }
//
//        @Override
//        public boolean isDemandingAbort() { return true; }
//    }
//
//   /**
//     * A TransitException used to signalize that model's computation is finished.
//     *
//     * It stores the result of the computation and sets {@link TransitException#newState} to {@code FINISHED}
//     */
//    protected static class FinishedException extends TransitException {
//        private static final long serialVersionUID = 196914329532915066L;
//
//        protected final Serializable result;
//
//        protected FinishedException(final Serializable resultParam) {
//            super(TaskState.FINISHED);
//            result = resultParam;
//        }
//    }
//
//   /**
//     * A TransitException used to signalize that the model's computation has to stop now.
//    *
//     */
//    private static class StopException extends TransitException {
//        private static final long serialVersionUID = 7783981039310846994L;
//
//       /**
//        * Signalizes that the TaskAction has to stop now.
//        *
//        * @param newStateParam the current TaskState of the model.
//        */
//        protected StopException(final @NotNull TaskState newStateParam) {
//            super(newStateParam);
//        }
//
//        @Override
//        public boolean isDemandingAbort() { return true; }
//    }



    public @NotNull ModelUpdateListener<Taskling> getModelUpdateListener() {
        return modelUpdateListener;
    }

    protected void setModelUpdateListener(@NotNull ModelUpdateListener<Taskling> listener) {
        modelUpdateListener = listener;
    }
}
