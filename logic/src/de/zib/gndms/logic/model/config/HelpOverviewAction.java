package de.zib.gndms.logic.model.config;

import com.google.common.base.Function;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Set;


/**
 * Prints a list of available config actions
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 26.08.2008 Time: 10:55:17
 */
@ConfigActionHelp(shortHelp = "Prints a list of available config actions")
public class HelpOverviewAction extends ConfigAction<String> {
    private Set<Class<? extends ConfigAction<?>>> configActions;
    private Function<String, String> nameMapper;


    @Override
    public void initialize() {
        if (configActions == null)
            throw new IllegalStateException("No configActions configured");
        super.initialize();    // Overridden method
    }


    public Function<String, String> getNameMapper() {
        return nameMapper;
    }


    public void setNameMapper(final Function<String, String> nameMapperParam) {
        nameMapper = nameMapperParam;
    }


    public @NotNull Set<Class<? extends ConfigAction<?>>> getConfigActions() {
        return configActions;
    }


    public void setConfigActions(
            final @NotNull Set<Class<? extends ConfigAction<?>>> configActionsParam) {
        configActions = configActionsParam;
    }


    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        writer.println("# Available actions:");
        writer.println();
        for (Class<? extends ConfigAction<?>> action : configActions) {
            final String name = action.getCanonicalName();
            writer.print(nameMapper == null ? name : nameMapper.apply(name));
            ConfigActionHelp help = action.getAnnotation(ConfigActionHelp.class);
            if (help == null)
                writer.println();
            else {
                writer.print(" - ");
                writer.println(help.shortHelp());
            }
        }
        writer.println();
        writer.println("# All actions prints help information when called with 'help'");
        writer.println();

        return "";
    }
}
