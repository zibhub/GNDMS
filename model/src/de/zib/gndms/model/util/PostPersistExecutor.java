package de.zib.gndms.model.util;

import org.jetbrains.annotations.NotNull;

/**
 * Implementors listen on Post-Persist events
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 04.08.2008 Time: 11:52:54
 */
public interface PostPersistExecutor<T> {
	<J extends T> void onPostPersist(@NotNull Class<J> clazz, @NotNull J persisted);
}
