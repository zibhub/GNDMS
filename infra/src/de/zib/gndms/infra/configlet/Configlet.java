package de.zib.gndms.infra.configlet;

import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 17:02:47
 */
public interface Configlet {
	void init(@NotNull final Log loggerParam, @NotNull final String name, Serializable data);
	void update(@NotNull Serializable data);
	void shutdown();
}
