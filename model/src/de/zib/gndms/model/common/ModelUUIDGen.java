package de.zib.gndms.model.common;

import org.jetbrains.annotations.NotNull;


/**
 * Wrapper interface for the separation of model and webservice layer.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 11.08.2008 Time: 14:15:24
 */
public interface ModelUUIDGen {
	@NotNull String nextUUID();
}
