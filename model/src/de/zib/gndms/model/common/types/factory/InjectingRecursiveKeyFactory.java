package de.zib.gndms.model.common.types.factory;

import de.zib.gndms.stuff.BoundInjector;
import org.jetbrains.annotations.NotNull;
import com.google.inject.Inject;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.12.2008 Time: 13:39:27
 */
public abstract class InjectingRecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>>
	extends AbstractRecursiveKeyFactory<K, T> {

	protected InjectingRecursiveKeyFactory() {
		super();
	}

	// Injection support should be factored out

	private BoundInjector boundInjector;


	public synchronized void optionallyInjectMembers(Object obj) {
		if (obj == null) return;
		final BoundInjector inj = optionallyGetBoundInjector();
		if (inj != null) inj.injectMembers(obj);
	}

	public synchronized void injectMembers(Object obj) {
		if (obj == null) return;
		getBoundInjector().injectMembers(obj);
	}

	public synchronized BoundInjector optionallyGetBoundInjector() {
		return boundInjector;
	}

	public synchronized @NotNull BoundInjector getBoundInjector() {
		if (boundInjector == null)
			throw new IllegalStateException("boundInjector not yet set");
		else
			return boundInjector;
	}

	@Inject
	public synchronized void setBoundInjector(final @NotNull BoundInjector boundInjectorParam) {
		if (boundInjector == null)
			boundInjector = boundInjectorParam;
		else
			if (boundInjector != boundInjectorParam)
			throw new IllegalStateException("Attempt to overwrite boundInjector");
	}	
}
