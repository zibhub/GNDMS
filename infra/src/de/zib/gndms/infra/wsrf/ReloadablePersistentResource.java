package de.zib.gndms.infra.wsrf;

import org.globus.wsrf.PersistentResource;
import org.globus.wsrf.ResourceException;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 11:07:52
 */
public interface ReloadablePersistentResource extends PersistentResource {
	void reload() throws ResourceException;
}
