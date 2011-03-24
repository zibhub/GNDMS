package de.zib.gndms.gritserv.typecon.util;

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



import org.jetbrains.annotations.NotNull;
import org.apache.axis.types.Token;
import org.apache.axis.types.NormalizedString;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.kit.util.WidAux;
import types.ContextT;
import types.ContextTEntry;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 22.10.2008 Time: 14:06:57
 */
public class ContextTAux {
    
    private ContextTAux() {}


    public static String computeWorkflowId(
            final @NotNull ModelUUIDGen gen, final ContextT contextParam)
    {
        if (contextParam == null) {
            return gen.nextUUID();
        }
        else {
            ContextTEntry widEntry;
            ContextTEntry[] newEntries;

            ContextTEntry[] entries = contextParam.getEntry();
            if (entries == null) {
                newEntries = new ContextTEntry[1];
                widEntry = new ContextTEntry();
                newEntries[0] = widEntry;
            }
            else {
                for (ContextTEntry entry : entries)
                    if ("Workflow.Id".equals(entry.getKey().toString()))
                        return entry.get_value().toString();
                newEntries = new ContextTEntry[entries.length + 1];
                System.arraycopy(entries, 0, newEntries, 0, entries.length);
                widEntry = new ContextTEntry();
                newEntries[entries.length] = widEntry;
            }
            widEntry.setKey(new Token("Workflow.Id"));
            final String id = gen.nextUUID();
            widEntry.set_value(new NormalizedString(id));
            contextParam.setEntry(newEntries);
            return id;
        }
    }


    public static void initWid(final ModelUUIDGen gen, final ContextT contextParam) {
        String id = computeWorkflowId(gen, contextParam);
        WidAux.initWid( id );
    }
}
