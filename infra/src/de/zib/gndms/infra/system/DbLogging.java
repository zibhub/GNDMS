package de.zib.gndms.infra.system;

import java.io.File;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 20.08.2008, Time: 15:30:56
 */
public class DbLogging {

    public static void activate( String pathToLogFile  ) throws IOException {

        System.setProperty("derby.infolog.append", "true");
        System.setProperty("derby.language.logStatementText", "true");
        System.setProperty("derby.stream.error.logSeverityLevel", "20000");

		File dbLogFile = new File( pathToLogFile, "derby.log");
		if (!dbLogFile.exists())
                dbLogFile.createNewFile();
        System.setProperty("derby.stream.error.file",
				  dbLogFile.getCanonicalPath());
    }
}