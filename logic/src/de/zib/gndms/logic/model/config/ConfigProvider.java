package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 11:17:49
 */
public interface ConfigProvider extends OptionProvider {
    int getIntOption(@NotNull String name) throws MandatoryOptionMissingException;

    int getIntOption(@NotNull String name, int def);


    long getLongOption(@NotNull String name) throws MandatoryOptionMissingException;

    long getLongOption(@NotNull String name, long def);



    @NotNull ImmutableScopedName getISNOption(@NotNull String name) throws MandatoryOptionMissingException;

    @NotNull ImmutableScopedName getISNOption(@NotNull String name, @NotNull ImmutableScopedName def);


    boolean isBooleanOptionSet(@NotNull String name) throws MandatoryOptionMissingException;

    boolean isBooleanOptionSet(@NotNull String name, boolean def);


    @NotNull <E extends Enum<E>> E getEnumOption(@NotNull Class<E> clazz,
                                        @NotNull String name, boolean toUpper)
                                                throws MandatoryOptionMissingException;

    @NotNull <E extends Enum<E>> E getEnumOption(
            @NotNull Class<E> clazz, @NotNull String name, boolean toUpper, @NotNull E def);

    
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull Calendar getISO8601Option(@NotNull String name, @NotNull Calendar def) throws ParseException;

    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull Calendar getISO8601Option(@NotNull String name)
            throws MandatoryOptionMissingException, ParseException;
}
