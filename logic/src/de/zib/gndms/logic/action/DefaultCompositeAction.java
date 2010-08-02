package de.zib.gndms.logic.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
 * @author: try ste fan pla nti kow zib
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
