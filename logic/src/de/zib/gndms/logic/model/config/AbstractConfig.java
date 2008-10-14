package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Calendar;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 10:49:31
 */
public abstract class AbstractConfig implements ConfigProvider {

    public @NotNull String getOption(final String nameParam)
            throws MandatoryOptionMissingException {
        if (hasOption(nameParam))
            return getNonMandatoryOption(nameParam);
        else
            throw new MandatoryOptionMissingException(nameParam);
    }

    public @NotNull String getOption(final @NotNull String name, final @NotNull String def) {
        final String val;
        try {
            return getOption(name);
        }
        catch (MandatoryOptionMissingException e) {
            return def;
        }
    }


    public int getIntOption(@NotNull String name, int def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Integer.parseInt(option);
    }


    public int getIntOption(@NotNull String name) throws MandatoryOptionMissingException {
        final String option = getOption(name);
        return Integer.parseInt(option);
    }


    public long getLongOption(@NotNull String name, long def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Long.parseLong(option);
    }


    public long getLongOption(@NotNull String name) throws MandatoryOptionMissingException  {
        final String option = getOption(name);
        return Long.parseLong(option);
    }


    public boolean isBooleanOptionSet(@NotNull String name, boolean def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : "true".equals(option.trim().toLowerCase());
    }


    public boolean isBooleanOptionSet(@NotNull String name) throws MandatoryOptionMissingException  {
        final String option = getOption(name);
        return "true".equals(option.trim().toLowerCase());
    }


    @NotNull
    public <E extends Enum<E>> E getEnumOption(final @NotNull Class<E> clazz,
                                               final @NotNull String name, boolean toUpper,
                                               final @NotNull E def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Enum.valueOf(clazz, toUpper ? option.toUpperCase() : option);
    }


    @NotNull
    public <E extends Enum<E>> E getEnumOption(final @NotNull Class<E> clazz,
                                               final @NotNull String name, boolean toUpper)
     throws MandatoryOptionMissingException
    {
        final String option = getOption(name);
        return Enum.valueOf(clazz, toUpper ? option.toUpperCase() : option);
    }


    @NotNull
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public Calendar getISO8601Option(String name, @NotNull Calendar def) throws ParseException {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : new DateTime(option).toDateTimeISO().toGregorianCalendar();
    }


    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull Calendar getISO8601Option(String name)
            throws MandatoryOptionMissingException, ParseException {
        final String option = getOption(name);
        return new DateTime(option).toDateTimeISO().toGregorianCalendar();
    }


    @NotNull
    public ImmutableScopedName getISNOption(@NotNull final String name)
            throws MandatoryOptionMissingException {
        return new ImmutableScopedName(getOption(name));
    }


    @NotNull
    public ImmutableScopedName getISNOption(
            @NotNull final String name, @NotNull final ImmutableScopedName def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : new ImmutableScopedName(name);
    }
}
