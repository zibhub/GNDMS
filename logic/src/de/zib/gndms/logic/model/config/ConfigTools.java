package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.lang.reflect.Field;
import java.io.PrintWriter;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 23.10.2008 Time: 16:37:27
*/
@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
class ConfigTools {
    private ConfigTools() {
    }

    /**
     * Returns a Map containing all fields and their corresponding description as an {@code ConfigOption} object
     * of a {@code clazz}'s instance.
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
     * Fills a map with the names of all fields and their corresponding description as an {@code ConfigOption} object.
     * If denoted in the {@code ConfigOption} object, the alternative field name will be taken
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
     * Prints the syntax description using a {@code PrintWriter}
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
     * Prints a Map containing the possible keys with their description to a {@code PrintWriter}
     * @param writer the {@code PrintWriter} the option reminder will printed to
     * @param mapParam a Map containing the possible keys with their description
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
