package de.zib.gndms.logic.action;

/**
 * Used to skip action initialization and executing; i.e. when printing a help screen.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 17:56:50
 */
public class SkipActionInitializationException extends ActionInitializationException {
    private static final long serialVersionUID = 5225015991599636796L;


    public SkipActionInitializationException() {
    }


    public SkipActionInitializationException(final String message) {
        super(message);
    }


    public SkipActionInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public SkipActionInitializationException(final Throwable cause) {
        super(cause);
    }
}
