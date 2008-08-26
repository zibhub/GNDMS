package de.zib.gndms.infra.system;

import de.zib.gndms.infra.GridConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;


/**
 * Mockup GridConfig for Testing
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 17:34:44
 */
public final class GridConfigMockup extends GridConfig {
		private final String gridName;


		public GridConfigMockup(final String gridNameParam) {
			super();
			gridName = gridNameParam;
		}


		@Override public @NotNull String getGridJNDIEnvName() throws Exception {
			// safe to do here
			return "";
		}


		@Override public @NotNull String getGridName() throws Exception {
			return gridName;
		}


		@Override public @NotNull String getGridPath() throws Exception {
			return System.getenv("GLOBUS_LOCATION") +  File.separator + "etc"
				  + File.separator + getGridName() + "_shared" + File.separator;
		}
}