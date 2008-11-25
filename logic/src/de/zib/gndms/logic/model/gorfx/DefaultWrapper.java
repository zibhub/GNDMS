package de.zib.gndms.logic.model.gorfx;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.11.2008 Time: 16:54:01
 */
public abstract class DefaultWrapper<I, T> implements Wrapper<T> {

	protected DefaultWrapper(final Class<I> interfaceClassParam) {
		interfaceClass = interfaceClassParam;
	}

	private final Class<I> interfaceClass;

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


	protected abstract <Y extends T> Y wrapInterfaceInstance(final Class<Y> wrapClass, @NotNull final I wrappedAsItf);
}
