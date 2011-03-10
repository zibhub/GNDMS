package de.zib.gndms.stuff;

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



import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;


/**
 * The {@code BoundInjector} class wrappes an {@link Injector}.
 * Thus, it provides setter and getter methods and a method to inject dependencies into an object
 *
 * @author  try ste fan pla nti kow zib
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
