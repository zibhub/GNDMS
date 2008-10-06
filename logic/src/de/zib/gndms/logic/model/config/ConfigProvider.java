package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.text.ParseException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 11:17:49
 */
public interface ConfigProvider extends OptionProvider {
    int getIntOption(String name) throws MandatoryOptionMissingException;

    int getIntOption(String name, int def);


    long getLongOption(String name) throws MandatoryOptionMissingException;

    long getLongOption(String name, long def);


    boolean isBooleanOptionSet(String name) throws MandatoryOptionMissingException;

    boolean isBooleanOptionSet(String name, boolean def);


    @NotNull <E extends Enum<E>> E getEnumOption(@NotNull Class<E> clazz,
                                        @NotNull String name, boolean toUpper)
                                                throws MandatoryOptionMissingException;

    @NotNull <E extends Enum<E>> E getEnumOption(
            @NotNull Class<E> clazz, @NotNull String name, boolean toUpper, @NotNull E def);

    
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull Calendar getISO8601Option(String name, @NotNull Calendar def) throws ParseException;

    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull Calendar getISO8601Option(String name)
            throws MandatoryOptionMissingException, ParseException;
}
