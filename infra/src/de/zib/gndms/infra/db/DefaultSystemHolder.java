package de.zib.gndms.infra.db;

import org.jetbrains.annotations.NotNull;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 25.07.2008 Time: 13:07:12
 */
public final class DefaultSystemHolder implements SystemHolder {
	private GNDMSystem system;


	@NotNull
	public synchronized GNDMSystem getSystem() throws IllegalStateException {
		if (system == null)
			throw new IllegalStateException("System not yet set");
		return system;
	}

	public synchronized void setSystem(@NotNull GNDMSystem aSystem) throws IllegalStateException {
		if (system == null)
			system = aSystem;
		else
			throw new IllegalStateException("System already set");
	}
}
