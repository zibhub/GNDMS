package de.zib.gndms.model.util;

import javax.persistence.*;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.11.2008 Time: 12:52:45
 */
public final class TxFrame {
	private final EntityManager em;

	private final boolean wasActive;
	private boolean locallyCommited = false;

	public TxFrame(final EntityManager emParam) {
		em = emParam;
		wasActive = em.getTransaction().isActive();
		begin();
	}


	public void begin() {
		if (wasActive)
			return;
		else
			em.getTransaction().begin();
	}

	public void commit() {
		if (! wasActive)
			em.getTransaction().commit();
		locallyCommited = true;
	}

	public void rollback() {
		if (wasActive)
			em.getTransaction().setRollbackOnly();
		else
			em.getTransaction().rollback();
	}

	public void finish() {
		if (locallyCommited)
			return;
		else
			rollback();
	}
}
