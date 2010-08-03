package de.zib.gndms.model.common.types.factory;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.jetbrains.annotations.NotNull;


/**
 * Default implementation of the RecursiveKeyFactory interface.
 *
 * The creation of a new instance is delegated to the implementing subclass by defining the {@link #newInstance(Object)}
 * method.
 *
 * As described in {@link RecursiveKeyFactory}, this factory can be created by a KeyFactory.
 *
 * @see RecursiveKeyFactory
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 17:44:42
 */
public abstract class AbstractRecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> implements
	  RecursiveKeyFactory<K, T> {
    KeyFactory<K, RecursiveKeyFactory<K, T>> factory;
    K key;


    /**
     * A new instance of type {@code T} is created by invoking {@link #newInstance(Object)}.
     * Before the instance is returned, {@code this} KeyFactory will be registered as new Factory of the created instance,
     * and the used key {@code keyParam} will be registered as the used key for the instance,
     * by calling the methods {@code setKey()} and {@code setFactory} and the created instance.
     *
     * @param keyParam the key for the new instance, which shall be created
     * @return a new instance of type {@code T} which has been created by {@link #newInstance(Object)}
     *         using the key {@code keyParam}
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
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
