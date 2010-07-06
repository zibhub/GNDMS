package de.zib.gndms.infra.action;

import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.security.SecurityManager;

import javax.persistence.EntityManager;
import java.io.*;

/**
 * Action for reading log files
 */
public class ReadContainerLogAction extends SystemAction implements PublicAccessible {

    private File getLogFile() {
        return new File(getSystem().getContainerHome(), "var" +  File.pathSeparatorChar + "container.log");
    }

    int skipLines;
    int numLines;

    @Override
    public void initialize() {
        super.initialize();
        skipLines = Math.abs(getIntOption("skipLines", 0));
        numLines  = getIntOption("numLines", 0);
    }

    @Override
    public Object execute(@NotNull EntityManager em, @NotNull PrintWriter writer) {
        final File logFile = getLogFile();

        final String dn = SecurityManager.getManager().getCaller();
        if (! getSystem().isGridAdmin(dn))
            throw new SecurityException("Authenticated user not allowed to access log files. DN was '" + dn + '\'');
        
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
        return null;
    }

    @SuppressWarnings({"NestedAssignment"})
    private int streamLog(PrintWriter writer, File logFile, int skipLinesParam, int numLinesParam) {
        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(logFile.getName()));
            try {
                final int maxLines = skipLinesParam + numLinesParam;
                int count = 0;
                String line;

                for (; (numLinesParam == 0 || count < maxLines) && (line = rd.readLine()) != null; count ++)
                    if (count >= skipLines)
                        writer.println(line);

                return count;
            }
            finally { rd.close(); }
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
