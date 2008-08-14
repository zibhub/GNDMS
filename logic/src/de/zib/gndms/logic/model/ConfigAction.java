package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.CommandAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.*;


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
    public void initialize() {
        super.initialize();    // Overridden method
        if (!hasPrintWriter())
            throw new IllegalStateException("PrintWriter missing");
    }


    @Override
    public final R execute(final @NotNull EntityManager em) {
        final R retVal = execute(em, getPrintWriter());
        printWriter.write(retVal.toString());
        return retVal;
    }

    public abstract R execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer);

    @Override
    public void cleanUp() {
        printWriter.flush();
        if (closingWriterOnCleanUp)
            printWriter.close();
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
        cmdParams = Collections.unmodifiableMap(cmdParams);
    }


    @NotNull
    public final String localOptionsToString() {
        return CommandAction.ParameterTools.asString(getLocalOptions());
    }

    public final void parseLocalOptions(final @NotNull String cfgParams)
            throws ParameterTools.ParameterParseException {
        Map<String,String> cfgMap = new HashMap<String,String>(8);
        CommandAction.ParameterTools.parseParameters(cfgMap, cfgParams);
        setLocalOptions(cfgMap);
    }


    @NotNull
    public String getOption(final @NotNull String name, final @NotNull String def) {
        final String val = getOption(name);
        return val == null ? def : val;
    }

    public String getOption(final @NotNull String name) {
        String val = getLocalOption(name);
        if (val == null) {
            final CommandAction<?> nextCommandAction = nextParentOfType(CommandAction.class);
            return nextCommandAction == null ? null : nextCommandAction.getOption(name);
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
    public Map<String, String> getAllOptions() {
        Map<String, String> allOptions = new HashMap<String, String>(8);
        List<CommandAction> parents = getParentChain(CommandAction.class);
        for (CommandAction<?> action : parents) {
            final Map<String, String> options = action.getLocalOptions();
            if (options != null)
                allOptions.putAll(options);
        }
        return allOptions;
    }


    @NotNull
    public final String allOptionsToString() {
        return CommandAction.ParameterTools.asString(getLocalOptions());
    }


    public PrintWriter getPrintWriter() {
        if (printWriter == null) {
            final CommandAction<?> commandAction = nextParentOfType(CommandAction.class);
            if (commandAction != null)
                return commandAction.getPrintWriter();
        }
        return printWriter;
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
        final String option = getOption(name);
        return option == null ? def : Integer.parseInt(option);
    }

    public int getIntOption(String name) {
        final String option = getOption(name);
        return Integer.parseInt(option);
    }

    public long getLongOption(String name, long def) {
        final String option = getOption(name);
        return option == null ? def : Integer.parseInt(option);
    }

    public long getLongOption(String name) {
        final String option = getOption(name);
        return Integer.parseInt(option);
    }
}
