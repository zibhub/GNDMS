package de.zib.gndms.stuff.mold;

import org.jetbrains.annotations.NotNull;

/**
 * This Interface is needed to create a {@code molder} for a specific class.
 *
 *
 * @see Molder
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 12:08:19
 */
public interface Molding {
    /**
     * Returns a molder for a specific class. 
     * @param moldedClazz the class, the {@code molder} should be applied to
     * @return a molder for instances of {@code moldedClazz}'s class
     */
    <D> Molder<D> molder(@NotNull final Class<D> moldedClazz);
}
