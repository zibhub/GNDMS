package de.zib.gndms.kit.config;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 10:52:30
 */
public interface OptionProvider extends Iterable<String> {

    /**
     * Checks if the current used configuration has a specific option
     * @param name the name of the option
     * @return true if the used configuration has specified a value for this option
     */
    boolean hasOption(final @NotNull String name);

    /**
     * Returns the String set in the current configuration for a specific option
     * @param nameParam the name of the option
     * @return the String set in the current configuration for a specific option
     */
    String getNonMandatoryOption(final String nameParam);

    /**
     * Returns the value set in the current configuration for the chosen option
     * @param nameParam the name of the option
     * @return  the value set in the current configuration
     * @throws MandatoryOptionMissingException if the configuration has not set the option
     */
    @NotNull String getOption(final String nameParam) throws MandatoryOptionMissingException;

    /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    @NotNull String getOption(final @NotNull String name, final @NotNull String def);
}
