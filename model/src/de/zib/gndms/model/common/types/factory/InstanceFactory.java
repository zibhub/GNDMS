package de.zib.gndms.model.common.types.factory;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:00:40
 */
public class InstanceFactory<T extends KeyFactoryInstance<Class<T>, T>> implements KeyFactory<Class<T>, T> {

    @NotNull
    public T getInstance(@NotNull final Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        final T instance = clazz.newInstance();
        instance.setFactory(this);
        instance.setKey(clazz);
        return instance;
    }


    public void setup() {
    }
}
