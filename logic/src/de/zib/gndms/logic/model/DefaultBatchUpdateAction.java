package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.DefaultCompositeAction;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:54:38
 */
public class DefaultBatchUpdateAction extends DefaultCompositeAction<Void, Object>
	implements BatchUpdateAction<Void> {

	private EntityUpdateListener listener;

	
	public EntityUpdateListener getListener() {
		return listener;
	}


	public void setListener(final @NotNull EntityUpdateListener listenerParam) {
		listener = listenerParam;
	}
}
