package de.zib.gndms.logic.model.gorfx;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.11.2008 Time: 16:36:32
 */
public interface Wrapper<T> {
	<X extends T, Y extends T> Y wrap(Class<Y> wrapClass, X wrapped);
}
