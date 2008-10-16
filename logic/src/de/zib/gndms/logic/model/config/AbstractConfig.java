package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 10:49:31
 */
public abstract class AbstractConfig implements ConfigProvider {
    private static final Pattern SHELL_ENV_PAT = Pattern.compile("%\\{([a-zA-Z_][a-zA-Z0-9_]*)\\}");


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


    public String getNonMandatoryOption(final String nameParam) {
        final String optionValue = getConcreteNonMandatoryOption(nameParam);
        if (optionValue == null)
            return optionValue;
        else {
            StringBuffer result = new StringBuffer(optionValue.length() << 2);
            Matcher matcher = SHELL_ENV_PAT.matcher(optionValue);
            while (matcher.find()) {
                final String varName = matcher.group(1);
                matcher.appendReplacement(result, replaceVar(nameParam, varName));
            }
            matcher.appendTail(result);
            return result.toString();
        }
    }


    @SuppressWarnings({ "MethodMayBeStatic" })
    protected String replaceVar(final String optionName, final String varName) {
        return System.getenv(optionName);
    }


    public abstract String getConcreteNonMandatoryOption(final String nameParam);


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
    public DateTime getISO8601Option(@NotNull String name, @NotNull DateTime def) throws ParseException {
        final String option = getNonMandatoryOption(name).trim();
        return option == null ? def : parseISO8601(option).toDateTimeISO();
    }


    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull DateTime getISO8601Option(@NotNull String name)
            throws MandatoryOptionMissingException, ParseException {
        final String option = getOption(name).trim();
        return parseISO8601(option).toDateTimeISO();
    }


    public static @NotNull DateTime parseISO8601(final @NotNull String optionParam) {
        if (optionParam.length() == 0)
            throw new IllegalArgumentException("Empty ISO 8601 timestamp");
        return optionParam.charAt(0) == 'P' ?
                new DateTime(0L).plus(ISOPeriodFormat.standard().parsePeriod(optionParam))
                : ISODateTimeFormat.dateTimeParser().parseDateTime(optionParam);
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
