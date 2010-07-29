package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.security.SecurityManager;

import javax.persistence.EntityManager;
import java.io.*;

/**
 * Action for reading log files
 */
@ConfigActionHelp(shortHelp = "Read the container log", longHelp = "Read the container log -- if you're a gridAdmin")
public class ReadContainerLogAction extends SystemAction<ConfigActionResult> implements PublicAccessible {

    private File getLogFile() {
        return new File(getSystem().getContainerHome().getAbsolutePath() + File.separatorChar + "var" +  File.separatorChar + "container.log");
    }

    @ConfigOption(descr = "Lines to skip")
    int skipLines;

    @ConfigOption(descr = "Number of lines to return after skipping (n==0: all lines, n<0: last n lines from tail after discarding skipLines last lines")
    int numLines;


    @ConfigOption(descr = "If set, remove all lines that contain this substring from output")
    String skip;

    @ConfigOption(descr = "Only return lines that contain this substring")
    String grep;

    @Override
    public void initialize() {
        super.initialize();
        skipLines = Math.abs(getIntOption("skipLines", 0));
        numLines  = getIntOption("numLines", 0);
    }

    @Override
    public ConfigActionResult execute(@NotNull EntityManager em, @NotNull PrintWriter writer) {
        final File logFile = getLogFile();

        final String dn = SecurityManager.getManager().getCaller();
        if (! getSystem().isGridAdmin("ReadContainerLogAction", dn))
            throw new SecurityException("Authenticated user not allowed to access log files. DN was '" + dn + '\'');

        if (skip == null) skip = "";
        if (grep == null) grep = "";

        if (numLines == 0) {
            streamLog(writer, logFile, 0, Integer.MAX_VALUE);
            return null;
        }

        if (numLines > 0) {
            streamLog(writer, logFile, skipLines, numLines);
            return null;
        }

        // numLines < 0
        int count = streamLog(writer, logFile, Integer.MAX_VALUE, 0);
        streamLog(writer, logFile, count + numLines + skipLines, Math.abs(numLines) - skipLines);
        return ok();
    }

    @SuppressWarnings({"NestedAssignment"})
    private int streamLog(PrintWriter writer, File logFile, int skipLinesParam, int numLinesParam) {
        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(logFile));
            try {
                final int maxLines = skipLinesParam + numLinesParam;
                int count = 0;
                String line;

                for (; (numLinesParam == 0 || count < maxLines) && (line = rd.readLine()) != null; count ++)
                    if (count >= skipLines) {
                        if ((skip.length() == 0 || !line.contains(skip)) &&
                            ((grep.length() == 0) || line.contains(grep)))
                            writer.println(Integer.toString(count) + ':' + ' ' + line);
                    }

                return count;
            }
            finally { rd.close(); }
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
