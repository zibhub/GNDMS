package de.zib.gndms.model.common.types.factory;

import org.jetbrains.annotations.NotNull;


/**
 * A KeyFactory creates and returns a {@link KeyFactoryInstance} corresponding to a specific key.
 *
 * The first template parameter is the key type, the second parameter specifies the type of the KeyFactoryInstance object,
 * which will be created and returned.
 *
 * @see KeyFactory
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 17:24:24
 */
@SuppressWarnings({ "InterfaceNamingConvention" })
public interface KeyFactory<K, T extends KeyFactoryInstance<K, T>> {

    
    /**
     * Returns the {@code KeyFactoryInstance} object corresponding to {@code key}.
     *
     * @param key a key corresponding to a KeyFactoryInstance object
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @NotNull T getInstance(@NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException;

    void setup();
}
