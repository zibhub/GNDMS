package de.zib.gndms.kit.configlet;

import de.zib.gndms.kit.config.MapConfig;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;


/**
 * Provides a default implementation of the {@code Configlet-Interface}. Data will be stored into a {@link MapConfig}.
 * Everytime the object is updated, a new {@code Mapconfig}-Object will be created.
 *
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:20:09
 */
public class DefaultConfiglet implements Configlet {
    /**
     * Holds the data of the current configuration
     */
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

    /**
     * Returns the current configurations-map. Method is synchronized.
     * @return the current configurations-map
     */
	public synchronized MapConfig getMapConfig() {
		return mapConfig;
	}

    /**
     * Returns the used logger
     * @return the used logger
     */
	public Log getLog() {
		return log;
	}

    /**
     * Returns the name of the Configlet
     * @return the name of the Configlet
     */
	public String getName() {
		return name;
	}

    /**
     * Overrides used {@code Map} with the new configuration. Method is synchronized.
     * @param data the new configurations,
     * expected to be a {@code Map<String, String>}  mapping the {@code optionnames} to their {@code values}
     */
	@SuppressWarnings({ "unchecked" })
	private synchronized void configConfig(final Serializable data) {
		final Map<String, String> map = (Map<String, String>) data;
		mapConfig = new MapConfig(map);
    }
}
