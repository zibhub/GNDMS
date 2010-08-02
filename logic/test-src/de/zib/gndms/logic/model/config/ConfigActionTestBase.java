package de.zib.gndms.logic.model.config;

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



import de.zib.gndms.logic.action.ActionTestTools;
import de.zib.gndms.model.test.ModelEntityTestBase;
import de.zib.gndms.kit.config.ParameterTools;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Optional;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 14:38:39
 */
public abstract class ConfigActionTestBase extends ModelEntityTestBase {

    public ConfigActionTestBase(final String dbPath, @Optional("c3grid") final String Name) {
        super(dbPath, Name);
    }

    protected void prepareConfigAction(
            final @NotNull ConfigAction<?> actionParam, final PrintWriter pwriterParam,
            final String options)
            throws ParameterTools.ParameterParseException {
        actionParam.setOwnEntityManager(createEntityManager());
        ActionTestTools.setupConfigAction(actionParam, pwriterParam);
        actionParam.parseLocalOptions(options);
    }


    protected final @NotNull EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
}
