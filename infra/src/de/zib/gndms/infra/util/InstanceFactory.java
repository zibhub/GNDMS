package de.zib.gndms.infra.util;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:00:40
 */
public class InstanceFactory<T> implements Factory<T> {
    private final Class<T> clazz;


    public InstanceFactory(final @NotNull Class<T> clazzParam) {
        clazz = clazzParam;
    }


    public Class<T> getClazz() {
        return clazz;
    }


    public @NotNull T getInstance() throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }
}
