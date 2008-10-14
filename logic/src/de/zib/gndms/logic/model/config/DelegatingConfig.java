package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 06.10.2008 Time: 11:21:46
*/
public class DelegatingConfig extends AbstractConfig {
    private final OptionProvider provider;


    public DelegatingConfig(final OptionProvider providerParam) {
        super();
        provider = providerParam;
    }


    public boolean hasOption(final @NotNull String name) {
        return provider.hasOption(name);
    }


    public String getNonMandatoryOption(final String nameParam) {
        return provider.getNonMandatoryOption(nameParam);
    }


    @Override
    public @NotNull String getOption(final String nameParam)
            throws MandatoryOptionMissingException {
        return provider.getOption(nameParam);
    }
}
