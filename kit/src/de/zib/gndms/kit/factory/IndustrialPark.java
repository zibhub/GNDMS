package de.zib.gndms.kit.factory;

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
public abstract class IndustrialPark<K, I, T extends FactoryInstance<K, T>>
        implements RecursiveFactory<K, T> {

    private final @NotNull Map<I, Factory<K, T>> map = Maps.newHashMap();
    private @NotNull Factory<K, RecursiveFactory<K, T>> metaFactory;
    private K factoryKey;


    public IndustrialPark() {

    }
    public IndustrialPark(final @NotNull Factory<K, RecursiveFactory<K, T>> factoryParam) {
        setFactory(factoryParam);
    }

    public synchronized Factory<K, T> getFactory(final @NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull I mappedKey = mapKey(key);
        if (map.containsKey(mappedKey))
            return map.get(mappedKey);
        final @NotNull RecursiveFactory<K, T> newValue = getFactory().getInstance(key);
        map.put(mappedKey, newValue);
        return newValue;
    }


    public abstract @NotNull I mapKey(final K keyParam);


    public Factory<K, RecursiveFactory<K, T>> getFactory() {
        return metaFactory;
    }


    public void setFactory(final @NotNull Factory<K, RecursiveFactory<K, T>> factoryParam) {
       metaFactory = factoryParam;
    }




    public @NotNull T getInstance(@NotNull final K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull Factory<K, T> factory = getFactory(key);
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

