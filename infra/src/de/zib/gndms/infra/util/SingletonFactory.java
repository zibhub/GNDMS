package de.zib.gndms.infra.util;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:02:29
 */
public class SingletonFactory<T> extends InstanceFactory<T> {
    private T instance;


    public SingletonFactory(final @NotNull Class<T> clazzParam) {
        super(clazzParam);
    }


    @Override
    public synchronized @NotNull T getInstance()
            throws IllegalAccessException, InstantiationException {
        if (instance == null)
            instance = super.getInstance();
        return instance;
    }
}
