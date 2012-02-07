package de.zib.gndms.stuff.threading;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A flexible, thread-safe implementation of a data flow variable based on a lock and condition variable
 * <p/>
 * Multiple attempts to either set a value of set a cause usually result in an IllegalStateException. However this
 * behavior may be changed by overriding replaceValue or replaceCause in subclasses (potentially subject to weather the
 * DV value has been wasRetrieved by calling get).
 * <p/>
 * Additionally an invalid value may be converted to an exception and an exception may be converted to sensible
 * default value by overriding validateCause or toCause in subclasses.
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 17:05
 * @see DV
 * @see DefaultDV.Factory
 * @see ReentrantLock
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class DefaultDV<V, T extends Throwable> implements DV<V, T> {

    /**
     * Current completion state of a DefaultDV
     */
    public static enum State {
        /**
         * Used to indicate that a DV has not been set to either a cause or a value
         */
        EMPTY,

        /**
         * Used to indicate that a DV has been set to a valued
         */
        VALUE,

        /**
         * Used to indicate that a DV has been set to a cause
         */
        CAUSE;
    }

    // lock for protecting all DV state
    private final Lock lock = new ReentrantLock();

    // condition used to signal whenever a value or cause is set
    private final Condition wasSetCond = lock.newCondition();

    /* actual DV state */

    private State state = State.EMPTY;
    // if true, a DV cause or value was retrieved by a getXXX call
    private boolean wasRetrieved = false;
    private V value;
    private T cause;


    public final boolean isEmpty() {
        lock.lock();
        try {
            return State.EMPTY.equals(state);
        } finally {
            lock.unlock();
        }
    }

    public final boolean hasValue() {
        lock.lock();
        try {
            return State.VALUE.equals(state);
        } finally {
            lock.unlock();
        }
    }

    public final boolean hasCause() {
        lock.lock();
        try {
            return State.CAUSE.equals(state);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Empties this DV variable and resets the wasRetrieved state
     */
    public final void setEmpty() {
        lock.lock();
        try {
            state = State.EMPTY;
            value = null;
            cause = null;
            wasRetrieved = false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to set this DV's value using newValue.
     * <p/>
     * If DV is already set to another value, tries to merge the two values by calling replaceValue.
     *
     * @param newValue the candidate value for this DV
     * @throws IllegalArgumentException if the values cannot be merged
     * @throws IllegalStateException    if DV is already set to some cause
     */
    public void setValue(final V newValue) throws IllegalArgumentException, IllegalStateException {
        lock.lock();
        try {
            if (isEmpty()) {
                state = State.VALUE;
                value = newValue;
                wasSetCond.signalAll();
            } else {
                if (hasValue())
                    value = replaceValue(wasRetrieved, value, newValue);
                else
                    throw mkDVAlreadySet(wasRetrieved, false);
            }
        } finally {
            lock.unlock();
        }
    }


    /**
     * Try to merge oldValue and newValue into a final value for this DV.
     *
     * @param retrieved true if oldValue has already been seen by some client via a getXXX call
     * @param oldValue  the old value of this DV
     * @param newValue  the new candidate value of this DV
     * @return the replacement value for this DV
     * @throws IllegalArgumentException if the values cannot be merged
     */
    protected V replaceValue(boolean retrieved, final V oldValue, final V newValue) throws IllegalArgumentException {
        throw mkDVAlreadySet(retrieved, true);
    }


    /**
     * Attempts to set this DV's cause using newCause.
     * <p/>
     * If DV is already set to another cause, tries to merge the two causes by calling replaceCause.
     *
     * @param newCause the candidate cause for this DV
     * @throws IllegalArgumentException if the causes cannot be merged
     * @throws IllegalStateException    if DV is already set to some value
     */
    public void setCause(final T newCause) throws IllegalArgumentException, IllegalStateException {
        lock.lock();
        try {
            if (isEmpty()) {
                state = State.CAUSE;
                cause = newCause;
                wasSetCond.signalAll();
            } else {
                if (hasCause())
                    cause = replaceCause(wasRetrieved, cause, newCause);
                else
                    throw mkDVAlreadySet(wasRetrieved, false);
            }
        } finally {
            lock.unlock();
        }
    }


    /**
     * Try to merge oldCause and newCause into a final cause for this DV.
     *
     * @param retrieved true if oldCause has already been seen by some client via a getXXX call
     * @param oldCause  the old Cause of this DV
     * @param newCause  the new candidate cause of this DV
     * @return the replacement cause for this DV
     * @throws IllegalArgumentException if the causes cannot be merged
     */
    protected T replaceCause(boolean retrieved, final T oldCause, final T newCause) {
        throw mkDVAlreadySet(retrieved, true);
    }

    /**
     * @return cause, if one is set (Does not change the wasRetrieved state)
     */
    protected T peekCause() {
        lock.lock();
        try {
            if (State.CAUSE.equals(state))
                return cause;
            else
                throw new IllegalStateException("DV cause has not been set");
        } finally {
            lock.unlock();
        }
    }


    /**
     * @return state, if one is set (Does not change the wasRetrieved state)
     */
    protected State peekState() {
        lock.lock();
        try {
            return state;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return value, if a cause is set (Sets wasRetrieved state)
     * @throws T, if a cause is set (Sets wasRetrieved state)
     */
    public V getValue() throws T {
        return getOrPeekValue(false);
    }

    /**
     * @return value, if a cause is set (Does not change the wasRetrieved state)
     * @throws T, if a cause is set (Does not change the wasRetrieved state)
     */
    public V peekValue() throws T {
        return getOrPeekValue(true);
    }

    /**
     * @param timeunit amount of unit timeunits to wait
     * @param unit     timeunit used by timeunit
     * @return value, if a cause is set (Sets wasRetrieved state)
     * @throws T,                   if a cause is set (Sets wasRetrieved state)
     * @throws InterruptedException if the thread was interrupted
     * @throws java.util.concurrent.TimeoutException
     *                              if the result could not be determined with the given timeout
     */
    public V getValue(long timeunit, TimeUnit unit) throws T, InterruptedException, TimeoutException {
        return getOrPeekValue(false, timeunit, unit);
    }

    /**
     * @param timeunit amount of unit timeunits to wait
     * @param unit     timeunit used by timeunit
     * @return value, if a cause is set (Does not change the wasRetrieved state)
     * @throws T,                   if a cause is set (Does not change the wasRetrieved state)
     * @throws InterruptedException if the thread was interrupted
     * @throws java.util.concurrent.TimeoutException
     *                              if the result could not be determined with the given timeout
     */
    public V peekValue(long timeunit, TimeUnit unit) throws T, InterruptedException, TimeoutException {
        return getOrPeekValue(true, timeunit, unit);
    }

    private V getOrPeekValue(boolean peekOnly) throws T {
        lock.lock();
        try {
            if (isEmpty())
                wasSetCond.awaitUninterruptibly();

            return fetchValue(peekOnly);
        } finally {
            lock.unlock();
        }
    }

    private V getOrPeekValue(boolean peekOnly, long timeunit, TimeUnit unit)
            throws T, InterruptedException, TimeoutException {
        lock.lock();
        try {
            if (isEmpty())
                wasSetCond.await(timeunit, unit);

            if (/* still */ isEmpty())
                throw new TimeoutException();
            else
                return fetchValue(peekOnly);
        } finally {
            lock.unlock();
        }
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
     * Validation/Default value callback for causes for values
     *
     * @param value the value to which this DV has been set
     * @return a final result value returned to the user
     * @throws T a cause thrown if validation fails
     */
    protected V validateValue(final V value) throws T {
        return value;
    }

    /**
     * Validation/Default value callback for causes
     *
     * @param cause to which this DV has been set
     * @return a default value to be used instead
     * @throws T an actual cause to be thrown instead of cause
     */
    protected V validateCause(final T cause) throws T {
        throw cause;
    }

    private RuntimeException mkDVAlreadySet(boolean retrieved, boolean argError) {
        final String msg = retrieved ? "DV already set and wasRetrieved" : "DV already set";
        return argError ? new IllegalArgumentException(msg) : new IllegalStateException(msg);
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
                return hasNext && !isEmpty();
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

    /**
     * Factory for instances of class DefaultDV
     *
     * @author try ste fan pla nti kow zib
     * @see DefaultDV
     */
    public static class Factory implements DV.Factory {

        public <V, T extends Throwable> DefaultDV<V, T> newEmpty() {
            return new DefaultDV<V, T>();
        }

        public <V, T extends Throwable> DefaultDV<V, T> newValue(final V value) {
            final DefaultDV<V, T> dv = new DefaultDV<V, T>();
            dv.setValue(value);
            return dv;
        }

        public <V, T extends Throwable> DefaultDV<V, T> newCause(final T cause) {
            final DefaultDV<V, T> dv = new DefaultDV<V, T>();
            dv.setCause(cause);
            return dv;
        }

        public static DV.Factory getInstance() {
            return instance;
        }

        private static DefaultDV.Factory instance = new DefaultDV.Factory();
    }
}
