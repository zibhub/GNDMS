package de.zib.gndms.infra.db;

import org.apache.log4j.Logger;
import org.globus.wsrf.jndi.Initializable;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:09:00
 */
public final class DbSetupFacade implements Initializable {

	private static final Logger logger = Logger.getLogger(DbSetupFacade.class);

	public DbSetupFacade() {

	}

	public void initialize() throws Exception {
		System.out.println("YAY!");
		logger.info("DbSetupFacade initialized");
	}
}
