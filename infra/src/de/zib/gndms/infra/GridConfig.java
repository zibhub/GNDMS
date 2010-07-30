package de.zib.gndms.infra;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
     * Creates a subContext-chain, starting from {@link Constants#getRootContext()},
     * with the names {@link #getGridJNDIEnvName()} ,{@link #getGridName()}, {@code partionName} in the given order
     * and returns the last subContext.
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
     * Let {@code names} be a list of m entries {@code names={name_1, .. , name_m} }, sC_1 the startContext,
     * then this method makes sure, that {@code sC_i} has a subContext {@code sC_(i+1)} bounded to the name {@code name_i},
     * for all Integers {@code i} between 1 and {@code m}.
     *
     * The last Context in this subContext-chain ({@code sC_(m+1)}) is returned.
     *
     * A new subContext is only created if a Context does not already have the desired subContext.
     *
     * @param startContext a Context, for which a subContext chain will be created
     * @param names the list of names for the subcontext chain, created in the given order
     * @return the subcontext corresponding to the last entry of {@code names} in the subContext-chain
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
