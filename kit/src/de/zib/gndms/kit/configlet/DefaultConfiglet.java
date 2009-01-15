package de.zib.gndms.kit.configlet;

import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.configlet.Configlet;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:20:09
 */
public class DefaultConfiglet implements Configlet {
	private MapConfig mapConfig;
	private Log log;
	private String name;

	public void init(final @NotNull Log loggerParam,  @NotNull final String aName, final Serializable data) {
		configConfig(data);
		log = loggerParam;
		name = aName;
	}


	public void update(@NotNull final Serializable data) {
		configConfig(data);
	}


	public void shutdown() {
		// whatever
	}


	public synchronized MapConfig getMapConfig() {
		return mapConfig;
	}


	public Log getLog() {
		return log;
	}


	public String getName() {
		return name;
	}


	@SuppressWarnings({ "unchecked" })
	private synchronized void configConfig(final Serializable data) {
		final Map<String, String> map = (Map<String, String>) data;
		mapConfig = new MapConfig(map);
	}
}
