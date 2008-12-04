package de.zib.gndms.kit.monitor;

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
 * @author Stefan Plantikow <plantikow@zib.de>
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
