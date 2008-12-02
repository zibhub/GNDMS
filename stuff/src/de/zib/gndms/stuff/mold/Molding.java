package de.zib.gndms.stuff.mold;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 12:08:19
 */
public interface Molding {
	<D> Molder<D> molder(@NotNull final Class<D> moldedClazz);
}
