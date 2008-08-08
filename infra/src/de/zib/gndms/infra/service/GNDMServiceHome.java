package de.zib.gndms.infra.service;

import de.zib.gndms.infra.db.EMFactoryProvider;
import de.zib.gndms.infra.system.SystemHolder;


/**
 * Shared interface of all GNDMS service resource homes
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:51:59
 */
public interface GNDMServiceHome extends ServiceInfo, EMFactoryProvider, SystemHolder {
}
