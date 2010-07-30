package de.zib.gndms.stuff.threading;

import org.jetbrains.annotations.NotNull;

/**
 * A FillOnce instance represents an object holding exactly one value.
 *
 *
 * When retrieving its value {@code get()} will wait, if no value has been set yet.
 *
 * @param <T> the class type of the one value, a FillOnce instance contains.
 */
public final class FillOnce<T> {
    private final boolean ignoreConflicts;

    private volatile T value;


    /**
     * Creates a new FillOnce instance.
     * If {@code ignoreConflictsParam} is false an Execption will be thrown while trying to override the value.
     *
     * @param ignoreConflictsParam decides whether an Exception will be thrown while trying to override the value
     */
    public FillOnce(final boolean ignoreConflictsParam) {
        ignoreConflicts = ignoreConflictsParam;
    }


    public synchronized void set(final @NotNull T newVal) {
        if (value == null) {
            value = newVal;
            notifyAll();
        }
        else
            if (! ignoreConflicts)
                throw new IllegalStateException("Second attempt to set value");
    }

    /**
     * Returns the value set for this.
     * If it has not been set yet, it will wait.
     *
     * @return the value set for this
     */
    public synchronized T get() {
       while (value == null)
           try {
               wait();
           }
           catch (InterruptedException e) {
               // ignore
           }
        return value;
    }

    /**
     * Return whether a value has already been assigned to this.
     *
     * @return whether a value has already been assigned to this.
     */
    public synchronized boolean has() {
        return value == null;
    }

    /**
     * Removes the value instance from this, so that a new value can be assigned
     */
    public synchronized void reset() {
        value = null;
    }
}
