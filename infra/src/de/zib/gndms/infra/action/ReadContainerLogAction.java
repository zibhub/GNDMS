package de.zib.gndms.infra.action;

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

    @ConfigOption(descr = "Number of lines to be skipped")
    public int skip;

    @ConfigOption(descr = "Number of lines to return after skipping (n==0: all lines, n<0: last n lines from tail after discarding skip last lines")
    public int num;


    @ConfigOption(descr = "If set, remove all lines that contain this substring from the output")
    public String filter;

    @ConfigOption(descr = "Only return lines that contain this substring")
    public String grep;

    @ConfigOption(descr = "If set, do not print line numbers")
    public boolean raw;

    @Override
    public void initialize() {
        super.initialize();
        skip = Math.abs(getIntOption("skip", 0));
        num = getIntOption("num", 0);
        filter = getOption("filter", "");
        grep = getOption("grep", "");
        raw = isBooleanOptionSet("raw", false);
    }

    @Override
    public ConfigActionResult execute(@NotNull EntityManager em, @NotNull PrintWriter writer) {
        final File logFile = getLogFile();

        final String dn = SecurityManager.getManager().getCaller();
        if (! getSystem().isGridAdmin("ReadContainerLogAction", dn))
            throw new SecurityException("Authenticated user not allowed to access log files. DN was '" + dn + '\'');

        final int maxValue;
        if (num < 0) {
            maxValue  = streamLog(writer, logFile, Integer.MAX_VALUE, 0, false);
            num = Math.abs(num);
            skip = Math.min(0, maxValue - skip - num);
        }
        else
            maxValue = Integer.MAX_VALUE - skip - 1;

        if (num == 0)
            num = maxValue;

        streamLog(writer, logFile, skip, num, true);
        return ok();
    }

    @SuppressWarnings({"NestedAssignment"})
    private int streamLog(PrintWriter writer, File logFile, int skipLinesParam, int numLinesParam, boolean print) {
//        writer.print('\'' + filter + '\'');
//        writer.print('\'' + grep + '\'');
//        writer.print(skip);
//        writer.println(num);
        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(logFile));
            try {
                final int maxLines = skipLinesParam + numLinesParam;
                int count = 0;
                String line;

                for (; (numLinesParam == 0 || count <= maxLines) && (line = rd.readLine()) != null; count ++) {
//                    writer.println("");
//                    writer.print(count);
                    if (!print)
                        continue;
//                    writer.print("print");
                    if (count < skipLinesParam)
                        continue;
//                    writer.print("count");
                    if (filter.length() > 0 && line.contains(filter))
                        continue;
//                    writer.print("filter");
                    if (grep.length() > 0 && !line.contains(grep))
                        continue;
//                    writer.print("grep");
                    if (!raw)
                        writer.print(Integer.toString(count) + ':' + ' ');
                    writer.println(line);
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
