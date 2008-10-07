package de.zib.gndms.kit.factory;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:02:29
 */
public abstract class SingletonFactory<T extends FactoryInstance<T>> extends InstanceFactory<T> {
    private T instance;


    protected SingletonFactory(final @NotNull OfferType ot) {
        super(ot);
    }


    @Override
    public synchronized @NotNull T getInstance()
            throws IllegalAccessException, InstantiationException {
        if (instance == null)
            instance = super.getInstance();
        return instance;
    }
}
