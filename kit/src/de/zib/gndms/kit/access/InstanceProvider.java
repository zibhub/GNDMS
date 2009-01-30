package de.zib.gndms.kit.access;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:57:12
 */
public interface InstanceProvider {
	@NotNull <T> T waitForInstance(@NotNull Class<T> clazz, @NotNull String name);

	void addInstance(@NotNull String name, @NotNull Object obj);

	@NotNull <T> T getInstance(@NotNull Class<? extends T> clazz, @NotNull String name);
}
