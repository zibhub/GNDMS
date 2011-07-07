package de.zib.gndms.model.common.types.factory;

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



import de.zib.gndms.stuff.BoundInjector;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;


/**
 * An InjectingRecursiveKeyFactory extends an AbstractRecursiveKeyFactory by an {@link BoundInjector}.
 * 
 * All methods concerning the Boundinjector are made {@code synchronized}.
 * 
 * @author  try ste fan pla nti kow zib
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

    /**
     * Invokes {@link #injectMembers(Object)} on {@code obj} if a {@code BoundInjector} has been set and
     * if obj is not {@code null}.
     *
     * @param obj an Object, members shall be injected on
     */
	public synchronized void optionallyInjectMembers(Object obj) {
		if (obj == null) return;
		final BoundInjector inj = optionallyGetBoundInjector();
		if (inj != null) inj.injectMembers(obj);
	}

    /**
     *
     * @see de.zib.gndms.stuff.BoundInjector#injectMembers(Object) 
     */
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
