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



import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;
import de.zib.gndms.kit.config.OptionProvider;

/**
 * A Configlet stores a configuration and provides a method to update it.
 * 
 * A configuration object (see {@link OptionProvider}) can be created by every arbitriary {@code serializable} source.
 * 
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 17:02:47
 */
public interface Configlet {

    /**
     *  initializes Configlet with a logger, a start-configuration and a name
     *
     * @param loggerParam  
     * @param name  the name of the configuration
     * @param data  the configuration, expected to be a {@code Map<String, String>}
     */
    void init(@NotNull final Log loggerParam, @NotNull final String name, Serializable data);

    /**
     * Sets a new configuration
     *
     * @param data the new configuration, expected to be a {@code Map<String, String>}
     */
    void update(@NotNull Serializable data);

    /**
     * Cleanup
     */
    void shutdown();
}
