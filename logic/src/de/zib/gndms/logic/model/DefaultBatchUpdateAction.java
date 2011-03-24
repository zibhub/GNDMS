package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.logic.action.DefaultCompositeAction;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Collection;


/**
 * The DefaultBatchUpdateAction provides a default implementation of a BatchUpdateAction. 
 *
 * All actions in the list will be children of this BatchUpdateAction.
 *
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:54:38
 */
public class DefaultBatchUpdateAction<M extends GridResource> extends DefaultCompositeAction<Void, Void>
	implements BatchUpdateAction<M, Void> {

	private EntityUpdateListener<M> listener;


    @Override
    public void initialize() {
        if (getActions() == null)
            setActions(new LinkedList<Action<Void>>());
        super.initialize();    // Overridden method
    }


    public EntityUpdateListener<M> getListener() {
		return listener;
	}


	public void setListener(final @NotNull EntityUpdateListener<M> listenerParam) {
		listener = listenerParam;
	}


    /**
     * Sets {@code this} as parent of {@code voidActionParam} and
     * adds {@code voidActionParam} to the list of actions to be executed.
     *
     * @param voidActionParam an action which should be executed
     * @see DefaultCompositeAction#addAction(de.zib.gndms.logic.action.Action) 
     */
    @Override
    public void addAction(final Action<Void> voidActionParam) {
        voidActionParam.setParent(this);
        super.addAction(voidActionParam);    // Overridden method
    }


    @Override
    public void cleanUp() {
        final Collection<Action<Void>> collection = getActions();
        if (collection != null)
            collection.clear();
        super.cleanUp();    // Overridden method
    }
}
