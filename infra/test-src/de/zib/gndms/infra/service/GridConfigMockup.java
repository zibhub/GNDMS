package de.zib.gndms.infra.service;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
