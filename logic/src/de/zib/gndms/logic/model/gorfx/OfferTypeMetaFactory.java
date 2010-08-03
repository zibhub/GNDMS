package de.zib.gndms.logic.model.gorfx;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import com.google.inject.Injector;
import de.zib.gndms.model.common.types.factory.AbstractRecursiveKeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.common.types.factory.RecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 17:56:30
 */
public abstract class OfferTypeMetaFactory<T extends KeyFactoryInstance<OfferType, T>> extends
	  AbstractRecursiveKeyFactory<OfferType, RecursiveKeyFactory<OfferType, T>>
	implements Wrapper<RecursiveKeyFactory<OfferType, T>>
{
	private Wrapper<? super RecursiveKeyFactory<OfferType, T>> wrap;
	private Injector injector;

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


    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
    @Override
    public RecursiveKeyFactory<OfferType, T> newInstance(final OfferType key)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String factoryClassName = getFactoryClassName(key);
        Class<? extends RecursiveKeyFactory<OfferType, T>> clazz;
        clazz = (Class<? extends RecursiveKeyFactory<OfferType, T>>)
                Class.forName(factoryClassName);
        return wrap(clazz, (RecursiveKeyFactory) clazz.newInstance());
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
		if (injector != null)
			injector.injectMembers(wrapped);

		final Wrapper<? super RecursiveKeyFactory<OfferType, T>> curWrap = getWrap();

		if (curWrap == null)
			return wrapClass.cast(wrapped);
		else
			return curWrap.wrap(wrapClass, wrapped);
	}


	public Injector getInjector() {
		return injector;
	}


	public void setInjector(final Injector injectorParam) {
		injector = injectorParam;
	}
}
