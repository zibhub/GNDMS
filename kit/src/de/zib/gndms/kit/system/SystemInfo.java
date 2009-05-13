package de.zib.gndms.kit.system;

import org.jetbrains.annotations.NotNull;
import com.google.inject.Injector;


/**
 * A SystemInfo class returns informations about the GNDM system.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
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
	@NotNull Injector getSystemAccessInjector();
}
