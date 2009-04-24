package de.zib.gndms.logic.model;

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
