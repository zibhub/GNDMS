package de.zib.gndms.infra.system;

import de.zib.gndms.kit.access.InstanceProvider;
import de.zib.gndms.infra.access.ServiceHomeProvider;
import de.zib.gndms.logic.access.TaskActionProvider;
import de.zib.gndms.kit.config.ConfigletProvider;
import de.zib.gndms.logic.model.gorfx.ORQCalculatorProvider;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:55:14
 */
public interface SystemDirectory
	  extends ConfigletProvider, ORQCalculatorProvider, TaskActionProvider, InstanceProvider,
	  ServiceHomeProvider, SystemInfo {
}
