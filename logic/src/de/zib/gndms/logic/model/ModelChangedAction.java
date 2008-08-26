package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.AbstractAction;
import de.zib.gndms.model.common.GridResource;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:36:18
 */
public class ModelChangedAction extends AbstractAction<Void> {
    
    private GridResource model;

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


	@Override
	public Void execute() {
		listener.onModelChange( model );
		return null;
	}


	public final GridResource getModel() {
		return model;
	}


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
