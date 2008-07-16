package de.zib.gndms.infra;

import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.07.2008 Time: 14:22:20
 */
@SuppressWarnings({"OverloadedMethodsWithSameNumberOfParameters"})
public final class GNDMSConfig {

	private GNDMSConfig() {}

	@NotNull
	public static Context findSharedContext(@NotNull Context startContext, @NotNull Name name)
		  throws NamingException {
		Context resultContext;

		try {
			resultContext = startContext.createSubcontext(name);
		}
		catch (NameAlreadyBoundException e) {
			resultContext = (Context) startContext.lookup(name);
		}
		return resultContext;
	}

	@NotNull
	public static Context findSharedContext(@NotNull Context startContext, @NotNull String name)
		  throws NamingException {
		Context resultContext;

		try {
			resultContext = startContext.createSubcontext(name);
		}
		catch (NameAlreadyBoundException e) {
			resultContext = (Context) startContext.lookup(name);
		}
		return resultContext;
	}

	@NotNull
	public static Context findSharedContext(@NotNull Context startContext, @NotNull Name[] names)
		  throws NamingException {
		Context context = startContext;
		for (Name name : names)
			context = findSharedContext(context, name);
		return context;
	}

	@NotNull
	public static Context findSharedContext(@NotNull Context startContext, @NotNull String[] names)
		  throws NamingException {
		Context context = startContext;
		for (String name : names)
			context = findSharedContext(context, name);
		return context;
	}
}
