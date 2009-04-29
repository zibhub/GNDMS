package de.zib.gndms.model.common.types.factory;

import org.jetbrains.annotations.NotNull;


/**
 * Default implementation of the RecursiveKeyFactory interface.
 *
 * The creation of a new instance is delegated to the implementing subclass by defining the {@link #newInstance(Object)}
 * method.
 *
 * @see RecursiveKeyFactory
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 17:44:42
 */
public abstract class AbstractRecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> implements
	  RecursiveKeyFactory<K, T> {
    KeyFactory<K, RecursiveKeyFactory<K, T>> factory;
    K key;


    @NotNull
    public T getInstance(@NotNull final K keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull T newInstance = newInstance(keyParam);
        newInstance.setKey(keyParam);
        newInstance.setFactory(this);
        return newInstance;
    }

    /**
     * Must be implemented to define how a new instance corresponding to the given key is created.
     * Will be used by {@link #getInstance(Object)}.
     * 
     * @param keyParam a key defining which instance should be created
     * @return a new instance, corresponding to the given key
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public abstract T newInstance(final K keyParam) throws IllegalAccessException,
            InstantiationException, ClassNotFoundException;


    public KeyFactory<K, RecursiveKeyFactory<K, T>> getFactory() {
        return factory;
    }


    public void setFactory(@NotNull final KeyFactory<K, RecursiveKeyFactory<K, T>> factoryParam) {
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
