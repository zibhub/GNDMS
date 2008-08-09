package de.zib.gndms.infra.db

import de.zib.gndms.infra.system.GNDMSystem
import de.zib.gndms.infra.service.GNDMServiceHome
import de.zib.gndms.infra.model.GridEntityModelHandler
import de.zib.gndms.model.dspace.DSpace
import de.zib.gndms.infra.system.GridConfigMockup
import de.zib.gndms.infra.service.GNDMServiceHomeMockup
import de.zib.gndms.infra.model.DefaultModelInitializer
import de.zib.gndms.infra.model.SingletonGridResourceModelHandler

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 08.08.2008 Time: 17:41:48
 */
class DbTest {

	public static void main(String[] args) throws ResourceException {
		final String gridName = "c3grid";

		GNDMSystem sys = new GNDMSystem(new GridConfigMockup(gridName));
		sys.initialize();
		final GNDMServiceHome home = new GNDMServiceHomeMockup(sys);
		SingletonGridResourceModelHandler mH = new SingletonGridResourceModelHandler(DSpace.class, home);
		DSpace model = (DSpace) mH.getSingleModel(null, "findDSpaceInstances", 
		                                          new DefaultModelInitializer<DSpace>());
		model.getId();
		System.out.println("foo");
	}
}
