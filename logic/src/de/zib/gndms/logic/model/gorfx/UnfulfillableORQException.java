package de.zib.gndms.logic.model.gorfx;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 14:39:36
 */
public class UnfulfillableORQException extends RuntimeException {
    private static final long serialVersionUID = -7430762158076623931L;


    public UnfulfillableORQException() {
        super();
    }


    public UnfulfillableORQException(final String message) {
        super(message);
    }


    public UnfulfillableORQException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public UnfulfillableORQException(final Throwable cause) {
        super(cause);
    }
}
