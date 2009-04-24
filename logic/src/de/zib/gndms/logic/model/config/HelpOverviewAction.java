package de.zib.gndms.logic.model.config;

import com.google.common.base.Function;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.Comparator;


/**
 * Prints a list of available config actions.
 *
 * <p>The list contains all actions of the set {@link #configActions} with their corresponding descriptions,
 * as denoted in their {@link ConfigActionHelp} annotation.
 *
 *
 * <p>A name mapper can be denoted, using {@code setNameMapper()}, mapping the canonical name (<tt>actionClass.getCanonicalName()</tt> )
 * of each action class contained in the set to a predefined String and will used when printing the list.
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


    /**
     * Sets a function which will be used to Map <tt>getCanonicalName()</tt> of each class in the set to another String,
     * when printing the list. 
     * @param nameMapperParam
     */
    public void setNameMapper(final Function<String, String> nameMapperParam) {
        nameMapper = nameMapperParam;
    }


    public @NotNull Set<Class<? extends ConfigAction<?>>> getConfigActions() {
        return configActions;
    }

    /**
     * Sets the list of available <tt>ConfigAction</tt>s.
     * @param configActionsParam list of available <tt>ConfigAction</tt>s.
     */
    public void setConfigActions(
            final @NotNull Set<Class<? extends ConfigAction<?>>> configActionsParam) {
        configActions = configActionsParam;
    }


    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        writer.println("# Available actions:");
        writer.println();
        // Sort by class name
        final Object[] caArray = configActions.toArray();
        Arrays.sort(caArray, new Comparator<Object>() {
            public int compare(final Object o1, final Object o2) {
                return ((Class)o1).getCanonicalName().compareTo(((Class)o2).getCanonicalName());
            }
        });
        // Iterate over result
        for (Object obj : caArray) {
            Class<? extends ConfigAction<?>> action = (Class<? extends ConfigAction<?>>) obj;
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
