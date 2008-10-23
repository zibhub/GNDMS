package de.zib.gndms.logic.action;

import de.zib.gndms.logic.model.config.ConfigProvider;
import de.zib.gndms.logic.model.config.ParameterTools;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


/**
 * Configuration actions take a string-string map parameter from some external source
 * and may write results to an optional print writer
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 10:36:15
 */
public interface CommandAction<R> extends Action<R>, ConfigProvider {
    Map<String, String> getLocalOptions();
    void setLocalOptions(final Map<String, String> cfgParams);

    String getLocalOption(final @NotNull String name);

    @NotNull String localOptionsToString();
    void parseLocalOptions(final @NotNull String options)
            throws ParameterTools.ParameterParseException;

    @NotNull Map<String, String> getAllOptions();

    @NotNull Set<String> getAllOptionNames();
    @NotNull String allOptionsToString();

    PrintWriter getPrintWriter();
    boolean hasPrintWriter();
    void setPrintWriter(final PrintWriter writer);

}
