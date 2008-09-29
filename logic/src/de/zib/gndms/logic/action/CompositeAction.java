package de.zib.gndms.logic.action;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:06:11
 */
public interface CompositeAction<R,V> extends Action<R> {
	void addAction(Action<V> actionParam);
}
