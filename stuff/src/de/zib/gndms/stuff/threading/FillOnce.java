package de.zib.gndms.stuff.threading;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.jetbrains.annotations.NotNull;

/**
 * A FillOnce instance represents an object holding exactly one value.
 *
 *
 * When retrieving its value {@code get()} will wait, if no value has been set yet.
 *
 * @param <T> the class type of the one value, a FillOnce instance contains.
 *
 * @deprecated Use DV, it's way more cool
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
