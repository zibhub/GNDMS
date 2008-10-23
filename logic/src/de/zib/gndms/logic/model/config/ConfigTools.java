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


    public static Map<String, ConfigOption> getParamMap(
            final @NotNull Class<? extends ConfigAction<?>> clazz) {
        Map<String, ConfigOption> map = new HashMap<String, ConfigOption>(8);
        fillParamMapForClass(clazz, map);
        return map;
    }


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

    @SuppressWarnings({ "HardcodedFileSeparator" })
    protected static void printOptionReminder(PrintWriter printWriter) {
        printWriter.println("Option format reminder: opt1: value1; ...; optN: valueN");
        printWriter.println(" * Option names must match the regexp: '"
                + ConfigAction.OPTION_NAME_PATTERN.toString() + "'.");
        printWriter.println(" * Values may be enclosed in single or double quotes.");
        printWriter.println("   * Without quotes, values get whitespace-trimmed.");
        printWriter.println("   * Inside quotes, '\\' may be used for escaping.");
        printWriter.println(
                    " * 'foo' or '+foo' denotes a true, '!foo' or '-foo' a false boolean option named 'foo'.");
        printWriter.println(
                    " * Timestamps are expected to be in ISO8601-format and based on UTC.");
    }


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
