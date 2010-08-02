package de.zib.gndms.logic.action;

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



import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.util.SimpleModelUUIDGen;

import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:22:56
 */
public final class ActionTestTools {
    private ActionTestTools() {}


    @SuppressWarnings({ "FeatureEnvy" })
    public static void setupConfigAction(ConfigAction<?> cfgA, PrintWriter pwriter) {
        cfgA.setClosingWriterOnCleanUp(false);
        cfgA.setWriteResult(true);
        cfgA.setPrintWriter(pwriter);
        cfgA.setUUIDGen(SimpleModelUUIDGen.getInstance());
    }
}
