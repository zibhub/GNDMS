package de.zib.gndms.infra;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;

/**
 * Represents an infinite stream of objects.
 *
 * Subclass instances are not guaranteed to be thread-safe.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
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
	public final T nextElement() {
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
	public final T nextElementIfDifferent() {
		final T oldCurrent = current;
		final T nextElement = nextElement();
		if (nextElement().equals(oldCurrent))
			return null;
		else
			return nextElement;
	}

	@NotNull
	public final T currentElement() {
		if (current == null)
			throw new IllegalStateException(
				  "currentElement() called before first call to nextElement()");
		return current;
	}

	/**
	 * @return an object if one is available, null otherwise
	 */
	@Nullable
	abstract T tryNextElement();

	/**
	 * @return a newly created default object. will be called at most once per instance.
	 */
	@NotNull
	abstract T createInitialDefaultElement();
}
