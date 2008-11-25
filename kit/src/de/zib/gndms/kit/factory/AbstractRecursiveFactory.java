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


    @NotNull
    public T getInstance(@NotNull final K keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull T newInstance = newInstance(keyParam);
        newInstance.setKey(keyParam);
        newInstance.setFactory(this);
        return newInstance;
    }


    public abstract T newInstance(final K keyParam) throws IllegalAccessException,
            InstantiationException, ClassNotFoundException;


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

    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
    }

}
