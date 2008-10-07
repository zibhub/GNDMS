package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.CommandAction;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.action.SkipActionInitializationException;
import de.zib.gndms.logic.model.AbstractEntityAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.text.ParseException;


/**
 * Default implementation of command actions for database manipulations.
 *
 * Takes care of command parsing and handles the associated print writer.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:53:21
 */
@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
public abstract class ConfigAction<R> extends AbstractEntityAction<R>
        implements CommandAction<R> {

    public static final Pattern OPTION_NAME_PATTERN = Pattern.compile("[a-z][a-zA-Z0-9-_]*");

    private Map<String, String> cmdParams;
    private PrintWriter printWriter;
    private boolean writeResult;
    private boolean closingWriterOnCleanUp;

    @SuppressWarnings({ "ThisEscapedInObjectConstruction" })
    private final ConfigProvider config = new DelegatingConfig(this);


    @Override
    public void initialize() {
        if (hasOption("help")) {
            printWriter.println(getClass().getCanonicalName());
            printWriter.println();
            printWriter.println(getShortHelp());
            printWriter.println();
            printWriter.println("Options: ");
            ConfigTools.printOptionHelp(printWriter, getParamMap());
            printWriter.println();
            printWriter.println(getLongHelp());
            printWriter.println();
            ConfigTools.printOptionReminder(printWriter);
            printWriter.println();
            throw new SkipActionInitializationException();
        }
        else
            super.initialize();    // Overridden method
    }


    @Override
    public final R execute(final @NotNull EntityManager em) {
        final R retVal = execute(em, getPrintWriter());
        if (isWriteResult() && hasPrintWriter())
            getPrintWriter().write(retVal == null ? "(null)" : retVal.toString());
        return retVal;
    }


    @SuppressWarnings({ "RawUseOfParameterizedType" })
    public String getShortHelp() {
        final Class<? extends ConfigAction> clazz = getClass();
        final ConfigActionHelp actionHelp = clazz.getAnnotation(ConfigActionHelp.class);
        String val = actionHelp.shortHelp();
        if (val == null || val.length() == 0)
            return "Short description missing";
        else
            return val;
    }


    @SuppressWarnings({ "RawUseOfParameterizedType" })
    public String getLongHelp() {
        final Class<? extends ConfigAction> clazz = getClass();
        final ConfigActionHelp actionHelp = clazz.getAnnotation(ConfigActionHelp.class);
        String val = actionHelp.longHelp();
        if (val == null || val.length() == 0)
            return "Long description missing";
        else
            return val;
    }

    @SuppressWarnings({ "unchecked" })
    public Map<String, ConfigOption> getParamMap() {
        return ConfigTools.getParamMap((Class<? extends ConfigAction<?>>) getClass());
    }

    public abstract R execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer);


    @Override
    public void cleanUp() {
        if (printWriter != null) {
            if (closingWriterOnCleanUp) {
                printWriter.flush();
                printWriter.close();                
            }
        }
        super.cleanUp();    // Overridden method
    }


    public static boolean isValidOptionName(final @NotNull String optionName) {
        return OPTION_NAME_PATTERN.matcher(optionName).matches();
    }


    public String getLocalOption(final @NotNull String name) {
        if (cmdParams == null)
            return null;
        else
            return cmdParams.get(name);
    }


    public @NotNull String getLocalOption(final @NotNull String name, final @NotNull String def) {
        if (cmdParams == null)
            return def;
        else {
            final String val = cmdParams.get(name);
            return val == null ? def : val;
        }
    }


    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
    public final Map<String, String> getLocalOptions() {
        return cmdParams;
    }


    public final void setLocalOptions(final Map<String, String> cfgParams) {
        cmdParams = Collections.unmodifiableMap(cfgParams);
    }


    public final @NotNull String localOptionsToString(boolean withNewlines) {
        return CommandAction.ParameterTools.asString(getLocalOptions(), OPTION_NAME_PATTERN,
                                                     withNewlines);
    }


    public final @NotNull String localOptionsToString() {
        return localOptionsToString(false);
    }


    public final void parseLocalOptions(final @NotNull String cfgParams)
            throws ParameterTools.ParameterParseException {
        Map<String,String> cfgMap = new HashMap<String,String>(8);
        CommandAction.ParameterTools.parseParameters(cfgMap, cfgParams, OPTION_NAME_PATTERN);
        setLocalOptions(cfgMap);
    }


    public final @NotNull String getOption(final @NotNull String name, final @NotNull String def) {
        final String val;
        try {
            return getOption(name);
        }
        catch (MandatoryOptionMissingException e) {
            return def;
        }
    }


    public @NotNull String getOption(final @NotNull String name)
            throws MandatoryOptionMissingException {
        String retVal = getNonMandatoryOption(name);
        if (retVal == null)
            throw new MandatoryOptionMissingException("Missing option '" + name + '\'');
        return retVal;
    }


    public final boolean hasOption(final @NotNull String name)
    {
        return getNonMandatoryOption(name) != null;
    }


    public String getNonMandatoryOption(final @NotNull String name) {
        String val = getLocalOption(name);
        if (val == null) {
            final CommandAction<?> nextCommandAction = nextParentOfType(CommandAction.class);
            if (nextCommandAction == null)
                return null;
            else
                try {
                    return nextCommandAction.getOption(name);
                }
                catch (MandatoryOptionMissingException e) {
                    return null;
                }
        }
        else
            return val;
    }


    public @NotNull Set<String> getAllOptionNames() {
        return getAllOptions().keySet();
    }


    @SuppressWarnings({ "RawUseOfParameterizedType" })
    public final @NotNull Map<String, String> getAllOptions() {
        Map<String, String> allOptions = new HashMap<String, String>(8);
        List<CommandAction> parents = getParentChain(CommandAction.class);
        for (CommandAction<?> action : parents) {
            final Map<String, String> options = action.getLocalOptions();
            if (options != null)
                allOptions.putAll(options);
        }
        allOptions.putAll(getLocalOptions());
        return allOptions;
    }


    public final @NotNull String allOptionsToString(boolean withNewLines)
    {
        return CommandAction.ParameterTools.asString(getAllOptions(), OPTION_NAME_PATTERN,
                                                     withNewLines);
    }


    @NotNull
    public final String allOptionsToString() {
        return allOptionsToString(false);
    }

    
    public PrintWriter getPrintWriter() {
        if (printWriter == null)
            return getParentPrintWriter();
        return printWriter;
    }


    private PrintWriter getParentPrintWriter() {
        final CommandAction<?> commandAction = nextParentOfType(CommandAction.class);
        return commandAction == null ? null : commandAction.getPrintWriter();
    }


    public boolean hasPrintWriter() {
        return getPrintWriter() != null;
    }

    public void setPrintWriter(final PrintWriter writerParam) {
        printWriter = writerParam;
    }


    public boolean isWriteResult() {
        return writeResult;
    }


    public void setWriteResult(final boolean writeResultParam) {
        writeResult = writeResultParam;
    }


    public boolean isClosingWriterOnCleanUp() {
        return closingWriterOnCleanUp;
    }


    public void setClosingWriterOnCleanUp(final boolean closingWriterOnCleanUpParam) {
        closingWriterOnCleanUp = closingWriterOnCleanUpParam;
    }



    
    public int getIntOption(final String name) throws MandatoryOptionMissingException {
        return config.getIntOption(name);
    }


    public int getIntOption(final String name, final int def) {
        return config.getIntOption(name, def);
    }




    public long getLongOption(final String name) throws MandatoryOptionMissingException {
        return config.getLongOption(name);
    }


    public long getLongOption(final String name, final long def) {
        return config.getLongOption(name, def);
    }




    public boolean isBooleanOptionSet(final String name) throws MandatoryOptionMissingException {
        return config.isBooleanOptionSet(name);
    }


    public boolean isBooleanOptionSet(final String name, final boolean def) {
        return config.isBooleanOptionSet(name, def);
    }




    public @NotNull <E extends Enum<E>> E getEnumOption(@NotNull final Class<E> clazz,
                                                        @NotNull final String name,
                                                        final boolean toUpper)
            throws MandatoryOptionMissingException {
        return config.getEnumOption(clazz, name, toUpper);
    }


    @NotNull
    public <E extends Enum<E>> E getEnumOption(
            @NotNull final Class<E> clazz, @NotNull final String name, final boolean toUpper,
            @NotNull final E def) {return config.getEnumOption(clazz, name, toUpper, def);}




    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull Calendar getISO8601Option(final String name)
            throws MandatoryOptionMissingException, ParseException {
        return config.getISO8601Option(name);
    }


    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull Calendar getISO8601Option(final String name, @NotNull final Calendar def)
            throws ParseException {
        return config.getISO8601Option(name, def);
    }




    public static class ConfigTools {
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
                    + OPTION_NAME_PATTERN.toString() + "'.");
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
                public int compare(final Object o1, final Object o2) {
                    return
                }
            });
            for (Map.Entry<String, ConfigOption> entry : mapParam.entrySet()) {
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
}
