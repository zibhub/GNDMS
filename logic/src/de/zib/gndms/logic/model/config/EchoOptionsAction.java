package de.zib.gndms.logic.model.config;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * Just returns all option parameters and prints them by virtue of ConfigAction.setWriteResult(true).
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:06:40
 */
@ConfigActionHelp(shortHelp="Echoes back all parameters as provided", longHelp="Echoes back all parameters as provided without interpreting them using the newline-separated, quoted option format.")
public final class EchoOptionsAction extends ConfigAction<String> {

    @Override
    public void initialize() {
        setWriteResult(true);
        super.initialize();    // Overridden method
    }


    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        return allOptionsToString(true);
    }
}
