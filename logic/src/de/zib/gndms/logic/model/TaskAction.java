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

    public TaskAction(final @NotNull UUID pk) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            final M model = em.find(getTaskClass(), pk);
            em.getTransaction().commit();
            setModel(model);
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }

    public TaskAction() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            final M model = createInitialTask();
            em.persist(model);
            em.getTransaction().commit();
            setModel(model);
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }

    protected abstract M createInitialTask();

    protected abstract Class<M> getTaskClass();

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


    @Override
    public Serializable execute(final @NotNull EntityManager em) {
        final M model = getModel();
        final TaskState state = model.getState();
        try {
            switch (state) {
                case FAILED:
                case FINISHED:
                    return model.getData();
                default:
                    transit(state, model);
            }
        }
        catch (TransitException e) {
            return transititionLoop(e);
        }
        throw new IllegalStateException("Invalid transition");
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "ObjectAllocationInLoop" })
    private Serializable transititionLoop(final TransitException transitEx) {
        for (TransitException curEx = transitEx; true;) {
            if (curEx instanceof FinishedException) {
                return ((FinishedException)curEx).result;
            }
            if (curEx instanceof FailedException) {
                return curEx.getCause();
            }
            try {
                transit(curEx.newState);
                fail(new IllegalStateException("No proper TransitException thrown"));
            }
            catch (final TransitException newEx) {
                curEx = newEx;
            }
        }
    }


    private void transit(final @NotNull TaskState newState) {
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            final @NotNull M model = getModel();
            model.setState(newState);
            em.merge(model);
            em.getTransaction().commit();
            transit(newState, model);
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    private void transit(final @NotNull TaskState newState, final @NotNull M model) {
        switch (newState) {
            case CREATED: onCreated(model); break;
            case CREATED_UNKNOWN: onUnknown(model); break;
            case INITIALIZED: onInitialized(model); break;
            case INITIALIZED_UNKNOWN: onUnknown(model); break;
            case IN_PROGRESS: onInProgress(model); break;
            case IN_PROGRESS_UNKNOWN: onUnknown(model); break;
            case FAILED:
                onFailed(model, new RuntimeException("Unspecified failure"));
                break;
            case FINISHED:
                onFinished(model, null);
                break;
            default:
                throw new AssertionError("Invalid or unsupported task state");
        }
    }

    protected TaskState onUnknown(final M model) {
        throw new UnsupportedOperationException();
    }


    protected void onCreated(final M model) {
        model.setProgress(0.0f);
        throw new TransitException(TaskState.INITIALIZED);

    }

    protected void onInitialized(final M model) {
        throw new TransitException(TaskState.IN_PROGRESS);
    }

    protected abstract void onInProgress(final M model);

    protected void onFailed(final M model, final @NotNull RuntimeException ex) {
        model.fail(ex);
    }


    protected void onFinished(final M model, final Serializable result) {
        model.finish(result);
    }

    protected static void fail(final @NotNull RuntimeException e) {
        throw new FailedException(e);
    }

    protected static void finish(final Serializable result) {
        throw new FinishedException(result);
    }

    protected static void transitToState(final @NotNull TaskState newState) {
        throw new TransitException(newState);
    }
}
