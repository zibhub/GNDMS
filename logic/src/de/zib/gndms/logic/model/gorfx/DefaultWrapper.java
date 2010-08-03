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



import org.jetbrains.annotations.NotNull;


/**
 * Default implementation of the {@link Wrapper} Interface.
 *
 * It is used to wrap an element of a class {@code X} to an element of class {@code Y}, by simply invoking
 * {@code cast()} with the element as parameter. Furthermore a special wrapper method is used, if the element belongs to
 * a certain interface.
 *
 * The template parameter {@code I} defines an interfaceclass. If the element which is to be wrapped belongs to that
 * interface, a special wrap method will be used.
 * The template parameter {@code T} defines a common superclass of class {@code X} and class {code Y}.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.11.2008 Time: 16:54:01
 */
public abstract class DefaultWrapper<I, T> implements Wrapper<T> {

	protected DefaultWrapper(final Class<I> interfaceClassParam) {
		interfaceClass = interfaceClassParam;
	}

	private final Class<I> interfaceClass;

    /**
     * Wrapps {@code wrapped} to instance of class Y. If {@code X} implements} {@link #interfaceClass},
     * {@link #wrapInterfaceInstance(Class, Object)} will be used. Otherwise {@code wrapClass.cast(wrapped)} will be
     * invoked.
     * 
     * @param wrapClass
     * @param wrapped
     * @param <X>
     * @param <Y>
     * @return
     */
	public <X extends T, Y extends T> Y wrap(final Class<Y> wrapClass, final X wrapped) {
		if (wrapped == null)
			return null;
		else {
			if (getInterfaceClass().isInstance(wrapped))
				return wrapInterfaceInstance(wrapClass, getInterfaceClass().cast(wrapped));
			else
				return wrapClass.cast(wrapped);
		}

	}


	protected @NotNull Class<I> getInterfaceClass() {
		return interfaceClass;
	}

    /**
     * An alternative way to wrap an element if it belongs to the interface {@code I}.
     *
     *
     * @param wrapClass
     * @param wrappedAsItf
     * @param <Y>
     * @return
     */
	protected abstract <Y extends T> Y wrapInterfaceInstance(final Class<Y> wrapClass, @NotNull final I wrappedAsItf);
}
