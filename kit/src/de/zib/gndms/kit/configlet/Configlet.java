package de.zib.gndms.kit.configlet;

import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;
import de.zib.gndms.kit.config.OptionProvider;

/**
 * A Configlet stores a configuration and provides a method to update it.
 * 
 * A configuration object (see {@link OptionProvider}) can be created by every arbitriary {@code serializable} source.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
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
