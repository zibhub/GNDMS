package de.zib.gndms.model.util;

import org.jetbrains.annotations.NotNull;

/**
 * Used to find an instance by GenericLifecycleListener
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 04.08.2008 Time: 11:34:07
 */
public interface InstanceResolver<T> {
	@NotNull
	<J extends T> J retrieveInstance(@NotNull Class<J> clazz, @NotNull String name);
}
