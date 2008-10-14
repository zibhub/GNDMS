package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 10:52:30
 */
public interface OptionProvider {
    boolean hasOption(final @NotNull String name);
    
    String getNonMandatoryOption(final String nameParam);

    @NotNull String getOption(final String nameParam) throws MandatoryOptionMissingException;

    @NotNull String getOption(final @NotNull String name, final @NotNull String def);
}
