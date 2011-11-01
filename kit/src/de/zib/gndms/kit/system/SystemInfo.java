package de.zib.gndms.kit.system;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;
import de.zib.gndms.stuff.GNDMSInjector;


/**
 * A SystemInfo class returns informations about the GNDM system.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 10.12.2008 Time: 11:18:10
 */
public interface SystemInfo {

    /**
     * Returns the name given to the GNDM system
     * 
     * @return the name given to the GNDM system
     */
	@NotNull String getSystemName();

    /**
     * Returns the temp path chosen for the GNDMSystem
     *
     * @return the temp path chosen for the GNDMSystem
     */
	@NotNull String getSystemTempDirName();

    /**
     * Returns the injector used with the GNDM System
     *
     * @return the injector used with the GNDM System
     */
	@NotNull GNDMSInjector getSystemAccessInjector();
}
