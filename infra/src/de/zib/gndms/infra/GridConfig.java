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


    /**
     * Returns the subContext of {@link Constants#getRootContext()}, which is bounded to {@code partitionName}.
     * Furthermore it makes sure, that the subcontexts, bounded to {@code getGridJNDIEnvName()} and {@code getGridName()}
     * exists.
     *
     * @see #findSharedContext(javax.naming.Context, String)
     * @param partitionName a name, a subcontext is or will be bounded to
     * @return the subcontext, which has been bounded with partitionName
     * @throws NamingException if a naming exception occurs, while executing {@code createSubcontext()} or {@code lookup()}
     */
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

    /**
     *
     * @see #findSharedContext(javax.naming.Context, String)
     */
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

    /**
     * Returns the {@code Subcontext} of {@code startContext}, which is bounded to {@code name}.
     * If it does not exist, a new subcontext is created and bounded to {@code name}.
     *
     * @param startContext a Context, which is supposed to have a certain subContext
     * @param name the name of a subcontext of {@code startContext}
     * @return a chosen subcontext of {@code startContext}
     * @throws NamingException if a naming exception occurs, while executing {@code createSubcontext()} or {@code lookup()}
     */
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

    /**
     * @see #findSharedContext(javax.naming.Context, String[])
     */
	@NotNull
	public static Context findSharedContext(@NotNull Context startContext, @NotNull Name[] names)
		  throws NamingException {
		Context context = startContext;
		for (Name name : names)
			context = findSharedContext(context, name);
		return context;
	}

    /**
     * Checks for all entries in {@code names}, if a subcontext of {@code startContext} exists,
     * bounded to the corresponding name.
     * If not, a new subcontext is created and bounded to the name.
     *
     * The subcontext, corresponding to the last entry of {@code names} will be returned.
     *
     * @param startContext a Context, which is supposed to have a certain subContexts
     * @param names a list of names subcontexts of {@code startContext}
     * @return the subcontext corresponding to the last entry of {@code names}
     * @throws NamingException if a naming exception occurs, while executing {@code createSubcontext()} or {@code lookup()}
     */
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
        boolean debugMode = isDebugMode();
		return GNDMSystem.lookupSystem(context, Constants.JNDI_DB_FACADE_INSTANCE_NAME,  this, debugMode);
	}

    /**
     * Returns whether to use debug mode or not.
     * Result is retrieved from the system environment variable 'GNDMS_DEBUG'.
     *
     * @return whether to use debug mode or not
     */
    @SuppressWarnings({ "MethodMayBeStatic" })
    public boolean isDebugMode() {
        return System.getenv("GNDMS_DEBUG") != null;
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
