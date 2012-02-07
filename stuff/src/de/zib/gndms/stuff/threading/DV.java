package de.zib.gndms.stuff.threading;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A dataflow variable~(DV) is a variable that can be set once to a value of type V or a Throwable of type T
 * (called cause).
 * <p/>
 * Implementations typically are thread-safe and instances may be used multiple times by "emptying" the DV
 * again by calling setEmpty()
 * <p/>
 * Multiple attempts to either set a value or set a cause will usually trigger an IllegalStateException
 * <p/>
 * Implementations may choose to provide two sets of calls for retrieving a DV's content.
 * <p/>
 * peekValue() calls retrieve the content without any side effect. Not all implementations may support this.
 * <p/>
 * getValue() calls retrieve the content and may take notice of this insofar as that the implementation
 * may be behave differently afterwards regarding future calls to a setter (i.e. reject further set attempts).
 * <p/>
 * In any case, multiple calls to getValue() or peekValue() without any interleaving calls to a setter are guaranteed
 * to always produce the same value.
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 17:05
 */
@SuppressWarnings({"UnusedDeclaration"})
public interface DV<V, T extends Throwable> extends Iterable<V> {

    /**
     * @return true, if the DV does not contain a value or a cause
     */
    public boolean isEmpty();


    /**
     * @return true, if the DV currently contains a value
     */
    public boolean hasValue();


    /**
     * @return true, if the DV currently contains a cause
     */
    public boolean hasCause();

    /**
     * Sets this DV to be empty again
     *
     * @throws UnsupportedOperationException if implementations choose to not provide this behavior
     */
    public void setEmpty() throws UnsupportedOperationException;

    /**
     * If possible, set this DV to contain a value and construct this value using newValue
     *
     * @param newValue the value to be used for setting the DV
     * @throws IllegalStateException    if the DV does not allow to be set at this point
     * @throws IllegalArgumentException if the DV does not allow to be set using newValue at this point
     */
    void setValue(V newValue) throws IllegalStateException, IllegalArgumentException;

    /**
     * If possible, set this DV to contain a cause and construct this cause using newCause
     *
     * @param newCause the cause to be used for setting the DV
     * @throws IllegalStateException    if the DV does not allow to be set to a value at this point
     * @throws IllegalArgumentException if the DV does not allow to be set to a cause using newCause at this point
     */
    void setCause(T newCause) throws IllegalStateException, IllegalArgumentException;

    /**
     * @return the current value to which this DV has been set
     *         <p/>
     *         This method may block indefinitely
     *         <p/>
     *         The DV may notice that the value has been retrieved and behave differently afterwards
     *         (i.e. not accept further calls to set).
     * @throws T if this DV does not contain a value but a cause of type T
     */
    V getValue() throws T;

    /**
     * @return the current value to which this DV has been set
     *         <p/>
     *         This method may block indefinitely
     * @throws T                             if this DV does not contain a value but a cause of type T
     * @throws UnsupportedOperationException if the DV implementation does not support peeking
     */
    V peekValue() throws T, UnsupportedOperationException;

    /**
     * @param timeunit number of unit units to be waited at most before a timeout occurs
     * @param unit     basic time unit used for specifying the timeout
     *                 <p/>
     *                 This method may block until timeunit units of unit have passed
     * @return the current value to which this DV has been set
     * @throws T                             if this DV does not contain a value but a cause of type T
     * @throws InterruptedException          if the method was interrupted while waiting for the DV to be set
     * @throws TimeoutException              if the method hit the timeout while waiting for the DV to be set
     * @throws UnsupportedOperationException if the DV implementation does not support peeking
     */
    V peekValue(long timeunit, TimeUnit unit)
            throws T, InterruptedException, TimeoutException, UnsupportedOperationException;


    /**
     * @param timeunit number of unit units to be waited at most before a timeout occurs
     * @param unit     basic time unit used for specifying the timeout
     *                 <p/>
     *                 This method may block until timeunit units of unit have passed
     *                 <p/>
     *                 The DV may notice that the value has been retrieved and behave differently afterwards
     *                 (i.e. not accept further calls to set).
     * @return the current value to which this DV has been set
     * @throws T                    if this DV does not contain a value but a cause of type T
     * @throws InterruptedException if the method was interrupted while waiting for the DV to be set
     * @throws TimeoutException     if the method hit the timeout while waiting for the DV to be set
     */
    V getValue(long timeunit, TimeUnit unit) throws T, InterruptedException, TimeoutException;


    /**
     * Constructs either an empty or a single value iterator for the DV value.  If the DV contains
     * a cause, this cause is thrown on the first call to Iterator.next() as a wrapped RuntimeException
     *
     * @return an iterator for the DV
     */
    Iterator<V> iterator();

    /**
     * A factory for DV instances
     *
     * @author try ste fan pla nti kow zib
     *         <p/>
     *         User stepn Date: 01.04.11 TIME: 21:44
     * @see DV
     */
    public static interface Factory {
        /**
         * @return new DV instance that is empty
         */
        <V, T extends Throwable> DV<V, T> newEmpty();

        /**
         * @param value the value to be put inside the returned DV
         * @return new DV instance that contains value as its value
         */
        <V, T extends Throwable> DV<V, T> newValue(V value);

        /**
         * @param cause the cause to be put inside the returned DV
         * @return new DV instance that contains cause as its cause
         */
        <V, T extends Throwable> DV<V, T> newCause(T cause);

    }
}
