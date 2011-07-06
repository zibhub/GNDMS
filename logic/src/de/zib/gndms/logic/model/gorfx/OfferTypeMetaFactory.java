package de.zib.gndms.logic.model.gorfx;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.stuff.GNDMSInjector;
import de.zib.gndms.model.common.types.factory.AbstractRecursiveKeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.common.types.factory.RecursiveKeyFactory;
import de.zib.gndms.neomodel.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 17:56:30
 */
public abstract class OfferTypeMetaFactory<T extends KeyFactoryInstance<String, T>> extends
	  AbstractRecursiveKeyFactory<String, RecursiveKeyFactory<String, T>>
	implements Wrapper<RecursiveKeyFactory<String, T>>
{
	private Wrapper<? super RecursiveKeyFactory<String, T>> wrap;
	private GNDMSInjector injector;

    @Override
    @SuppressWarnings({ "unchecked" })
    @NotNull
    public RecursiveKeyFactory<String, T> getInstance(@NotNull final String key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final RecursiveKeyFactory<String, T> factory = newInstance(key);
        factory.setKey(key);
        factory.setFactory(this);
        factory.setup();
        return factory;
    }


    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
    @Override
    public RecursiveKeyFactory<String, T> newInstance(final String key)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String factoryClassName = getFactoryClassName(key);
        Class<? extends RecursiveKeyFactory<String, T>> clazz;
        clazz = (Class<? extends RecursiveKeyFactory<String, T>>)
                Class.forName(factoryClassName);
        return wrap(clazz, (RecursiveKeyFactory) clazz.newInstance());
    }


    public abstract String getFactoryClassName(final String key);


    @Override
    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void setup() {
        // intentional
    }


	public Wrapper<? super RecursiveKeyFactory<String, T>> getWrap() {
		return wrap;
	}


	public void setWrap(final Wrapper<? super RecursiveKeyFactory<String, T>> wrapParam) {
		wrap = wrapParam;
	}


	public <X extends RecursiveKeyFactory<String, T>, Y extends RecursiveKeyFactory<String, T>> Y wrap(final Class<Y> wrapClass, final X wrapped) {
		if (injector != null)
			injector.injectMembers(wrapped);

		final Wrapper<? super RecursiveKeyFactory<String, T>> curWrap = getWrap();

		if (curWrap == null)
			return wrapClass.cast(wrapped);
		else
			return curWrap.wrap(wrapClass, wrapped);
	}


	public GNDMSInjector getInjector() {
		return injector;
	}


	public void setInjector(final GNDMSInjector injectorParam) {
		injector = injectorParam;
	}
}
