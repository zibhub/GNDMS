package de.zib.gndms.logic.action;

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
 * set in the configuration for an CommandAction object, global options are the local ones plus all options from
 * set in the parent chain of the instance, being a CommandAction.
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
     * See the {@link de.zib.gndms.kit.config.ParameterTools description} about the syntax.
     * @param options a String containg the option keys and their corresponding values.
     * @throws ParameterTools.ParameterParseException if the String is not valid according to the syntax described above.
     */
    void parseLocalOptions(final @NotNull String options)
            throws ParameterTools.ParameterParseException;

    /**
     * Returns the map containing the local configuration settings plus the ones from the parent chain.
     * 
     * @return the map containing the local configuration settings plus the ones from the parent chain.
     */
    @NotNull Map<String, String> getAllOptions();

    /**
     * Returns a set containing all local and global options.
     * Global means all all CommandActions in the parent chain, starting from this.
     *
     * @return a set containing all local and global options.
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
