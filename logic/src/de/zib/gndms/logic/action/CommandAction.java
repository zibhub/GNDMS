package de.zib.gndms.logic.action;

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



import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.ParameterTools;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


/**
 * Configuration actions take a string-string map parameter from some external source
 * and may write results to an optional print writer.
 *
 * A CommandAction instance has access to local and global options. Whereas local option are the ones
 * set in the configuration for a specific CommandAction object, global options are the local ones
 * plus all options from the set in the parent chain of the instance, being a CommandAction.
 *
 * The first template parameter is the return type.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 10:36:15
 */
public interface CommandAction<R> extends Action<R>, ConfigProvider {

    /**
     * Returns the map containing the local configuration settings.
     * 
     * @return the map containing the configuration settings for this {@code CommandAction}
     */
    Map<String, String> getLocalOptions();

    /**                       
     * Sets the map containing the local configuration settings,
     *
     * @param cfgParams the map containing the local configuration settings.
     */
    void setLocalOptions(final Map<String, String> cfgParams);

    /**
     * Returns the setting in the local configuration map for a specific option.
     * 
     * @param name the name of the option
     * @return the setting in the local configuration map for a specific option.
     */
    String getLocalOption(final @NotNull String name);

    /**
     * Returns a String, listing all local option keys and their corresponding values.
     * Note the returned String will is not based only on the local configuration map,
     * but also from all configuration maps in the parent chain.
     * A key must start a letter to be listed.
     *
     * @return a String, listing all option keys and their corresponding values of all configurations maps, starting
     * from this, in the parent chain.
     * @see de.zib.gndms.kit.config.ParameterTools#asString(java.util.Map, java.util.regex.Pattern)
     */
    @NotNull String localOptionsToString();

    /**
     * Parses {@code options} and sets them as the new configuration map.
     * See {@link de.zib.gndms.kit.config.ParameterTools} description about the syntax.
     * @param options a String containg the option keys and their corresponding values.
     * @throws ParameterTools.ParameterParseException if the String is not valid according to the syntax described above.
     */
    void parseLocalOptions(final @NotNull String options)
            throws ParameterTools.ParameterParseException;

    /**
     * Returns the map containing the local configuration settings plus the ones from the parent chain of this CommandAction.
     * 
     * @return the map containing the local configuration settings plus the ones from the parent chain of this CommandAction.
     */
    @NotNull Map<String, String> getAllOptions();

    /**
     * Returns a set containing all availabel option names.
     * This means all option names in the parent chain for a CommandAction, starting from this.
     *
     * @return a set containing all set options.
     */
    @NotNull Set<String> getAllOptionNames();

    /**
     * Returns a String, listing all option keys and their corresponding values.
     * Note the returned String will not only be based on the local configuration map,
     * but also on all configuration maps in the parent chain.
     * A key must start a letter to be listed.
     *
     * @return a String, listing all option keys and their corresponding values of all configurations map starting
     * from this, in the parent chain.
     * @see de.zib.gndms.kit.config.ParameterTools#asString(java.util.Map, java.util.regex.Pattern)
     */
    @NotNull String allOptionsToString();

    PrintWriter getPrintWriter();

    boolean hasPrintWriter();

    void setPrintWriter(final PrintWriter writer);

}
