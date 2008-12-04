package de.zib.gndms.stuff.mold;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:24:18
 */
public interface Molder<V> {
	void mold(@NotNull V molded);
}
