package de.zib.gndms.stuff.config;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;

/**
 * Represents an infinite stream of objects.
 *
 * Subclass are not guaranteed to be thread-safe.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.07.2008 Time: 23:12:45
 */
public abstract class InfiniteEnumeration<T> implements Enumeration<T> {
	@Nullable private T current;

	/**
	 * @return true
	 */
	public boolean hasMoreElements() {
		return true;
	}

	/**
	 * @return a non-null object of type T either retrieved using tryGetNextElement(), or if that
	 * returned null, the result of calling createInitialDefaultElement() if this is the first call,
	 * or the last element returned by nextElement(), otherwise.
	 */
	@NotNull
	public final synchronized T nextElement() {
		final T nextElement = tryNextElement();
		if (nextElement == null) {
			if (current == null)
				current = createInitialDefaultElement();
			return current;
		}
		else
			current = nextElement;
		return current;
	}

	/**
	 *
	 * @return nextElement() if that is not equal() to the current one, null otherwise.
	 */
	public final synchronized T nextElementIfDifferent() {
		final T oldCurrent = current;
		final T nextElement = nextElement();
		if (areEqualElements(oldCurrent, nextElement))
			return null;
		else
			return nextElement;
	}

    protected boolean areEqualElements(final T oldCurrent, final T nextElement) {
        return oldCurrent == nextElement ||
                (oldCurrent == null ? nextElement.equals(oldCurrent) : oldCurrent.equals(nextElement));
    }

    @NotNull
	public final synchronized T currentElement() {
		if (current == null)
			throw new IllegalStateException(
				  "currentElement() called before first call to nextElement()");
		return current;
	}

	/**
	 * @return an object if one is available, null otherwise
	 */
	@Nullable
	protected abstract T tryNextElement();

	/**
	 * @return a newly created default object. will be called at most once per instance.
	 */
	@NotNull
	protected abstract T createInitialDefaultElement();
}
