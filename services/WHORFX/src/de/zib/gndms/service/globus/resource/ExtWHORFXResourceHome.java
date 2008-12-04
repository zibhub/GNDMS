package de.zib.gndms.service.globus.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

/**
 * This class overrides the ResourceHome that is automatically generated by introduce for Globus
 * Toolkit. In GNDMS this is mainly necessary to provide RDBMS/JPA-based resource persistence.
 * In order to use the extended resource home they have to be configured in jndi-config.xml.
 * If this has been done properly, you should see an info-level log message during the start up
 * of the web service container that notifies succesfull initialization of the extended resource
 * home.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 16.07.2008 Time: 12:35:27
 */
public final class ExtWHORFXResourceHome extends WHORFXResourceHome {

	// logger can be an instance field since resource home classes are instantiated at most once
	@NotNull
	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private final Log logger = LogFactory.getLog(ExtWHORFXResourceHome.class);

	@Override
	public synchronized void initialize() throws Exception {
		super.initialize();    // Overridden method
		logger.info("Extension class initializing");
	}
}
