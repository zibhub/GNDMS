package de.zib.gndms.infra.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Encapsulates an entity manager for semi-nested transaction support.
 * Tx begin returns a boolean flag that indicates whether an new transaction was started or if there
 * already was an ongoing transaction. Later commit/rollback calls are executed only if the
 * flag was true. Otherwise they do nothing. Nested rollbacks rollback the outermost transaction.
 * Additonally, thunks can be registered in FIFO order for execution *after* succesful commit to
 * the database. Typical usage:
 *
 * <pre>
 * final boolean wasNewTx = emg.begin();
 * try {
 *      // 1) JPA database accesing commands
 *      // 2) Opt. Thunk registration
 *      emg.onCommitDo(new Runnable() {Ê
 *          public void run() {
 *              // code modifying state that is not under tx control *after* succesful commit
 *          }
 * }
 * // The catch is necessary to avoid commits in the presence of exceptions!
 * // Notice that you must never call rollback() in finally!
 * catch (RuntimeException re) { emg.rollback(wasNewTx, re); }
 * finally {
 *      // thunks will be executed at the end of this call *if* this stackframe created
 *      // the active transaction (wasNewTx is true)
 *      // Notice that you must always call commit() in finally!
 *      emg.commitAndClose(wasNewTx);
 * }
 *
 * Instances should only be used from one thread.
 *
 * If an additional ThreadLocal<EntityManager> emgs has been set via setEMThreadLocal(emgs),
 * emgs.remove() is called after succesful commitAndClose(), rollbackAndClose() and close() calls.
 *
 * @see GNDMSystem#currentEMG() for extended usage
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 14:02:45
 */
@SuppressWarnings({"BooleanMethodNameMustStartWithQuestion"})
public class EntityManagerGuard {
	/**
	 * The entity manager that is used by this guard; fixed at construction
	 */
	@NotNull
	private final EntityManager em;

	/**
	 * Opt. thread local that hold this EMG
	 */
	@Nullable
	private ThreadLocal<EntityManagerGuard> emgs;

	/**
	 * Lazily created queue of thunks-to-be-executed on succesful commit
	 */
	@Nullable
	private Queue<Runnable> thunks;

	private boolean rollingBack;

	/***
	 *
	 * @param theEM an open entity manager whose FlushModeType will be set to COMMIT
	 */
	public EntityManagerGuard(@NotNull EntityManager theEM) {
		em = theEM;
		em.setFlushMode(FlushModeType.COMMIT);
		assertEMisOpen();
	}

	/**
	 * Gives access to the EM; if you explicitely call transaction management functions or
	 * close the EM you will break this EMG.
	 *
	 * @return the entity manager used by this EMG
	 */
	@NotNull
	public EntityManager getEM() {
		return em;
	}

	/**
	 * Sets the thread local of this EMG and sets this EM inside it using ThreadLocal.set().
	 * Additonally, on EMG.close(), this EMG will call ThreadLocal.remove().
	 *
	 * @param theEMgs
	 * @throws IllegalStateException if a thread local was already set
	 */
	public void setEMThreadLocal(@NotNull ThreadLocal<EntityManagerGuard> theEMgs) {
		if (emgs != null)
			throw new IllegalStateException("Cant overwrite previously set ThreadLocal<EMG> emgs");
		emgs = theEMgs;
		emgs.set(this);
	}

	/**
	 * Ensures the existence of an active transaction associated with the EM.
	 *
	 * @return true, if a new active transactions was begun. false, if an old one was found.
	 */
	public boolean begin() {
		assertEMisOpen();

		if (em.getTransaction().isActive())
			return false;
		else {
			em.getTransaction().begin();
			rollingBack = false;
			return true;
		}
	}

	/**
	 * Adds a thunk of code to be executed on the next database commit. All thunks are arranged in
	 * a FIFO queue internally. Therefore, onCommitDo, should be called in the current stack frame
	 * after all transactionally managed objects have been modified succesfully.
	 *
	 * Thunks must never fail and therefore be restricted to simple state change operations
	 * without external effects.
	 *
	 * @param thunk
	 */
	public void onCommitDo(@NotNull Runnable thunk) {
		getThunks().add(thunk);
	}

	public final void rollback(final boolean wasNewTx, @NotNull RuntimeException e) {
		rollingBack = true;
		throw e;
	}

	/**
	 * Rollbacks the EM's Tx if wasNewTx is true
	 *
	 * @param wasNewTx flag indicating whether this was the stack frame that called begin()
	 * @throws NestedRollbackException if wasNewTx is false
	 */
	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
	public final void rollback(final boolean wasNexTx) {
		rollback(wasNexTx, new NestedRollbackException());
	}


	/**
	 * Commits the EM's Tx if wasNewTx is true and executes all outstanding thunks;
	 * otherwise does nothing.
	 *
	 * @param wasNewTx flag indicating whether this was the stack frame that called begin()
	 */
	public final void commit(final boolean wasNewTx) {
		if (rollingBack)
			couldRollbackActiveTx(wasNewTx);
		else
			couldCommitActiveTx(wasNewTx);
	}

	public void commitAndClose(final boolean wasNewTx) {
		try {
			if (rollingBack)
				couldRollbackActiveTx(wasNewTx);
			else
				couldCommitActiveTx(wasNewTx);
		}
		finally {
			close();
		}
	}

	/**
	 * Flushes the underlying EM
	 */
	public final void flush() {
		em.flush();
	}

	public void close() {
		try {
			em.close();
		}
		finally {
			postClose();
		}
	}

	private void postClose() {
		thunks = null;
		try {
			if (emgs != null)
				emgs.remove();
		}
		finally {
			emgs = null;
		}
	}

	private boolean couldRollbackActiveTx(final boolean wasNewTx) {
		if (wasNewTx) {
			rollingBack = false;
			assertEMisOpen();
			assertActiveTx();

			em.getTransaction().rollback();
			clearThunks();
			return true;
		}
		else
			return false;
	}

	private boolean couldCommitActiveTx(final boolean wasNewTx) {
		if (wasNewTx) {
			assertEMisOpen();
			assertActiveTx();

			em.getTransaction().commit();
			executeThunks();
			return true;
		}
		else
			return false;
	}

	private void assertEMisOpen() {
		if (! em.isOpen()) {
			postClose();
			throw new IllegalStateException("EntityManager closed");
		}
	}

	private void assertActiveTx() {
		if (! em.getTransaction().isActive())
			throw new IllegalStateException("No active transaction");
	}

	private void clearThunks() {
		if (thunks != null)
			thunks.clear();
	}

	@SuppressWarnings({"InfiniteLoopStatement"})
	private void executeThunks() {
		if (thunks != null) {
			final Queue<Runnable> theThunks = thunks;
			try {
				try {
					while (true) {
						final Runnable nextThunk = theThunks.remove();
						nextThunk.run();
					}
				}
				catch (NoSuchElementException nse) {
					// intented
				}
			}
			finally {
				theThunks.clear();
			}
		}
	}

	@NotNull
	private Queue<Runnable> getThunks() {
		if (thunks == null)
			thunks = new LinkedList<Runnable>();
		return thunks;
	}
}
