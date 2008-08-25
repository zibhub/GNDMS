package de.zib.gndms.infra.monitor;

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
