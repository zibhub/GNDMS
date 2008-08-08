package de.zib.gndms.infra.db

import de.zib.gndms.infra.system.GNDMSystem
import de.zib.gndms.infra.service.GNDMServiceHome
import de.zib.gndms.infra.model.ModelHandler
import de.zib.gndms.model.dspace.DSpace
import de.zib.gndms.infra.system.GridConfigMockup
import de.zib.gndms.infra.service.GNDMServiceHomeMockup
import de.zib.gndms.infra.model.ModelCreator
import org.jetbrains.annotations.NotNull

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
		sys.tryTxExecution();
		final GNDMServiceHome home = new GNDMServiceHomeMockup(sys);
		ModelHandler mH = new ModelHandler(DSpace.class, home);
		DSpace model = (DSpace) mH.getSingleModel(null, "findDSpaceInstances", 
		                                          new SimpleDSpaceModelCreator());
		model.getId();
		System.out.println("foo");
	}


}

private final class SimpleDSpaceModelCreator implements ModelCreator<DSpace> {
	@NotNull
	def public DSpace createInitialModel(@NotNull String id, @NotNull String gridName) {
		DSpace model = new DSpace();
		model.setId(id);
		model.setGridName(gridName);
		return model;
	}

}