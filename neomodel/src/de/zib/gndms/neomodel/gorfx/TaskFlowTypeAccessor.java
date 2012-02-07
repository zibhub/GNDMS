package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * TaskFlowTypeAccessor
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public interface TaskFlowTypeAccessor extends GridResourceItf {
    @NotNull String getId();

    @NotNull String getTaskFlowTypeKey();

    String getTaskActionFactoryClassName();

    String getCalculatorFactoryClassName();

    Map<String, String> getConfigMapData();
}
