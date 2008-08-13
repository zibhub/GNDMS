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
	private Class<? extends GridResource> modelClazz;
	private String uuid;

	private EntityUpdateListener listener;


	public ModelChangedAction(final Class<? extends GridResource> modelClazzParam,
	                          final String uuidParam) {
		super();
		modelClazz = modelClazzParam;
		uuid = uuidParam;
	}


	@Override
	public void initialize() {
		super.initialize();    // Overridden method
		final DefaultBatchUpdateAction batchUpdateAction =
                nextParentOfType(DefaultBatchUpdateAction.class);
		if (batchUpdateAction == null)
			throw new IllegalStateException("No containing BatchUpdateAction found");
		
		setListener(batchUpdateAction.getListener());
		if (listener == null)
			throw new IllegalStateException("No listener set");
	}


	@Override
	public Void execute() {
		listener.onModelChange(modelClazz, uuid);
		return null;
	}


	public Class<? extends GridResource> getModelClazz() {
		return modelClazz;
	}


	public void setModelClazz(final Class<? extends GridResource> modelClazzParam) {
		modelClazz = modelClazzParam;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(final String uuidParam) {
		uuid = uuidParam;
	}


	public EntityUpdateListener getListener() {
		return listener;
	}


	public void setListener(final EntityUpdateListener listenerParam) {
		listener = listenerParam;
	}
}
