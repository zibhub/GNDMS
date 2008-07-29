package de.zib.gndms.infra.db;

import org.jetbrains.annotations.NotNull;

/**
 * A simple box that holds a GNDMSystem instance.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 25.07.2008 Time: 13:01:27
 */
@SuppressWarnings({"ClassReferencesSubclass"})
public interface SystemHolder {
	@NotNull
	GNDMSystem getSystem() throws IllegalStateException;

	void setSystem(@NotNull GNDMSystem system) throws IllegalStateException;

}
