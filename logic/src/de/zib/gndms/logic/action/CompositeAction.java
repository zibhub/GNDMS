package de.zib.gndms.logic.action;

/**
 * A CompositeAction is an {@code action}, which can use several actions of another type to return its result.
 *
 * Thus, an implementation could execute a list of actions and return itself a result, 
 * based upon the results of the executed actions.
 *
 * The first template parameter is the return type, the second is the type of the other actions, wich can be added.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:06:11
 */
public interface CompositeAction<R,V> extends Action<R> {

    void addAction(Action<V> actionParam);
}
