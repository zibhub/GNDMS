package de.zib.gndms.logic.model.gorfx;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 14:40:12
 */
public class PermissionDeniedORQException extends RuntimeException {
    private static final long serialVersionUID = -5392892017807237052L;


    public PermissionDeniedORQException() {
        super();
    }


    public PermissionDeniedORQException(final String message) {
        super(message);
    }


    public PermissionDeniedORQException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public PermissionDeniedORQException(final Throwable cause) {
        super(cause);
    }
}
