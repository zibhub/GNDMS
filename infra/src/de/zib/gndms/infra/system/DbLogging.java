package de.zib.gndms.infra.system;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
