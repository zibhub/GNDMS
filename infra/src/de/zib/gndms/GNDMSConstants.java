package de.zib.gndms;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:00:27
 */
public final class GNDMSConstants {
	public static final String JNDI_BASE_NAME="java:comp/env/";

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
