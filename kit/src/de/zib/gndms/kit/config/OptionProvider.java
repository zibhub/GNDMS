package de.zib.gndms.kit.config;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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


/**
 * An Interface for an configuration Object. <br>
 *
 * The value set for an option is stored as a String.
 * A {@code getOption(String optionname)} method is used to get the value set for the option {@code optionname}
 * in a currently loaded configuration.
 *  
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 10:52:30
 */
public interface OptionProvider extends Iterable<String> {

    /**
     * Checks if the current used configuration has a specific option
     * 
     * @param name the name of the option
     * @return true if the used configuration has specified a value for this option
     */
    boolean hasOption(final @NotNull String name);

    /**
     * Returns the String set in the current configuration for a specific option
     *
     * @param nameParam the name of the option
     * @return the String set in the current configuration for a specific option
     */
    String getNonMandatoryOption(final String nameParam);

    /**
     * Returns the value set in the current configuration for the chosen option
     *
     * @param nameParam the name of the option
     * @return  the value set in the current configuration
     * @throws MandatoryOptionMissingException if the configuration has not set the option
     */
    @NotNull String getOption(final String nameParam) throws MandatoryOptionMissingException;

    /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     *
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    @NotNull String getOption(final @NotNull String name, final @NotNull String def);
}
