package de.zib.gndms.stuff.threading;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DV is a very flexible implementation of a data flow variable.
 *
 * A dataflow variable is a thread-safe variable that can be set once to a value of type V or a Throwable of type T
 * (called cause).
 *
 * Multiple attempts to either set a value of set a cause usually result in an IllegalStateException. However this
 * behavior may be changed by overriding replaceValue or replaceCause in subclasses (potentially subject to weather the
 * DV value has been wasRetrieved by calling get).
 *
 * Additionally an invalid value may be converted to an exception and an exception may be converted to sensible
 * default value by overriding validateCause or toCause in subclasses.
 *
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 17:05
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class DV<V, T extends Throwable> implements Iterable<V> {
    /**
     * State of a DV
     */
    public static enum State {
        /**
         * Used to indicate that a DV has not been set to either a cause or a value
         */
        UNSET,

        /**
         * Used to indicate that a DV has been set to a valued
         */
        VALUE,

        /**
         * Used to indicate that a DV has been set to a cause
         */
        CAUSE;

        /**
         * @return true if this state equals UNSET
         */
        public boolean isSet() { return ! UNSET.equals(this); }
    }

    // lock for protecting all DV state
    private final Lock lock = new ReentrantLock();

    // condition used to signal whenever a value or cause is set
    private final Condition wasSetCond = lock.newCondition();

    /* actual DV state */

    private State state = State.UNSET;
    // if true, a DV cause or value was retrieved by a getXXX call
    private boolean wasRetrieved = false;
    private V value;
    private T cause;


    /**
     * Attempts to set this DV to newValue.
     *
     * If DV is already set to another value, tries to merge the two values by calling replaceValue.
     *
     * @param newValue the candidate value for this DV
     *
     * @throws IllegalStateException if DV is already set to some cause
     */
    public void setValue(final V newValue) {
        lock.lock();
        try {
            if (state.isSet()) {
                if (State.VALUE.equals(state))
                    value = replaceValue(wasRetrieved, value, newValue);
                else
                    throw mkDVAlreadySet(wasRetrieved);
            }
            else {
                state  = State.VALUE;
                value  = newValue;
                wasSetCond.signalAll();
            }
        }
        finally { lock.unlock(); }
    }


    /**
     * Try to merge oldValue and newValue into a final value for this DV.
     *
     * @param retrieved true if oldValue has already been seen by some client via a getXXX call
     * @param oldValue the old value of this DV
     * @param newValue the new candidate value of this DV
     *
     * @return the replacement value for this DV
     *
     * @throws IllegalStateException if the values cannot be merged
     */
    protected V replaceValue(boolean retrieved, final V oldValue, final V newValue) {
        throw mkDVAlreadySet(retrieved);
    }


    /**
     * Attempts to set this DV to newCause.
     *
     * If DV is already set to another cause, tries to merge the two causes by calling replaceCause.
     *
     * @param newCause the candidate cause for this DV
     *
     * @throws IllegalStateException if DV is already set to some value
     */
    public void setCause(final T newCause) {
        lock.lock();
        try {
            if (state.isSet()) {
                if (State.CAUSE.equals(state)) {
                    cause = replaceCause(wasRetrieved, cause, newCause);
                }
                else
                    throw mkDVAlreadySet(wasRetrieved);
            }
            else {
                state  = State.CAUSE;
                cause  = newCause;
                wasSetCond.signalAll();
            }
        }
        finally { lock.unlock(); }
    }

    
    /**
     * Try to merge oldCause and newCause into a final cause for this DV.
     *
     * @param retrieved true if oldCause has already been seen by some client via a getXXX call
     * @param oldCause the old Cause of this DV
     * @param newCause the new candidate cause of this DV
     *
     * @return the replacement cause for this DV
     *
     * @throws IllegalStateException if the causes cannot be merged
     */
    protected T replaceCause(boolean retrieved, final T oldCause, final T newCause) {
        throw mkDVAlreadySet(retrieved);
    }

    /**
     * @return cause, if one is set (Does not change the wasRetrieved state)
     */
    public T peekCause() {
        lock.lock();
        try {
            if (State.CAUSE.equals(state))
                return cause;
            else
                throw new IllegalStateException("DV cause has not been set");
        }
        finally { lock.unlock(); }
    }


    /**
     * @return state, if one is set (Does not change the wasRetrieved state)
     */
    public State peekState() {
        lock.lock();
        try {
            return state;
        }
        finally { lock.unlock(); }
    }

    /**
     * @return value, if a cause is set (Sets wasRetrieved state)
     *
     * @throws T, if a cause is set (Sets wasRetrieved state)
     */
    public V getValue() throws T {
        return getOrPeekValue(false);
    }

    /**
     * @return value, if a cause is set (Does not change the wasRetrieved state)
     *
     * @throws T, if a cause is set (Does not change the wasRetrieved state)
     */
    public V peekValue() throws T {
        return getOrPeekValue(true);
    }

    /**
     * @param timeunit amount of unit timeunits to wait
     * @param unit timeunit used by timeunit
     *
     * @return value, if a cause is set (Sets wasRetrieved state)
     *
     * @throws T, if a cause is set (Sets wasRetrieved state)
     * @throws InterruptedException if the thread was interrupted
     * @throws TimeoutException if the result could not be determined with the given timeout
     */
    public V getValue(long timeunit, TimeUnit unit) throws T, InterruptedException, TimeoutException {
        return getOrPeekValue(false, timeunit, unit);
    }

    /**
     * @param timeunit amount of unit timeunits to wait
     * @param unit timeunit used by timeunit
     *
     * @return value, if a cause is set (Does not change the wasRetrieved state)
     *
     * @throws T, if a cause is set (Does not change the wasRetrieved state)
     * @throws InterruptedException if the thread was interrupted
     * @throws TimeoutException if the result could not be determined with the given timeout
     */
    public V peekValue(long timeunit, TimeUnit unit) throws T, InterruptedException, TimeoutException {
        return getOrPeekValue(true, timeunit, unit);
    }

    private V getOrPeekValue(boolean peekOnly) throws T {
        lock.lock();
        try {
            while (true) {
                if (state.isSet())
                    return fetchValue(peekOnly);
                else
                    wasSetCond.awaitUninterruptibly();
            }
        }
        finally { lock.unlock(); }
    }

    private V getOrPeekValue(boolean peekOnly, long timeunit, TimeUnit unit)
            throws T, InterruptedException, TimeoutException {
        lock.lock();
        try {
            if (! state.isSet())
                wasSetCond.await(timeunit, unit);

            if (state.isSet())
                return fetchValue(peekOnly);
            else
                throw new TimeoutException();
        }
        finally { lock.unlock(); }
    }

    private V fetchValue(boolean peekOnly) throws T {
        switch (state) {
            case VALUE:
                wasRetrieved |= !peekOnly;
                return validateValue(value);
            case CAUSE:
                wasRetrieved |= !peekOnly;
                return validateCause(cause);
             default:
                 throw new IllegalStateException("Unsupported DV state encountered");
        }
    }

    /**
     * Validation callback
     *
     *
     * @param value the value to which this DV has been set
     *
     * @return a final result value returned to the user
     *
     * @throws T a cause thrown if validation fails
     */
    protected V validateValue(final V value) throws T {
        return value;
    }

    /**
     * Default value callback
     *
     * @param cause to which this DV has been set
     *
     * @return a default value to be used instead
     *
     * @throws T an actual cause to be thrown instead of cause
     */
    protected V validateCause(final T cause) throws T {
        throw cause;
    }

    private IllegalStateException mkDVAlreadySet(boolean retrieved) {
        return new IllegalStateException(retrieved ?  "DV already set and wasRetrieved" : "DV already set");
    }


    /**
     * @return an empty or a single element iterator for this DV
     *
     * RuntimeException with boxed cause is thrown on iterator().next() if DV is set to one
     */
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            boolean hasNext = true;

            public boolean hasNext() {
                return hasNext && (peekState().isSet());
            }

            public V next() {
                hasNext = false;
                try {
                    return getValue();
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}