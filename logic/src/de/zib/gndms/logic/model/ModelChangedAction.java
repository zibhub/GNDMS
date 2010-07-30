package de.zib.gndms.logic.model;

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



import de.zib.gndms.logic.action.AbstractAction;
import de.zib.gndms.model.common.GridResource;


/**
 * A ModelChangedAction informs a {@code BatchUpdateAction} about a model change.
 *
 * Therefore the {@code BatchUpdateAction} must be parent action of a {@code ModelChangedAction} instance.
 * The instance will then automatically retrieve and inform the parent's listener.
 *
 * @see GridResource
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:36:18
 */
public class ModelChangedAction extends AbstractAction<Void> {


    /**
     * the new model
     */
    private GridResource model;

    /**
     * the listener who will be informed about a model change.
     */
    private EntityUpdateListener<GridResource> listener;


	public ModelChangedAction( final GridResource modelParam ) {
        super();
        model = modelParam;
	}


	@Override
	public void initialize() {
		super.initialize();    // Overridden method
		final BatchUpdateAction<GridResource, ?> batchUpdateAction =
                nextParentOfType(BatchUpdateAction.class);
		if (batchUpdateAction == null)
			throw new IllegalStateException("No containing BatchUpdateAction found");
		
		setListener(batchUpdateAction.getListener());
        requireParameter("listener", getListener());
	}

    /**
     * Informs the listener of a BatchUpdateAction about a model change.
     *
     * @see AbstractAction#execute() 
     */
	@Override
	public Void execute() {
		listener.onModelChange( model );
		return null;
	}

    
	public final GridResource getModel() {
		return model;
	}


    /**
     * Sets the new model
     * @param modelParam the new model
     */
    public void setModel(final GridResource modelParam) {
		model = modelParam;
	}


	public EntityUpdateListener<GridResource> getListener() {
		return listener;
	}


	public void setListener(final EntityUpdateListener<GridResource> listenerParam) {
		listener = listenerParam;
	}
}
