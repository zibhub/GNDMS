package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.common.types.factory.AbstractRecursiveKeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.common.types.factory.RecursiveKeyFactory;
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
public abstract class OfferTypeMetaFactory<T extends KeyFactoryInstance<OfferType, T>> extends
	  AbstractRecursiveKeyFactory<OfferType, RecursiveKeyFactory<OfferType, T>>
	implements Wrapper<RecursiveKeyFactory<OfferType, T>>
{
	private Wrapper<? super RecursiveKeyFactory<OfferType, T>> wrap;

    @Override
    @SuppressWarnings({ "unchecked" })
    @NotNull
    public RecursiveKeyFactory<OfferType, T> getInstance(@NotNull final OfferType key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final RecursiveKeyFactory<OfferType, T> factory = newInstance(key);
        factory.setKey(key);
        factory.setFactory(this);
        factory.setup();
        return factory;
    }


    @SuppressWarnings({ "unchecked" })
    @Override
    public RecursiveKeyFactory<OfferType, T> newInstance(final OfferType key)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String factoryClassName = getFactoryClassName(key);
        Class<? extends RecursiveKeyFactory<OfferType, T>> clazz;
        clazz = (Class<? extends RecursiveKeyFactory<OfferType, T>>)
                Class.forName(factoryClassName);
        return clazz.newInstance();
    }


    public abstract String getFactoryClassName(final OfferType key);


    @Override
    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }


	public Wrapper<? super RecursiveKeyFactory<OfferType, T>> getWrap() {
		return wrap;
	}


	public void setWrap(final Wrapper<? super RecursiveKeyFactory<OfferType, T>> wrapParam) {
		wrap = wrapParam;
	}


	public <X extends RecursiveKeyFactory<OfferType, T>, Y extends RecursiveKeyFactory<OfferType, T>> Y wrap(final Class<Y> wrapClass, final X wrapped) {
		final Wrapper<? super RecursiveKeyFactory<OfferType, T>> curWrap = getWrap();
		if (curWrap == null)
			return wrapClass.cast(wrapped);
		else
			return curWrap.wrap(wrapClass, wrapped);
	}




}
