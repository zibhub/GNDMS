package de.zib.gndms.kit.configlet;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.kit.config.MapConfig;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;


/**
 * Provides a default implementation of the {@code Configlet-Interface}. Data will be stored using a {@link MapConfig}.
 * Everytime the configuration is updated, a new {@code Mapconfig}-Object, storing the data, will be created.
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
     *
     * @return the current configurations-map
     */
	public synchronized MapConfig getMapConfig() {
		return mapConfig;
	}

    /**
     * Returns the used logger
     *
     * @return the used logger
     */
	public Log getLog() {
		return log;
	}

    /**
     * Returns the name of the Configlet
     *
     * @return the name of the Configlet
     */
	public String getName() {
		return name;
	}

    /**
     * Overrides used {@code Map} with the new configuration. Method is synchronized.
     *
     * @param data the new configurations,
     * expected to be a {@code Map<String, String>}  mapping the {@code optionnames} to their {@code values}
     */
	@SuppressWarnings({ "unchecked" })
	protected synchronized void configConfig(final Serializable data) {
		final Map<String, String> map = (Map<String, String>) data;
		mapConfig = new MapConfig(map);
    }
}
