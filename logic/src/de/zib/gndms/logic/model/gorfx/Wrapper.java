package de.zib.gndms.logic.model.gorfx;

/**
 * An Interface for an Wrapper.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.11.2008 Time: 16:36:32
 */
public interface Wrapper<T> {
    /**
     * Wraps {@code wrapped} to an instance of class {@code Y}.
     *
     * @param wrapClass
     * @param wrapped
     * @param <X>
     * @param <Y>
     * @return
     */
	<X extends T, Y extends T> Y wrap(Class<Y> wrapClass, X wrapped);
}
