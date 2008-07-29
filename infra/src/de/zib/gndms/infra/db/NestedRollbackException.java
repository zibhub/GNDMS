package de.zib.gndms.infra.db;

/**
 * Thrown by EntityManagerGuard to indicate a rollback in a nested transaction.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 15:27:59
 */
public class NestedRollbackException extends RuntimeException {
	private static final long serialVersionUID = -4309597721665512785L;

	public NestedRollbackException() {
		super();
	}

	public NestedRollbackException(String message) {
		super(message);
	}

	public NestedRollbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public NestedRollbackException(Throwable cause) {
		super(cause);
	}
}
