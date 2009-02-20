package de.zib.gndms.logic.action;

/**
 * A CompositeAction is an {@code action} of Type <i>R</i> ,which can use several actions of Type <i>V</i> to return its result.
 *
 * Thus, an implementation could execute a list of actions of Type <i>V</i> and return itself a result
 * of Type <i>R</i> , based upon the results of the executed actions.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:06:11
 */
public interface CompositeAction<R,V> extends Action<R> {

    void addAction(Action<V> actionParam);
}
