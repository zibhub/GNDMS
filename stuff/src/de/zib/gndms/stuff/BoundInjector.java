package de.zib.gndms.stuff;

import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;


/**
 * The {@code BoundInjector} class wrappes an {@link Injector}.
 * Thus, it provides setter and getter methods and a method to inject dependencies into an object
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.12.2008 Time: 12:37:41
 */
public final class BoundInjector {

	private Injector injector;

    /**
     * Returns the currently used {@code Injector}.If not set, {@code null} is returned
     * 
     * @return the currently used {@code Injector}.If not set, {@code null} is returned
     */
	public synchronized Injector optionallyGetInjector() {
        return injector;
	}

	public synchronized @NotNull Injector getInjector() {
		if (injector == null)
			throw new IllegalStateException("Injector not yet set");
		else
			return injector;
	}

    
    public synchronized void setInjector(final @NotNull Injector injectorParam) {
		if (injector == null)
			injector = injectorParam;
		else
			if (injector != injectorParam)
				throw new IllegalStateException("Attempt to overwrite injector");
	}

    /**
     * Injects the dependecies into the methods and fields of {@code obj}.
     *
     * @param obj the obj, dependencies should be injected to 
     */
	public void injectMembers(Object obj) {
		if (obj == null)
			return;
		getInjector().injectMembers(obj);
	}
}
