package de.zib.gndms.infra;

import de.zib.gndms.infra.system.GNDMSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public abstract class GridConfig {

    private static final Log logger = LogFactory.getLog(GridConfig.class);

	@NotNull
	public abstract String getGridJNDIEnvName() throws Exception;

	@NotNull
	public abstract String getGridName() throws Exception;

	@NotNull
	public abstract String getGridPath() throws Exception;

	@NotNull
	public Context getGridContext(@NotNull String partitionName) throws NamingException {
		try {
            final @NotNull Context rootContext = Constants.getRootContext();
            return findSharedContext(rootContext,
				  new String[] { getGridJNDIEnvName(), getGridName(), partitionName });
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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

	public GNDMSystem retrieveSystemReference() throws NamingException {
		Context context = getGridContext(Constants.JNDI_DB_CONTEXT_NAME);
        boolean debugMode = System.getenv("GNDMS_DEBUG") != null;
        logger.info("GNDMS_DEBUG " + (debugMode ? "true" : "false"));
		return GNDMSystem.lookupSystem(context, Constants.JNDI_DB_FACADE_INSTANCE_NAME,  this, debugMode);
	}

    public String asString() {
        try {
            return getGridJNDIEnvName() + '|' + getGridName() + '|' + getGridPath();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
