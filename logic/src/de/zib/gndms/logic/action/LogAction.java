package de.zib.gndms.logic.action;

import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 22.10.2008 Time: 11:18:28
 */
public interface LogAction {
    void setLog(@NotNull Log log);
    
    @NotNull Log getLog();

}
