package de.zib.gndms.logic.action;

/**
 * Thrown by CommandAction.getOption()
 *
 * @see CommandAction
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 18:06:09
 */
public class MandatoryOptionMissingException extends Exception {
    private static final long serialVersionUID = -8091452559507426974L;


    public MandatoryOptionMissingException() {
        super();
    }


    public MandatoryOptionMissingException(final String message) {
        super(message);
    }


    public MandatoryOptionMissingException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public MandatoryOptionMissingException(final Throwable cause) {
        super(cause);
    }
}
