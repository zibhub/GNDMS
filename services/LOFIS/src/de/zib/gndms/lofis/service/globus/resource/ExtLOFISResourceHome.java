package de.zib.gndms.lofis.service.globus.resource;

import de.zib.gndms.dspace.service.globus.resource.DSpaceResourceHome;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 16.07.2008 Time: 12:37:43
 */
public final class ExtLOFISResourceHome extends LOFISResourceHome {

	// logger can be an instance field since resource home classes are instantiated at most once
	@NotNull
	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private final Log logger = LogFactory.getLog(DSpaceResourceHome.class);

	@Override
	public synchronized void initialize() throws Exception {
		super.initialize();    // Overridden method
		logger.info("GNDMS ResourceHome overriding extension class"
			  + getClass().getName() + " initialized.");
	}
}
