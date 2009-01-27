package de.zib.gndms.kit.system;

import org.jetbrains.annotations.NotNull;
import com.google.inject.Injector;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.12.2008 Time: 11:18:10
 */
public interface SystemInfo {
	@NotNull String getSystemName();
	
	@NotNull String getSystemTempDirName();

	@NotNull Injector getSystemAccessInjector();
}
