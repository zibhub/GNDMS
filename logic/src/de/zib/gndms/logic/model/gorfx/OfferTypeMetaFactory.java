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
	implements Wrapper<RecursiveFactory<OfferType, T>>
{
	private Wrapper<? super RecursiveFactory<OfferType, T>> wrap;

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


    @Override
    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }


	public Wrapper<? super RecursiveFactory<OfferType, T>> getWrap() {
		return wrap;
	}


	public void setWrap(final Wrapper<? super RecursiveFactory<OfferType, T>> wrapParam) {
		wrap = wrapParam;
	}


	public <X extends RecursiveFactory<OfferType, T>, Y extends RecursiveFactory<OfferType, T>> Y wrap(final Class<Y> wrapClass, final X wrapped) {
		final Wrapper<? super RecursiveFactory<OfferType, T>> curWrap = getWrap();
		if (curWrap == null)
			return wrapClass.cast(wrapped);
		else
			return curWrap.wrap(wrapClass, wrapped);
	}




}
