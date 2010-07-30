package de.zib.gndms.kit.monitor;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.security.Principal;

/**
 * A binding factory that does not besides creating empty bindings.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 25.07.2008 Time: 14:09:55
 */
public final class EmptyBindingFactory implements GroovyBindingFactory {
	@NotNull
	public Binding createBinding(
		  @NotNull GroovyMoniServer groovyMoniServer, @NotNull Principal principal, @NotNull String s) {
		return new Binding();
	}

	public void initShell(@NotNull GroovyShell shell, @NotNull Binding binding) {
		// intentionally nothing
	}

	public void destroyBinding(@NotNull GroovyMoniServer moniServer, @NotNull Binding binding) {
		// intentionally nothing
	}

}
