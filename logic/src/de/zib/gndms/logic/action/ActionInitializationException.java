package de.zib.gndms.logic.action;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 13.08.2008 Time: 10:32:17
 */
public class ActionInitializationException extends RuntimeException {
	private static final long serialVersionUID = 161746780645573512L;


	public ActionInitializationException() {
		super();
	}


	public ActionInitializationException(final String message) {
		super(message);
	}


	public ActionInitializationException(final String message, final Throwable cause) {
		super(message, cause);
	}


	public ActionInitializationException(final Throwable cause) {
		super(cause);
	}
}
