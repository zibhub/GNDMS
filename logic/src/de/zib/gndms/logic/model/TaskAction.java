package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.io.Serializable;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.09.2008 Time: 11:26:48
 */
@SuppressWarnings({ "AbstractMethodCallInConstructor" })
public abstract class TaskAction<M extends Task> extends AbstractModelAction<M, Serializable> {
    private ExecutorService service;

    protected static class TransitException extends RuntimeException {
        private static final long serialVersionUID = 1101501745642141770L;

        protected final TaskState newState;

        protected TransitException(final @NotNull TaskState newStateParam) {
            super();
            newState = newStateParam;
        }

        protected TransitException(final @NotNull TaskState newStateParam,
                                   final @NotNull RuntimeException cause) {
            super(cause);
            newState = newStateParam;
        }
    }

    protected static class FailedException extends TransitException {
        private static final long serialVersionUID = -4220356706557491625L;

        protected FailedException(final @NotNull RuntimeException cause) {
            super(TaskState.FAILED, cause);
        }
    }

    protected static class FinishedException extends TransitException {
        private static final long serialVersionUID = 196914329532915066L;

        protected final Serializable result;

        protected FinishedException(final Serializable resultParam) {
            super(TaskState.FINISHED);
            result = resultParam;
        }
    }

    protected static class StopException extends TransitException {
        private static final long serialVersionUID = 7783981039310846994L;


        protected StopException(final @NotNull TaskState newStateParam) {
            super(newStateParam);
        }
    }

    public TaskAction(final @NotNull UUID pk) {
        super();
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            final M model = em.find(getTaskClass(), pk);
            em.getTransaction().commit();
            setModel(model);
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public TaskAction() {
        super();
    }


    @Override
    public void preInitialize() {
        if (getModel() == null)
            setModel(createInitialTask());
    }

    
    protected abstract @NotNull M createInitialTask();


    protected abstract @NotNull Class<M> getTaskClass();


    public ExecutorService getService() {
        if (service == null) {
            final TaskAction<?> taskAction = nextParentOfType(TaskAction.class);
            return taskAction == null ? null : taskAction.getService();
        }
        return service;
    }


    public void setService(final @NotNull ExecutorService serviceParam) {
        if (service == null)
            service = serviceParam;
        else
           throw new IllegalStateException("Can't overwrite service");
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    public Serializable execute(final @NotNull EntityManager em) {
        return transititionLoop();
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "ObjectAllocationInLoop" })
    private Serializable transititionLoop() {
        boolean first = true;
        for (TransitException curEx = null; curEx != null;) {
            try {
                try {
                    if (first) {
                        transit(null);
                        first = false;
                    }
                    else
                        transit(curEx.newState);
                    // if we come here, transit has failed
                    throw new IllegalStateException("No proper TransitException thrown");
                }
                catch (final StopException newEx) {
                    curEx = null;
                }
                catch (final TransitException newEx) {
                    curEx = newEx;
                }
                catch (RuntimeException e) {
                    fail(e);
                }
            }
            catch (TransitException newEx) {
                curEx = newEx;
            }
        }
        return getModel().getData();
    }


    private void transit(final TaskState newState) {
        final EntityManager em = getEntityManager();
        try {
            final @NotNull M model = getModel();
            final TaskState goalState = newState == null ? model.getState() : newState;
            em.getTransaction().begin();
            model.setState(goalState);
            em.merge(model);
            em.getTransaction().commit();
            transit(goalState, model);
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    private void transit(final @NotNull TaskState newState, final @NotNull M model) {
        switch (model.getState().transit(newState)) {
            case CREATED: onCreated(model); break;
            case CREATED_UNKNOWN: onUnknown(model); break;
            case INITIALIZED: onInitialized(model); break;
            case INITIALIZED_UNKNOWN: onUnknown(model); break;
            case IN_PROGRESS: onInProgress(model); break;
            case IN_PROGRESS_UNKNOWN: onUnknown(model); break;
            case FAILED: onFailed(model); throw new StopException(TaskState.FAILED);
            case FINISHED: onFinished(model); throw new StopException(TaskState.FINISHED);
            default:
                throw new IllegalStateException("Invalid or unsupported task state");
        }
    }

    protected TaskState onUnknown(final M model) {
        throw new UnsupportedOperationException();
    }


    protected void onCreated(final M model) {
        transitToState(TaskState.INITIALIZED);

    }

    protected void onInitialized(final M model) {
        transitToState(TaskState.IN_PROGRESS);
    }

    protected abstract void onInProgress(final M model);


    protected void onFailed(final M model) {
        stop(model);
    }


    protected void onFinished(final M model) {
        stop(model);
    }


    protected static void transitToState(final @NotNull TaskState newState) {
        throw new TransitException(newState);
    }


    protected void fail(final @NotNull RuntimeException e) {
        getModel().fail(e);
        throw new FailedException(e);
    }


    protected void finish(final Serializable result) {
        getModel().finish(result);
        throw new FinishedException(result);
    }

    
    private void stop(final M model) {throw new StopException(model.getState());}
}
