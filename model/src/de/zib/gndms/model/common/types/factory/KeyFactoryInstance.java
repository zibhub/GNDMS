package de.zib.gndms.model.common.types.factory;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.10.2008 Time: 10:58:31
 */
public interface KeyFactoryInstance<K, T extends KeyFactoryInstance<K, T>> {
    KeyFactory<K, T> getFactory();
    void setFactory(final @NotNull KeyFactory<K, T> factoryParam);

    K getKey();
    void setKey(final @NotNull K keyParam);
}
