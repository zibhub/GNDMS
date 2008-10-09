package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.factory.AbstractRecursiveFactory;
import de.zib.gndms.kit.factory.FactoryInstance;
import de.zib.gndms.kit.factory.RecursiveFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 17:56:30
 */
public abstract class OfferTypeMetaFactory<T extends FactoryInstance<OfferType, T>> extends
        AbstractRecursiveFactory<OfferType, RecursiveFactory<OfferType, T>>
{

    @Override
    @SuppressWarnings({ "unchecked" })
    @NotNull
    public RecursiveFactory<OfferType, T> getInstance(@NotNull final OfferType key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final RecursiveFactory<OfferType, T> factory = newInstance(key);
        factory.setKey(key);
        factory.setFactory(this);
        factory.setup();
        return factory;
    }


    @SuppressWarnings({ "unchecked" })
    @Override
    public RecursiveFactory<OfferType, T> newInstance(final OfferType key)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String factoryClassName = getFactoryClassName(key);
        Class<? extends RecursiveFactory<OfferType, T>> clazz;
        clazz = (Class<? extends RecursiveFactory<OfferType, T>>)
                Class.forName(factoryClassName);
        return clazz.newInstance();
    }


    public abstract String getFactoryClassName(final OfferType key);


    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }

    
}
