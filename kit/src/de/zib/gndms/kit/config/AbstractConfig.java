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
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This abstract class provides a default implementation of the {@code ConfigProvider} interface.
 *
 * A concrete subclass must implement the method {@link OptionProvider#hasOption(String)}, defining if the
 * configuration has a value set for the specific option.
 * A Value may contain system environment variables which are denoted by <tt> %{[a-zA-Z_][a-zA-Z0-9_]*} </tt> and
 * will be replaced automatically with their current values when the value of an option is returned. 
 *
 * @see ConfigProvider
 * @author  try ste fan pla nti kow zib
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
        try {
            return getOption(name);
        }
        catch (MandatoryOptionMissingException e) {
            return def;
        }
    }

    /**
     * Returns the String set in the current configuration for a specific option.
     * Its value may use system environment variables, which will be replaced by their actual values.
     * A system environment variable must be denoted in the following syntax:
     * <tt> %{[a-zA-Z_][a-zA-Z0-9_]*} </tt>
     *
     * @param optionName the name of a specific option
     * @return the value set for a specific option. System enviroment variables are replaced with their values
     */
    public String getNonMandatoryOption(final String optionName) {
        final String optionValue = getConcreteNonMandatoryOption(optionName);
        if (optionValue == null)
            return optionValue;
        else {
            StringBuffer result = new StringBuffer(optionValue.length() << 2);
            Matcher matcher = SHELL_ENV_PAT.matcher(optionValue);
            while (matcher.find()) {
                final String envVarName = matcher.group(1);
                final String replacement = replaceVar(optionName, envVarName);
                matcher.appendReplacement(result, replacement == null ? "" : replacement);
            }
            matcher.appendTail(result);
            return result.toString();
        }
    }


    /**
     * Replaces a system enviroment variable name with its value.
     * 
     * @param optionName
     * @param envVarName the name of the system enviroment variable
     * @return the value of a specific system enviroment variable
     */
    @SuppressWarnings({ "MethodMayBeStatic" })
    protected String replaceVar(final String optionName, final String envVarName) {
        return escape(System.getenv(envVarName));
    }

    /**
     * @see ParameterTools#escape(String) 
     */
	private static String escape(final String s) {
		return ParameterTools.escape(s);
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


    /**
     * Parse a String in ISO8601-format to DateTime-Object
     *
     * @param optionParam a String in ISO8601-format 
     * @return a DateTime-Object retrieved from the String
     */
    public static @NotNull DateTime parseISO8601(final @NotNull String optionParam) {
        if (optionParam.length() == 0)
            throw new IllegalArgumentException("Empty ISO 8601 timestamp");
        return optionParam.charAt(0) == 'P' ?
                new DateTime(0L).plus(ISOPeriodFormat.standard().parsePeriod(optionParam))
                : ISODateTimeFormat.dateTimeParser().parseDateTime(optionParam);
    }


    public @NotNull File getFileOption(final @NotNull String name) throws MandatoryOptionMissingException {
        return new File(getOption(name));
    }


    public @NotNull File getFileOption(final @NotNull String name, final @NotNull File def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : new File(option);
    }


    public <X> Class<? extends X> getClassOption(
            final @NotNull Class<X> baseClass, @NotNull final String name)
            throws MandatoryOptionMissingException, ClassNotFoundException {
        
        return Class.forName(getOption(name)).asSubclass(baseClass);
    }


    public <X> Class<? extends X> getClassOption(
            final @NotNull Class<X> baseClass, @NotNull final String name,
            @NotNull final Class<? extends X> def) throws ClassNotFoundException {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Class.forName(option).asSubclass(baseClass);
    }


	@SuppressWarnings({ "HardcodedFileSeparator" })
	@NotNull
	public ConfigProvider getDynArrayOption(@NotNull final String name)
		  throws ParseException, MandatoryOptionMissingException {
		final String optStr = getOption(name);
		final List<String> entries = ParameterTools.parseStringArray(optStr);

		final Map<String, String> map = new HashMap<String, String>(entries.size());
		map.put("count", Integer.toString(entries.size()));
		int item = 0;
		for (String entry : entries) {
			map.put(Integer.toString(item), entry);
			item++;
		}
		return new MapConfig(map);
	}


	@NotNull
	public Iterator<String> dynArrayKeys() {
		final int count = dynArraySize();
		return new Iterator<String>() {
			int item = 0;

			public boolean hasNext() {
				return item < count;
			}


			public String next() {
				final String val = Integer.toString(item);
				item++;
				return val;
			}


			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}


	public int dynArraySize() {return getIntOption("count", 0);}
}
