package de.zib.gndms.model.common.types.factory;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


/**
 * An IndustrialPark is used to retrieve instances of different KeyFactories, which where created by one 'meta'-KeyFactory.
 *
 * To get a desired object (using {@link #getInstance(Object)}), only the key of the instance must be denoted.
 * It retrieves automatically the proper KeyFactory and invokes {@code getInstance(key)} on that factory.
 *
 * The factory is chosen by {@link #getFactory(Object)}, which maps the instance key to a factory key.
 * A concrete subclass must define how this is done by implementing {@link #mapKey(Object)}.
 *
 * The IndustrialPark uses a map store already created KeyFactories.
 * So when an instance key is mapped to an factory key and a factory for that key has already been created, it will be used.
 *
 * The KeyFactories are created by the KeyFactory {@link #metaFactory}.
 *
 * The template parameter {@code K} is the instance's key type.
 * The parameter {@code I} is the factory's key type, which will be used by {@link #map} to get a certain factory.
 * The template parameter {@code T} specifies the type of the instances, which are created by all factories, which are
 * managed by this IndustrialPark.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 15:48:08
 */
public abstract class IndustrialPark<K, I, T extends KeyFactoryInstance<K, T>>
        implements RecursiveKeyFactory<K, T> {

    /**
     * A map, mapping from a factory key to specific KeyFactory.
     * This is used as a cache, so once a certain KeyFactory for a specific factory key has been created,
     * it can be reused, when calling {@code getFactory(key)} with the same key.
     */
    private final @NotNull Map<I, KeyFactory<K, T>> map = Maps.newHashMap();
    /**
     * The KeyFactory which creates a RecursiveKeyFactory for an instance key.
     */
    private @NotNull  KeyFactory<K, RecursiveKeyFactory<K, T>> metaFactory;
    /**
     * The key for this factory, so the key on which this factory has been created on the parent factory {@code metaFactory}.
     */
    private K factoryKey;


    public IndustrialPark() {

    }

    /**
     * Creates a new IndustrialPark instance and stores {@code factoryParam} as the parent factory
     * @param factoryParam the KeyFactory, which creates {@code this}
     */
    public IndustrialPark(final @NotNull KeyFactory<K, RecursiveKeyFactory<K, T>> factoryParam) {
        setFactory(factoryParam);
    }

    /**
     * Returns a specific factory, correspondig to a given instance key.
     *
     * The instance-key {@code key} (of type {@code K}) will be mapped to a KeyFactory key (of type {@code I}),
     * using the abstract method {@link #mapKey(Object)}.
     *
     * If a KeyFactory corresponding to the mapped KeyFactory key has already been created, the method returns the KeyFactory
     * from {@link #map}.
     *
     * If there is not already a Keyfactory registered on {@link #map} for the specified mapped key,
     * a new Keyfactory is created by the parent KeyFactory {@link #metaFactory}, by calling {@code getInstance(key)}
     * on that factory.
     * The new factory is then stored at {@link #map} with the mapped key as the its key and is returned.
     *
     * @param key a key specifying the desired KeyFactory
     * @return the KeyFactory, corresponding to chosen key
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public synchronized KeyFactory<K, T> getFactory(final @NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull I mappedKey = mapKey(key);
        if (map.containsKey(mappedKey))
            return map.get(mappedKey);
        final @NotNull RecursiveKeyFactory<K, T> newValue = getFactory().getInstance(key);
        map.put(mappedKey, newValue);
        return newValue;
    }

    /**
     * Maps an instance key to a factory key.
     *
     * This is used to chose a specific factory which shall return the instance corresponding to the given key {@code keyParam}
     * 
     * @param keyParam an instance key which will be mapped to a specific factory key
     * @return a certain factory key for a KeyFactory, which shall return the   instance,
     * corresponding to instance key {@code keyParam}
     */
    public abstract @NotNull I mapKey(final K keyParam);


    public KeyFactory<K, RecursiveKeyFactory<K, T>> getFactory() {
        return metaFactory;
    }


    public void setFactory(final @NotNull KeyFactory<K, RecursiveKeyFactory<K, T>> factoryParam) {
       metaFactory = factoryParam;
    }

    /**
     * Retrieves a factory from {@link #map}, corresponding to {@code key}
     * and invokes {@code getInstance(key)} on that factory to return the desired instance.
     *
     * @param key a key corresponding to a KeyFactoryInstance object
     * @return an instance corresponding to the given key,
     * and is created by a factory which also corresponds to the given key
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public @NotNull T getInstance(@NotNull final K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull KeyFactory<K, T> factory = getFactory(key);
        return factory.getInstance(key);
    }


    public K getOfferTypeId() {
        return factoryKey;
    }


    public void setOfferTypeId(final @NotNull K keyParam) {
        factoryKey = keyParam;
    }

    
    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }
}

