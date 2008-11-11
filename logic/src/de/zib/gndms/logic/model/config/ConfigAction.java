package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.CommandAction;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.action.SkipActionInitializationException;
import de.zib.gndms.logic.model.AbstractEntityAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;


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
@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass", "ClassWithTooManyMethods" })
public abstract class ConfigAction<R> extends AbstractEntityAction<R>
        implements CommandAction<R> {

    public static final Pattern OPTION_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9-_]*");

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
        return ParameterTools.asString(getLocalOptions(), OPTION_NAME_PATTERN,
                                                     withNewlines);
    }


    public final @NotNull String localOptionsToString() {
        return localOptionsToString(false);
    }


    public final void parseLocalOptions(final @NotNull String cfgParams)
            throws ParameterTools.ParameterParseException {
        Map<String,String> cfgMap = new HashMap<String,String>(8);
        ParameterTools.parseParameters(cfgMap, cfgParams, OPTION_NAME_PATTERN);
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
        return ParameterTools.asString(getAllOptions(), OPTION_NAME_PATTERN,
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



    
    public int getIntOption(@NotNull final String name) throws MandatoryOptionMissingException {
        return config.getIntOption(name);
    }


    public int getIntOption(@NotNull final String name, final int def) {
        return config.getIntOption(name, def);
    }




    public long getLongOption(@NotNull final String name) throws MandatoryOptionMissingException {
        return config.getLongOption(name);
    }


    public long getLongOption(@NotNull final String name, final long def) {
        return config.getLongOption(name, def);
    }




    public boolean isBooleanOptionSet(@NotNull final String name) throws MandatoryOptionMissingException {
        return config.isBooleanOptionSet(name);
    }


    public boolean isBooleanOptionSet(@NotNull final String name, final boolean def) {
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
    public @NotNull
    DateTime getISO8601Option(@NotNull final String name)
            throws MandatoryOptionMissingException, ParseException {
        return config.getISO8601Option(name);
    }


    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull
    DateTime getISO8601Option(@NotNull final String name, @NotNull final DateTime def)
            throws ParseException {
        return config.getISO8601Option(name, def);
    }


    public @NotNull ImmutableScopedName getISNOption(@NotNull final String name, @NotNull final ImmutableScopedName def) {
        return config.getISNOption(name, def);
    }


    public @NotNull ImmutableScopedName getISNOption(@NotNull final String name)
            throws MandatoryOptionMissingException {return config.getISNOption(name);}


    @NotNull
    public File getFileOption(@NotNull final String name, @NotNull final File def) {
        return config.getFileOption(name, def);
    }


    @NotNull
    public File getFileOption(@NotNull final String name) throws MandatoryOptionMissingException {
        return config.getFileOption(name);
    }


    public <X> Class<? extends X> getClassOption(
            final @NotNull Class<X> baseClass, @NotNull final String name)
            throws MandatoryOptionMissingException, ClassNotFoundException {
        return config.getClassOption(baseClass, name);
    }


    public <X> Class<? extends X> getClassOption(
            final @NotNull Class<X> baseClass, @NotNull final String name,
            @NotNull final Class<? extends X> def) throws ClassNotFoundException {
        return config.getClassOption(baseClass, name, def);
    }


	@NotNull
	public ConfigProvider getDynArrayOption(@NotNull final String name)
		  throws ParseException, MandatoryOptionMissingException {
		return config.getDynArrayOption(name);
	}


	public int dynArraySize() {return config.dynArraySize();}


	@NotNull
	public Iterator<String> dynArrayKeys() {return config.dynArrayKeys();}


	public Iterator<String> iterator() {
		return Collections.unmodifiableSet(getAllOptionNames()).iterator();
	}


	@SuppressWarnings({ "MethodMayBeStatic", "InstanceMethodNamingConvention" })
    protected OkResult ok() {
        return new OkResult();
    }

    @SuppressWarnings({ "MethodMayBeStatic", "InstanceMethodNamingConvention" })
    protected OkResult ok(final @NotNull String details) {
        return new OkResult(details);
    }

    @SuppressWarnings({ "MethodMayBeStatic" })
    protected FailedResult failed(final @NotNull String details) {
        return new FailedResult(details);
    }
}
