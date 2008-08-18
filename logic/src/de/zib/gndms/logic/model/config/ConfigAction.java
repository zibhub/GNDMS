package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.CommandAction;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.AbstractEntityAction;
import de.zib.gndms.logic.util.ISO8601;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.*;
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
public abstract class ConfigAction<R> extends AbstractEntityAction<R> implements CommandAction<R> {
    private Map<String, String> cmdParams;
    private PrintWriter printWriter;
    private boolean writeResult;
    private boolean closingWriterOnCleanUp;

    @Override
    public final R execute(final @NotNull EntityManager em) {
        final R retVal = execute(em, getPrintWriter());
        if (isWriteResult() && hasPrintWriter())
            printWriter.write(retVal == null ? "(null)" : retVal.toString());
        return retVal;
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


    public String getLocalOption(final @NotNull String name) {
        if (cmdParams == null)
            return null;
        else
            return cmdParams.get(name);
    }


    public final Map<String, String> getLocalOptions() {
        return cmdParams;
    }


    public final void setLocalOptions(final Map<String, String> cfgParams) {
        cmdParams = Collections.unmodifiableMap(cfgParams);
    }


    @NotNull
    public final String localOptionsToString(boolean withNewlines) {
        return CommandAction.ParameterTools.asString(getLocalOptions(), withNewlines);
    }

    @NotNull
    public final String localOptionsToString() {
        return localOptionsToString(false);
    }

    public final void parseLocalOptions(final @NotNull String cfgParams)
            throws ParameterTools.ParameterParseException {
        Map<String,String> cfgMap = new HashMap<String,String>(8);
        CommandAction.ParameterTools.parseParameters(cfgMap, cfgParams);
        setLocalOptions(cfgMap);
    }


    @NotNull
    public final String getOption(final @NotNull String name, final @NotNull String def) {
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
        return getNonMandatoryOption(name) == null;
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

    @NotNull
    public Set<String> getAllOptionNames() {
        return getAllOptions().keySet();
    }


    @SuppressWarnings({ "RawUseOfParameterizedType" })
    @NotNull
    public final Map<String, String> getAllOptions() {
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


    @NotNull
    public final String allOptionsToString(boolean withNewLines) {
        return CommandAction.ParameterTools.asString(getAllOptions(), withNewLines);
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


    // A few helpers...

    public int getIntOption(String name, int def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Integer.parseInt(option);
    }

    public int getIntOption(String name) throws MandatoryOptionMissingException {
        final String option = getOption(name);
        return Integer.parseInt(option);
    }

    public long getLongOption(String name, long def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Integer.parseInt(option);
    }

    public long getLongOption(String name) throws MandatoryOptionMissingException  {
        final String option = getOption(name);
        return Integer.parseInt(option);
    }

    public boolean isBooleanOptionSet(String name, boolean def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : "true".equals(option.trim().toLowerCase());
    }

    public boolean isBooleanOptionSet(String name) throws MandatoryOptionMissingException  {
        final String option = getOption(name);
        return "true".equals(option.trim().toLowerCase());
    }

    public <E extends Enum<E>> E getEnumOption(Class<E> clazz, String name, @NotNull E def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Enum.valueOf(clazz, option);
    }

    public <E extends Enum<E>> E getLCEnumOption(Class<E> clazz, String name, @NotNull E def) {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : Enum.valueOf(clazz, option.toUpperCase());
    }

    public <E extends Enum<E>> E getEnumOption(Class<E> clazz, String name)
     throws MandatoryOptionMissingException 
    {
        final String option = getOption(name);
        return Enum.valueOf(clazz, option);
    }

    public <E extends Enum<E>> E getLCEnumOption(Class<E> clazz, String name)
         throws MandatoryOptionMissingException
    {
        final String option = getOption(name);
        return Enum.valueOf(clazz, option.toUpperCase());
    }

    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public Calendar getISO8601Option(String name, @NotNull Calendar def) throws ParseException {
        final String option = getNonMandatoryOption(name);
        return option == null ? def : ISO8601.parseISO8601DateStr(option);
    }

    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public @NotNull Calendar getISO8601Option(String name)
            throws MandatoryOptionMissingException, ParseException {
        final String option = getOption(name);
        return ISO8601.parseISO8601DateStr(option);
    }
}
