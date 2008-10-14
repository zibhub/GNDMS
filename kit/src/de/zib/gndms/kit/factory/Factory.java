package de.zib.gndms.kit.factory;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 17:24:24
 */
@SuppressWarnings({ "InterfaceNamingConvention" })
public interface Factory<K, T extends FactoryInstance<K, T>> {
    @NotNull T getInstance(@NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException;

    void setup();
}
