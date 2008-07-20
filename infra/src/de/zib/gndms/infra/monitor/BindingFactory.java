package de.zib.gndms.infra.monitor;

import groovy.lang.Binding;

import java.security.Principal;

/**
 * Instances create a fresh binding to be used for a shell instance by
 * GroovyManagementConsoleRunner
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.07.2008 Time: 22:33:25
 */
public interface BindingFactory {
	Binding createBinding(GroovyMoniConsole console, Principal principal);
}
