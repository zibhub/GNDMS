package de.zib.gndms.kit.monitor;

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



import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.jetbrains.annotations.NotNull;

import java.security.Principal;

/**
 * Instances create a fresh groovy binding (i.e. to be used for a groovy shell instance
 * inside a GroovyMonitor) and destroy it when done.
 *
 * @see GroovyMonitor
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.07.2008 Time: 22:33:25
 */
public interface GroovyBindingFactory {
	/**
	 * Create a new groovy binding object.
	 * 
	 * @param moniServer that runs the shell that uses this binding
	 * @param principal that will use this binding
	 * @param args
	 * @return a new binding to be used by some groovy shell
	 */
	@NotNull
	Binding createBinding(
		  @NotNull GroovyMoniServer moniServer,
		  @NotNull Principal principal, @NotNull String args);

	/**
	 * Optionally called after createBinding to setup an associated groovy shell.
	 */
	void initShell(@NotNull GroovyShell shell, @NotNull Binding binding);

	/**
	 * Called when the binding is no longer needed.
	 *
	 * @param moniServer
	 * @param bindings
	 */
	void destroyBinding(@NotNull GroovyMoniServer moniServer, @NotNull Binding binding);
}
