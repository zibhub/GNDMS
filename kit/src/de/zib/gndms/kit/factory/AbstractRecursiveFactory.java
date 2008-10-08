package de.zib.gndms.kit.factory;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 17:44:42
 */
public abstract class AbstractRecursiveFactory<K, T extends FactoryInstance<K, T>> implements RecursiveFactory<K, T> {
    Factory<K, RecursiveFactory<K, T>> factory;
    K key;


    public Factory<K, RecursiveFactory<K, T>> getFactory() {
        return factory;
    }


    public void setFactory(@NotNull final Factory<K, RecursiveFactory<K, T>> factoryParam) {
        factory = factoryParam;
    }


    public K getKey() {
        return key;
    }


    public void setKey(@NotNull final K keyParam) {
        key = keyParam;
    }
}
