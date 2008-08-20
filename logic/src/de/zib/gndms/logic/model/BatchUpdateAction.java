package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.CompositeAction;
import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 13.08.2008 Time: 10:35:07
 */
public interface BatchUpdateAction<M extends GridResource, R> extends CompositeAction<R, Void> {

	EntityUpdateListener<M> getListener();

	void setListener(final @NotNull EntityUpdateListener<M> listenerParam);

}
