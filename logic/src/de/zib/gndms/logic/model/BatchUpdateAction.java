package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.DefaultCompositeAction;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:54:38
 */
public class BatchUpdateAction extends DefaultCompositeAction<Void, Void> {
	private EntityUpdateListener listener;

	
	public EntityUpdateListener getListener() {
		return listener;
	}


	public void setListener(final EntityUpdateListener listenerParam) {
		listener = listenerParam;
	}
}
