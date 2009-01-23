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
     * Checks if the used configuration has a specific option
     * @param name the name of the option
     * @return true if the used configuration has specified a value for this option
     */
    boolean hasOption(final @NotNull String name);

    /**
     * 
     * @param nameParam
     * @return
     */
    String getNonMandatoryOption(final String nameParam);

    /**
     * 
     * @param nameParam the name of the option
     * @return  
     * @throws MandatoryOptionMissingException if the configuration has not set the option
     */
    @NotNull String getOption(final String nameParam) throws MandatoryOptionMissingException;

    /**
     * 
     * @param name
     * @param def
     * @return
     */
    @NotNull String getOption(final @NotNull String name, final @NotNull String def);
}
