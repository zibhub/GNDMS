package de.zib.gndms.model.common.types.factory;

import org.jetbrains.annotations.NotNull;


/**
 * An instance of this class stores one {@link KeyFactory} along with a corresponding key.
 *
 * The instance's factory creates, given the right key, the instance.
 *
 * The first template parameter is the key type, the second template parameter specifies the {@code KeyFactoryInstance} type.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.10.2008 Time: 10:58:31
 */
public interface KeyFactoryInstance<K, T extends KeyFactoryInstance<K, T>> {
    /**
     * Returns the {@code KeyFactory} of a this instance
     * @return
     */
    KeyFactory<K, T> getFactory();

    /**
     *
     * @param factoryParam
     */
    void setFactory(final @NotNull KeyFactory<K, T> factoryParam);

    /**
     * Returns the key of this instance
     * @return
     */
    K getKey();

    /**
     * WAS ist die Bedeutung des keys ?
     * Er meinte nicht Vaterknoten der ihn erstellt hat.
     * Sets the key for this instance
     * 
     * @param keyParam a key which will be stored along with a KeyFactory
     */
    void setKey(final @NotNull K keyParam);
}
