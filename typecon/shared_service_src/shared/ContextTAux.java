package de.zib.gndms.shared;

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
 * @author Stefan Plantikow<plantikow@zib.de>
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
