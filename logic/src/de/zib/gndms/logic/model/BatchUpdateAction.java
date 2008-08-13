package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.CompositeAction;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 13.08.2008 Time: 10:35:07
 */
public interface BatchUpdateAction<R> extends CompositeAction<R, Void> {

	EntityUpdateListener getListener();

	void setListener(final @NotNull EntityUpdateListener listenerParam);

}
