package de.zib.gndms.model.common.types.factory;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 15:48:08
 */
public abstract class IndustrialPark<K, I, T extends KeyFactoryInstance<K, T>>
        implements RecursiveKeyFactory<K, T> {

    private final @NotNull Map<I, KeyFactory<K, T>> map = Maps.newHashMap();
    private @NotNull
    KeyFactory<K, RecursiveKeyFactory<K, T>> metaFactory;
    private K factoryKey;


    public IndustrialPark() {

    }
    public IndustrialPark(final @NotNull KeyFactory<K, RecursiveKeyFactory<K, T>> factoryParam) {
        setFactory(factoryParam);
    }

    public synchronized KeyFactory<K, T> getFactory(final @NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull I mappedKey = mapKey(key);
        if (map.containsKey(mappedKey))
            return map.get(mappedKey);
        final @NotNull RecursiveKeyFactory<K, T> newValue = getFactory().getInstance(key);
        map.put(mappedKey, newValue);
        return newValue;
    }


    public abstract @NotNull I mapKey(final K keyParam);


    public KeyFactory<K, RecursiveKeyFactory<K, T>> getFactory() {
        return metaFactory;
    }


    public void setFactory(final @NotNull KeyFactory<K, RecursiveKeyFactory<K, T>> factoryParam) {
       metaFactory = factoryParam;
    }


    public @NotNull T getInstance(@NotNull final K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull KeyFactory<K, T> factory = getFactory(key);
        return factory.getInstance(key);
    }


    public K getKey() {
        return factoryKey;
    }


    public void setKey(final @NotNull K keyParam) {
        factoryKey = keyParam;
    }

    
    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }
}

