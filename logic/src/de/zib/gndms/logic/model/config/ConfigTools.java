package de.zib.gndms.logic.model.config;

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



import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.lang.reflect.Field;
import java.io.PrintWriter;


/**
 * This Class provides methods to print help regarding the proper configuration of an ConfigAction instance.
 *
 * <tt>PrintOptionReminder()</tt> prints a details description about the syntax of the configuration.
 *
 * <tt>PrintOptionHelp()</tt> can be used with <tt>getParamMap()</tt> to print all possible configurations of a
 * specific ConfigAction class.
 * .
 *
 * @see  de.zib.gndms.logic.model.config.ConfigAction
 * @see de.zib.gndms.logic.model.config.ConfigOption
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 23.10.2008 Time: 16:37:27
 */
@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
class ConfigTools {
    private ConfigTools() {
    }

    /**
     * Returns a Map containing all annotated field names and their corresponding description for a specific class.
     * (See {@link #fillParamMapForClass(Class, java.util.Map)}).
     *
     * @param clazz the class object whose fields and their description should be put into a map
     * @return a Map containing all fields and their corresponding description as an {@code ConfigOption} object
     * of a {@code clazz}'s instance
     */
    public static Map<String, ConfigOption> getParamMap(
            final @NotNull Class<? extends ConfigAction<?>> clazz) {
        Map<String, ConfigOption> map = new HashMap<String, ConfigOption>(8);
        fillParamMapForClass(clazz, map);
        return map;
    }

    /**
     * Fills a map with the names of all annotated fields (<tt>ConfigOption</tt>) and their description,
     * for a given class class.
     * A field is stored in the map using its name as key and its corresponding {@link ConfigOption} Object as its value.
     * If an alternative name is given, the alternative field name will be taken as its key.
     *
     * @param clazz the class object whose fields and their description should be added to the map
     * @param mapParam the map to be filled with all field names and their corresponding
     */
    private static void fillParamMapForClass(
            final Class<?> clazz, final Map<String, ConfigOption> mapParam) {
        if (Object.class.equals(clazz))
            return;
        else
            fillParamMapForClass(clazz.getSuperclass(), mapParam);
        for (Field field : clazz.getDeclaredFields()) {
            ConfigOption option = field.getAnnotation(ConfigOption.class);
            if (option != null) {
                //TODO fix possibly key collision
                String altName = option.altName();
                if (altName != null && altName.length() > 0) {
                    if (mapParam.containsKey(altName))
                        throw new IllegalStateException("Duplicate parameter name detected");
                    else
                        mapParam.put(altName, option);
                }
                else
                    mapParam.put(field.getName(), option);
            }
        }
    }

    /**
     * Prints the syntax description used for the configuration of a ConfigAction.
     * 
     * @param printWriter the {@code PrintWriter} the option reminder will printed to
     */
    @SuppressWarnings({ "HardcodedFileSeparator" })
    protected static void printOptionReminder(PrintWriter printWriter) {
        printWriter.println("Option format reminder: opt1: value1; ...; optN: valueN");
        printWriter.println(" * Option names must match the regexp: '"
                + ConfigAction.OPTION_NAME_PATTERN.toString() + "'.");
        printWriter.println(" * Values may be enclosed in single or double quotes.");
        printWriter.println("   * Without quotes, values get whitespace-trimmed.");
        printWriter.println("   * Inside quotes, '\\' may be used for escaping.");
	    printWriter.println(" * Array/list literal syntax is '[a, b, c]'");
	    printWriter.println(" * Leading whitespace characters are removed from array element values unless they are escaped");
        printWriter.println(
                    " * 'foo' or '+foo' denotes a true, '!foo' or '-foo' a false boolean option named 'foo'.");
        printWriter.println(
                    " * Timestamps are expected to be in ISO8601-format and based on UTC.");
    }

    /**
     * Writes all possible configurations for specific ConfigAction class along with their description to an PrintWriter.
     * The map must be created using {@link #getParamMap(Class)}.
     *
     * @param writer the {@code PrintWriter} the help will be printed to
     * @param mapParam a map corresponding to a specific ConfigAction class
     */
    protected static void printOptionHelp(final @NotNull PrintWriter writer,
                                          final Map<String, ConfigOption> mapParam) {
        Object[] entries = mapParam.entrySet().toArray();
        Arrays.sort(entries, new Comparator<Object>() {
            @SuppressWarnings({ "unchecked" })
            public int compare(final Object o1, final Object o2) {
                return ((Map.Entry<String, ConfigOption>)o1).getKey().compareTo(((Map.Entry<String, ConfigOption>)o2).getKey());
            }
        });
        for (Object obj : entries) {
            Map.Entry<String, ConfigOption> entry = (Map.Entry<String, ConfigOption>) obj;

            writer.print(" * ");
            final String key = entry.getKey();
            writer.print(key);
            writer.print(": ");
            final ConfigOption option = entry.getValue();
            final String descr = option.descr();
            writer.println(descr == null || descr.length() == 0 ? "The " + key : descr);
        }
    }

}
