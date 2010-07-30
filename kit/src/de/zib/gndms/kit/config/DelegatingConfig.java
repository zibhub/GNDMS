package de.zib.gndms.kit.config;

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
import de.zib.gndms.kit.config.MandatoryOptionMissingException;

import java.util.Iterator;


/**
 * This class stores a configuration-object
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


    @Override
    public String getNonMandatoryOption(final String nameParam) {
        return provider.getNonMandatoryOption(nameParam);    // Overridden method
    }


    @Override
    public String getConcreteNonMandatoryOption(final String nameParam) {
        return provider.getNonMandatoryOption(nameParam);
    }


    @Override
    public @NotNull String getOption(final String nameParam)
            throws MandatoryOptionMissingException {
        return provider.getOption(nameParam);
    }

    
    public Iterator<String> iterator() {return provider.iterator();}
}
