package de.zib.gndms.infra;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Common constants for GNDMS.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:00:27
 */
public final class Constants {
	@NonNls
	@SuppressWarnings({"HardcodedFileSeparator"})
	public static final String JNDI_BASE_NAME="java:comp/env/";

	@NonNls
	public static final String JNDI_DB_CONTEXT_NAME = "sys";

	@NonNls
	public static final String JNDI_DB_FACADE_INSTANCE_NAME = "facadeInstance";

	private Constants() {}

    /**
     * Returns the root context, by looking up the JNDI base name
     * @return Returns the root, by looking up the JNDI base name
     * @throws NamingException if a naming exception occurs.
     */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	public static @NotNull Context getRootContext() throws NamingException {
		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			return (Context) ctx.lookup(JNDI_BASE_NAME);
		}
		finally {
			if (ctx != null) ctx.close();
		}
	}
}
