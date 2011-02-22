package de.zib.gndms.logic.model.gorfx;

/**
 * LifetimeExceededException
 *
 * @author try ste fan pla nti kow zib
 *
 * User stepn Date: 21.02.11 TIME: 11:01
 */
public class LifetimeExceededException extends RuntimeException {
    public LifetimeExceededException() {
        super();
    }

    public LifetimeExceededException(final String s) {
        super(s);
    }

    public LifetimeExceededException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public LifetimeExceededException(final Throwable throwable) {
        super(throwable);
    }
}
