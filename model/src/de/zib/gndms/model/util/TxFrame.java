package de.zib.gndms.model.util;

import javax.persistence.*;


/**
 * A TxFrame allows nesting within a transaction.
 *
 * It wrapps the methods {@code begin()}, {@code commit()}, {@code rollback()}, ensuring that
 * nested invocations do not interrupt with invocations of higher level.
 *
 * @see javax.persistence.EntityTransaction
 * @see javax.persistence.Entity
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.11.2008 Time: 12:52:45
 */
public final class TxFrame {


    private final EntityManager em;

    /**
     * Indicates whether the transaction of the entityManager has already been activated, before loaded into this TxFrame. 
     */
	private final boolean wasActive;
	private boolean locallyCommited = false;

    /**
     * When a new instance is created, it checks if {@code emParam}'s transaction is already active
     * and sets {@code wasActive} accordingly.
     * If the transaction is not already active, it will be started, by calling {@code begin()}.
     *
     * @param emParam An entityManager with a transaction 
     */
	public TxFrame(final EntityManager emParam) {
		em = emParam;
		wasActive = em.getTransaction().isActive();
		begin();
	}

    /**
     * Calls {@code begin()} on the current transaction of the entityManager, only if {@code wasActive} is {@code false}.
     * This allows nesting as it ensures that the method is invoked only once. A nested invocation does not call the
     * {@code begin()} method on the transaction.
     */
	public void begin() {
		if (wasActive)
			return;
		else
			em.getTransaction().begin();
	}

    /**
     *  Calls {@code commit()} on the current transaction of the entityManager, only if {@code wasActive} is {@code false}.
     * This mean {@code commit()} is only invoked, if {@code this} is the first level of the nesting chain.
     * Sets locallyCommited to {@code true}
     */
	public void commit() {
		if (! wasActive)
			em.getTransaction().commit();
		locallyCommited = true;
	}

    /**
     * If {@code wasActive} is {@code true}, so if {@code this} is not the first level of the nesting chain,
     * a rollback is marked only, using {@code setRollbackOnly() }.
     * All marked rollbacks are done when this method is invoked from a top level instance.
     */
	public void rollback() {
		if (wasActive)
			em.getTransaction().setRollbackOnly();
		else
			em.getTransaction().rollback();
	}

    /**
     * Calls {@link #rollback()} only if {@code loallyCommited} is set to {@code false}.
     */
	public void finish() {
		if (locallyCommited)
			return;
		else
			rollback();
	}
}
