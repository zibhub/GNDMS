package de.zib.gndms.logic.action;

import java.util.Collection;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:07:11
 */
public class DefaultCompositeAction<R, V> extends AbstractAction<R> implements CompositeAction<R,V> 
{ 

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


	protected R getResult() {
		return null;
	}

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
