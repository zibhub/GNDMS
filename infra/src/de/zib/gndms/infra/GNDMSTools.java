package de.zib.gndms.infra;

import org.globus.wsrf.impl.SimpleResourceKey;
import de.zib.gndms.model.common.SimpleRKRef;


/**
 * Utility functions that are used globally.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.08.2008 Time: 15:52:55
 */
public final class GNDMSTools {
	public static SimpleResourceKey SRKVepRefAsKey(final SimpleRKRef vep) {
		return new SimpleResourceKey(vep.getResourceKeyName(), vep.getResourceKeyValue());
	}
}
