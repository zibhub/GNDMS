package de.zib.gndms.infra.util;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 17:24:24
 */
public interface Factory<T> {
    @NotNull T getInstance() throws IllegalAccessException, InstantiationException;
}
