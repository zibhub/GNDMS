package de.zib.gndms.infra;

import org.jetbrains.annotations.NonNls;

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
	public static final String JNDI_DB_CONTEXT_NAME = "db";

	@NonNls
	public static final String JNDI_DB_FACADE_INSTANCE_NAME = "facadeInstance";

	private Constants() {}

	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	public static Context getRootContext() throws NamingException {
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
