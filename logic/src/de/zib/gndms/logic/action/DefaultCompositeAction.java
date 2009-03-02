package de.zib.gndms.logic.action;

import java.util.Collection;


/**
 * This class executes a list of actions concurrently.
 *
 * By default an instance of {@code DefaultCompositeAction} will return {@code null} after all actions have been executed.
 * Override {@link DefaultCompositeAction#getResult()} and {@link DefaultCompositeAction#addResult(Object)} to define
 * what the instance should return based upon the preceding results.
 *
 * The first template parameter is the return type, the second is the type of the other actions, wich can be added.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:07:11
 */
public class DefaultCompositeAction<R, V> extends AbstractAction<R> implements CompositeAction<R,V> 
{
    /**
     * a collection of all actions added by {@code addAction()}
     */
	private Collection<Action<V>> actions;


	@Override
	public void initialize() {
		if (actions == null)
			throw new IllegalStateException("No actions collection set");
	}


	@Override
	public final R execute() {
		for (Action<V> action : actions)
			addResult(action.call());
		return getResult();
	}

    /**
     * Returns the result of this action.
     * Must be overridden by subclasses to implement funcionality
     *
     * @return the result of this action
     */
	protected R getResult() {
		return null;
	}

    /**
     * Adds the result of an action to the calculation of the result of this.
     * Must be overridden by subclasses to implement funcionality
     * 
     * @param o the result of a preceding calculation made by an action of the list
     */
    protected void addResult(final V o) {

	}


	public void addAction(final Action<V> actionParam) {
		actions.add(actionParam);
	}


	@SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
	protected Collection<Action<V>> getActions() {
		return actions;
	}

    
    public void setActions(final Collection<Action<V>> actionsParam) {
		actions = actionsParam;
	}
}
