package de.zib.gndms.infra.system;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import com.google.inject.Binder;
import com.google.inject.Module;
import de.zib.gndms.GNDMSVerInfo;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.grams.LinuxDirectoryAux;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.kit.access.EMFactoryProvider;
import de.zib.gndms.kit.monitor.ActionCaller;
import de.zib.gndms.kit.monitor.GroovyMoniServer;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.LogAction;
import de.zib.gndms.logic.model.*;
import de.zib.gndms.logic.model.gorfx.DefaultWrapper;
import de.zib.gndms.logic.util.LogicTools;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.common.VEPRef;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.ReferencePropertiesType;
import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.jndi.Initializable;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static javax.persistence.Persistence.createEntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;


/**
 * This sets up the configuration and database storage area shared between
 * GNDMS services.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:09:00
 */
@SuppressWarnings({
        "OverloadedMethodsWithSameNumberOfParameters", "NestedAssignment",
        "ClassWithTooManyMethods" })
public final class GNDMSystem
	  implements Initializable, SystemHolder, EMFactoryProvider, Module,
        EntityUpdateListener<GridResource> {
    private static final long EXECUTOR_SHUTDOWN_TIME = 5000L;

	private static @NotNull Log createLogger() { return LogFactory.getLog(GNDMSystem.class); }

    private boolean shutdown;

    private final boolean debugMode;

    private @NotNull final UUIDGen uuidGen = UUIDGenFactory.getUUIDGen();
    private @NotNull final ModelUUIDGen uuidGenDelegate;
	private @NotNull final Log logger = createLogger();
	private @NotNull final GNDMSVerInfo verInfo = new GNDMSVerInfo();
    /**
     * the GNDMSystemDirectory connected with this GNDMSystem
     */
    private @NotNull GNDMSystemDirectory instanceDir;
	private @NotNull final GridConfig sharedConfig;
	private @NotNull File sharedDir;
    private @NotNull File dbDir;
    private @NotNull File logDir;
	private @NotNull File dbLogFile;
    private @NotNull File containerHome;
	private @NotNull EntityManagerFactory emf;
	private @NotNull EntityManagerFactory restrictedEmf;
//	private NetworkAuxiliariesProvider netAux;


	// Outside injector
	private @NotNull GroovyMoniServer groovyMonitor; // shouldnt be accessed by anyone but system
    private @NotNull TaskExecutionService executionService; // accessible only via system
	private @NotNull ConfigActionCaller actionCaller; // injected by itself





    /**
	 * Retrieves a GNDMSSystem using context.lookup(name).
	 *
	 * A lightweight factory facade is either atomically retrieved from context or bound under name
	 * if name is unbound in context. The factory acts as an intermediary and ensures that at most
	 * one DbSetupFacade ever gets instantiated and initialized.
	 *
	 * This instance is returned by this call from the factory facade.
	 *
	 * @param sharedContext
	 * @param facadeName
	 * @return GNDMSSystem singleton
	 * @throws NamingException
	 */
	@NotNull
	public static GNDMSystem lookupSystem(@NotNull Context sharedContext,
	                                      @NotNull Name facadeName,
	                                      @NotNull GridConfig anySharedConfig,
                                          boolean debugModeParam)
		  throws NamingException {
		try {
			final SysFactory theFactory =
                    new SysFactory(createLogger(), anySharedConfig, debugModeParam);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException ne) {
			return ((SysFactory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

    /**
     * @see #lookupSystem(javax.naming.Context, javax.naming.Name, de.zib.gndms.infra.GridConfig, boolean)
     *
     * @param sharedContext
     * @param facadeName
     * @param anySharedConfig
     * @param debugModeParam
     * @return
     * @throws NamingException
     */
	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	@NotNull
	public static GNDMSystem lookupSystem(
		  @NotNull Context sharedContext, @NotNull String facadeName,
		  @NotNull GridConfig anySharedConfig,
          boolean debugModeParam)
		  throws NamingException {
		try {
			final SysFactory theFactory =
                    new SysFactory(createLogger(), anySharedConfig, debugModeParam);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException ne) {
			return ((SysFactory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	@SuppressWarnings({ "ThisEscapedInObjectConstruction" })
    private GNDMSystem(@NotNull GridConfig anySharedConfig, boolean debugModeParam)  {
		sharedConfig = anySharedConfig;
        debugMode = debugModeParam;
		uuidGenDelegate = new ModelUUIDGen() {
		    public @NotNull String nextUUID() {
		        return GNDMSystem.this.nextUUID();
		    }
		};
	}


	public void initialize() throws RuntimeException {
		try {
			printVersion();
            containerHome = new File(System.getenv("GLOBUS_LOCATION")).getAbsoluteFile();
            logger.info("Container home directory is '" + containerHome.toString() + '\'');
            initSharedDir();
			createDirectories();
			prepareDbStorage();
			emf = createEMF();
			restrictedEmf = emf;
			tryTxExecution();
			// initialization intentionally deferred to initialize
	        instanceDir = new GNDMSystemDirectory(getSystemName(),
	                                              uuidGenDelegate,
	                                              new DefaultWrapper<SystemHolder, Object>(SystemHolder.class) {

		        @Override
		        protected <Y> Y wrapInterfaceInstance(final Class<Y> wrapClass, @NotNull final SystemHolder wrappedParam) {
			        wrappedParam.setSystem(GNDMSystem.this);
			        return wrapClass.cast(wrappedParam);
		        }
	        }, this);
	        instanceDir.addInstance("sys", this);
			instanceDir.reloadConfiglets(restrictedEmf);
			// Bad style, usually would be an inner class but
			// removed it from this source file to reduce source file size
			actionCaller = new ConfigActionCaller(this);
		}
		catch (Exception e) {
			logger.error("Initialization failed", e);
			throw new RuntimeException(e);
		}
	}


    /**
     * Writes information about the GNDMS release and GNDMS build (using {@link #verInfo}) to the logger.
     */
	@SuppressWarnings({ "ValueOfIncrementOrDecrementUsed", "MagicNumber" })
	private void printVersion() {
		final String releaseInfo = verInfo.readRelease();
		final String curBuildInfo = verInfo.readBuildInfo();
		int maxSize = Math.max(releaseInfo.length(), curBuildInfo.length()) + 14;
		final StringBuilder builder = new StringBuilder(maxSize);
		while (maxSize-- >= 0)
			builder.append('=');
		final String hrString = builder.toString();
		logger.warn(hrString);
		logger.warn("GNDMS RELEASE: " + releaseInfo);
		logger.warn("GNDMS BUILD: " + curBuildInfo);
		logger.warn(hrString);
	}


   /**
     * Binds several classes with {@code this} or other corresponding fields
     *
     * @param binder binds several classe with certain fields.
     */
   public void configure(final @NotNull Binder binder) {
       binder.bind(GNDMSystem.class).toInstance(this);
       binder.bind(EntityManagerFactory.class).toInstance(restrictedEmf);
       binder.bind(EMFactoryProvider.class).toInstance(this);
       binder.bind(GridConfig.class).toInstance(sharedConfig);
       //binder.bind(NetworkAuxiliariesProvider.class).toInstance(getNetAux());
       binder.bind(EntityUpdateListener.class).toInstance(this);
       binder.bind(BatchUpdateAction.class).to(DefaultBatchUpdateAction.class);
       binder.bind(UUIDGen.class).toInstance(uuidGen);
       binder.bind(GNDMSVerInfo.class).toInstance(verInfo);
       binder.bind(Log.class).toInstance(logger);
       binder.bind( DirectoryAux.class).toInstance( new LinuxDirectoryAux() );
       // TODO later: binder.bind(TxFrame.class).to(TxFrame.class);
   }

    /**
     * Retrieves the grid path using {@code sharedConfig} and stores the corresponding file in {@code sharedDir}.
     *
     * @throws Exception if an error occurs while creating the {@link File} instance.
     */
	private synchronized void initSharedDir() throws Exception {
        sharedDir = new File(sharedConfig.getGridPath()).getCanonicalFile();
        final String gridName = sharedConfig.getGridName();

        logger.info("Initializing for grid: '" + gridName
              + "' (shared dir: '" + sharedDir.getPath() + "')");

    }

    /**
     * Creates the path {@link #sharedDir} and the file {@link #logDir} on the file system.
     *
     * @throws IOException if an error occurs while accessing the file system
     */
	private void createDirectories() throws IOException {
        File curSharedDir = getSharedDir();

		doCheckOrCreateDir(curSharedDir);

		logDir = new File(curSharedDir, "log");
		doCheckOrCreateDir(logDir);
	}

    /**
     * Create the files {@link #dbDir} and {@link #dbLogFile} on the file system and stores the paths
     * in the system property "derby.system.home" respectively "derby.stream.error.file".
     *
     * @throws IOException if an error occurs while accessing the file system
     */
	@SuppressWarnings({ "ResultOfMethodCallIgnored" })
    private void prepareDbStorage() throws IOException {
        File curSharedDir = getSharedDir();
		dbDir = new File(curSharedDir, "db");
		doCheckOrCreateDir(dbDir);

		System.setProperty("derby.system.home", dbDir.getCanonicalPath());

        if (isDebugging()) {
            LogicTools.setDerbyToDebugMode();
        }

		dbLogFile = new File(logDir, "derby.log");
		if (!dbLogFile.exists())
			dbLogFile.createNewFile();
		System.setProperty("derby.stream.error.file",
				  dbLogFile.getCanonicalPath());
	}

    /**
     * Creates an EntityManagerFactory.
     * 
     * @return an EntityManagerFactory
     * @throws Exception
     */
	@SuppressWarnings({ "ResultOfMethodCallIgnored" })
    public @NotNull EntityManagerFactory createEMF() throws Exception {
		final String gridName = sharedConfig.getGridName();
		final Properties map = new Properties();

		map.put("openjpa.Id", gridName);
		map.put("openjpa.ConnectionURL", "jdbc:derby:" + gridName+";create=true");

        if (isDebugging()) {
            File jpaLogFile = new File(getLogDir(), "jpa.log");
            if (! jpaLogFile.exists())
                jpaLogFile.createNewFile();
            map.put("openjpa.Log", "File=" + jpaLogFile +
                    ", DefaultLevel=INFO, Runtime=TRACE, Tool=INFO");
        }
		logger.info("Opening JPA Store: " + map.toString());

		return createEntityManagerFactory(gridName, map);
	}

    /**
     * Checks if a commit can be done on the database
     *
     * @throws RuntimeException if an error occured while commiting on the database
     */
	private void tryTxExecution() throws RuntimeException{
		final EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
		catch (RuntimeException re)
			{ em.getTransaction().rollback();
              em.close();
              throw re;
        }
		finally
			{ em.close(); }
	}

    
	@SuppressWarnings({ "MethodOnlyUsedFromInnerClass" })
	private synchronized void setupShellService() throws Exception {
		File monitorConfig = new File(sharedDir, "monitor.properties");
		groovyMonitor = new GroovyMoniServer(getGridName(), monitorConfig,
                                             getInstanceDir().createBindingFactory(),
                                             getActionCaller());
		groovyMonitor.startConfigRefreshThread(true);
	}


    /**
     * Checks if the path denoted by <tt>mainDir</tt> exists and may create the path on the filesystem,
     * if not already done.
     *
     * @param mainDir a <tt>File</tt> instance denoting a path
     */
	@SuppressWarnings({ "ResultOfMethodCallIgnored" })
    private void doCheckOrCreateDir(File mainDir) {
		if (!mainDir.exists()) {
			logger.info("Creating " + mainDir.getPath());
			mainDir.mkdir();
		}
		if (!mainDir.isDirectory() || !mainDir.canRead())
			throw new IllegalStateException(mainDir + " is not accessible");
	}


    /**
     * Invokes a shutdown on the ExecutorService {@link #executionService}, on the configlets of the <tt>GNDMSystemDirectory</tt>,
     * stops the <tt>GroovyMoniServer</tt> and closes the EntityManagerFactory {@link #emf}.
     *
     * @throws Exception if an error occurs while stopping the monitorserver
     */
    @SuppressWarnings({
            "MethodOnlyUsedFromInnerClass", "SleepWhileHoldingLock",
            "CallToNativeMethodWhileLocked" })
	private synchronized void shutdown() throws Exception {
        if (! shutdown) {
            shutdown = true;
            if (executionService != null) {
                executionService.shutdown();
            }
	        instanceDir.shutdownConfiglets();
            final GroovyMoniServer moniServer = getMonitor();
            if (moniServer != null)
                moniServer.stopServer();
            emf.close();
        }
	}

    /**
     * Returns the {@code TaskExecutionService}, which is used by this system. If it does not exist, 
     * a new instance will be created.
     *
     * @return the TaskExecutionService instance, which is uses by this system.
     */
    private synchronized @NotNull TaskExecutionService getExecutionService() {
        if (executionService == null)
            executionService = new SysTaskExecutionService();
        return executionService;
    }


    public boolean isDebugging() {
        return debugMode;
    }


    public synchronized @NotNull File getSharedDir() {
        return sharedDir;
    }

	public @NotNull String getSystemName() {
		try { return '\'' + sharedConfig.getGridName() + "' system"; }
		catch (Exception e) { throw new RuntimeException(e); }
	}

    public @NotNull String getGridName() {
        try { return sharedConfig.getGridName(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }


    public @NotNull EntityManagerFactory getEntityManagerFactory() {
        return restrictedEmf;
    }


    public @NotNull
    GNDMSystemDirectory getInstanceDir() {
        return instanceDir;
    }

    public @NotNull ActionCaller getActionCaller() {
        return actionCaller;
    }


	public @NotNull GridConfig getSharedConfig() {
		return sharedConfig;
	}


	public @NotNull File getLogDir() {
		return logDir;
	}


	public @NotNull File getDbDir() {
		return dbDir;
	}


	public @NotNull File getDbLogFile() {
		return dbLogFile;
	}


	public synchronized @Nullable GroovyMoniServer getMonitor() {
		return groovyMonitor;
	}

	@SuppressWarnings({"ReturnOfThis"})
	public @NotNull GNDMSystem getSystem() {
		return this;
	}

	public void setSystem(@NotNull GNDMSystem system) throws IllegalStateException {
		throw new IllegalStateException("Cant set this system");
	}

    /**
     * Returns the next UUID using {@link #uuidGen}
     * 
     * @return the next UUID
     */
	public @NotNull String nextUUID() {
		return uuidGen.nextUUID();
	}


	public static @NotNull EndpointReferenceType serviceEPRType(@NotNull URI defAddr, @NotNull VEPRef dSpaceRef)
		  throws URI.MalformedURIException {
		if (dSpaceRef.getGridSiteId() != null)
			throw new IllegalArgumentException("Non-local EPRTs currently unsupported");

		try {
			return AddressingUtils.createEndpointReference(
				  defAddr.toString(), null);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public @NotNull EndpointReferenceType serviceEPRType(@NotNull String instPrefix,
	                                           @NotNull VEPRef dSpaceRef)
		  throws URI.MalformedURIException {
		return serviceEPRType(
                getInstanceDir().lookupServiceHome(instPrefix).getServiceAddress(), dSpaceRef);
	}


	public static @NotNull VEPRef modelEPRT(@NotNull QName keyTypeName,
                                            @NotNull EndpointReferenceType epr) throws NotSupportedException{
		@NotNull ReferencePropertiesType props = epr.getProperties();
		@NotNull MessageElement msgElem = props.get(keyTypeName);
		SimpleResourceKey key = new SimpleResourceKey(keyTypeName, msgElem.getObjectValue());

		// theVEPREF.setSite("");
		// theVEPREF.setRk(key);
        throw new NotSupportedException();
		// return new DSpaceRef();
	}


	public @NotNull VEPRef modelEPRT(@NotNull String instPrefix,
                                     @NotNull EndpointReferenceType epr) throws NotSupportedException{
		return modelEPRT(getInstanceDir().lookupServiceHome(instPrefix).getKeyTypeName(), epr);
	}


    public void onModelChange( GridResource model ) {
        try {
            onModelChange_(model);
        } catch ( ResourceException e ) {
            logger.warn(e);
        }
    }

    /**
     * Invokes <tt>refresh(model)</tt> on the <tt>GNDMPersistentServiceHome</tt> corresponding to the model class.
     * (see {@link de.zib.gndms.infra.system.GNDMSystemDirectory#addHome(Class, de.zib.gndms.infra.service.GNDMServiceHome)} )
     *
     * @param model the new model
     * @param <M> the class of the model
     * @throws ResourceException
     */
    @SuppressWarnings({ "unchecked" })
    private <M extends GridResource> void onModelChange_(final M model) throws ResourceException {
        final Class<M> modelClazz = (Class<M>) model.getClass();
        GNDMPersistentServiceHome<M> home = getInstanceDir().getHome(modelClazz);
        home.refresh(model);
    }

    /**
     * Returns a list of all {@code home}'s {@link org.globus.wsrf.Resource}s, managed by {@code em}.
     *
     * @param home a {@link org.globus.wsrf.ResourceHome} managing {@code Resources}
     * @param em the entityManager, on which the query will be done
     * @param <M> the model type
     * @return a list of all {@code home}'s {@link org.globus.wsrf.Resource}s, managed by {@code em}.
     */
    @SuppressWarnings({ "unchecked", "MethodMayBeStatic" })
    public @NotNull <M extends GridResource> List<String> listAllResources(
            final @NotNull GNDMPersistentServiceHome<M> home, final @NotNull EntityManager em) {
        Query query = home.getListAllQuery(em);
        return query.getResultList();
    }

    /**
     * Returns a list of all {@link org.globus.wsrf.Resource}s, which are managed by an EntityManager, corresponding to
     * the given EntityManagerFactory and
     * which are managed by the {@code GNDMPersistentServiceHome} {@link de.zib.gndms.infra.system.GNDMSystemDirectory#getHome(Class)}    
     *
     * @param emg the factory to create the EntityManager, used for the {@code list all} query
     * @param clazz the class, whose corresponding GNDMPersistentServiceHome will be used for the query.(See {@link de.zib.gndms.infra.system.GNDMSystemDirectory#homes})
     * @param <M> the model type
     * @return  a list of all {@code Resources}, corresponding to a specific EntityManager and GNDMPersistentServiceHome.
     */
    public final @NotNull  <M extends GridResource> List<String> listAllResources(
            final @NotNull EntityManagerFactory emg, final @NotNull Class<M> clazz) {
        final EntityManager manager = emg.createEntityManager();
        List<String> retList = null;
        try {
            try {
                manager.getTransaction().begin();
                retList = listAllResources(getInstanceDir().getHome(clazz), manager);
                manager.getTransaction().commit();
            }
            finally {
                if (manager.getTransaction().isActive())
                    manager.getTransaction().rollback();
            }
        }
        finally {
            if (manager.isOpen())
                manager.close();
        }
        return retList;
    }

    /**
     * Refreshes all resources corresponding to a specific GNDMPersistentServiceHome
     * @param home the GNDMPersistentServiceHome,whose Resource will be refreshed
     * @param <M>  the model type
     */
    @SuppressWarnings({ "ConstantConditions" })
    public final <M extends GridResource> void refreshAllResources(
            final @NotNull GNDMPersistentServiceHome<M> home) {
        final EntityManager manager = home.getEntityManagerFactory().createEntityManager();
        try {
            try {
                manager.getTransaction().begin();
                for (String id : listAllResources(home, manager)) {
                    try {
                        if (isDebugging())
                            logger.debug("Restoring " + home.getNickName() + ':' + id);
                        home.find(home.getKeyForId(id));
                    }
                    catch (ResourceException e) {
                        logger.warn(e);
                    }
                }
                manager.getTransaction().commit();
            }
            finally {
                if (manager.getTransaction().isActive())
                    manager.getTransaction().rollback();
            }
        }
        finally {
            if (manager.isOpen())
                manager.close();
        }
    }


    /**
     * Returns {@code this}
     *
     * @return {@code this}
     */
    @SuppressWarnings({ "ReturnOfThis" })
    public EntityUpdateListener<GridResource> getEntityUpdateListener() {
        return this;
    }


    /**
     * Returns {@link #uuidGenDelegate}
     *
     * @return {@link #uuidGenDelegate}
     */
    public @NotNull ModelUUIDGen getModelUUIDGen() {
        return uuidGenDelegate;
    }


    /**
     * Calls {@code sumitAction(action,logParam)} on {@code getExecutionService()}.
     *
     * @see SysTaskExecutionService#submitAction(de.zib.gndms.logic.model.EntityAction, org.apache.commons.logging.Log)
     */
    public @NotNull <R> Future<R> submitAction(final @NotNull EntityAction<R> action, final @NotNull Log logParam) {
        return getExecutionService().submitAction(action, logParam);
    }

    /**
     * Calls {@code sumitAction(em,action,logParam)} on {@code getExecutionService()}
     * 
     * @see SysTaskExecutionService#submitAction(javax.persistence.EntityManager, de.zib.gndms.logic.model.EntityAction, org.apache.commons.logging.Log) 
     *
     */
    public @NotNull <R> Future<R> submitAction(final @NotNull EntityManager em,
                                               final @NotNull EntityAction<R> action,
                                               final @NotNull Log logParam) {
        return getExecutionService().submitAction(em, action, logParam);
    }


	public void reloadConfiglets() {
		instanceDir.reloadConfiglets(getEntityManagerFactory());
	}


    /**
     * A SysTaskExecutionService submits {@link EntityAction}s to an {@link ExecutorService}.
     *
     * Before the action is submitted to the executor, using a suitable {@code submitAction(..)} method,
     * {@link #submit_(de.zib.gndms.logic.model.EntityAction, org.apache.commons.logging.Log)} will automatically
     * prepare the action using certain setters.
     *
     * When the executor is shutted down, using {@link #shutdown()},
     * the system will wait {@link de.zib.gndms.infra.system.GNDMSystem#EXECUTOR_SHUTDOWN_TIME} milliseconds.
     *
     */
	public final class SysTaskExecutionService implements TaskExecutionService, ThreadFactory {
        private final ThreadPoolExecutor executorService;
        private volatile boolean terminating;


        /**
         * Initializes the ExecutorService
         * 
         */
        public SysTaskExecutionService() {
            super();
            executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            executorService.prestartCoreThread();
        }


        public ExecutorService getExecutorService() {
            return executorService;
        }

        /**
         * Returns whether {@link #shutdown()} has already been invoked or not.
         *
         * @return whether {@link #shutdown()} has already been invoked or not.
         */
        public boolean isTerminating() {
            return terminating;
        }


        /**
         * Stopps all currently executed and waiting tasks on the Executor {@code getExecutorService()}.
         * Sets {@link #terminating} to {@code true}.
         * The method waits {@link GNDMSystem#EXECUTOR_SHUTDOWN_TIME} milliseconds until it returns.
         *
         */
        @SuppressWarnings({ "BusyWait" })
        public void shutdown() {
            terminating = true;
            getExecutorService().shutdownNow();
            /* truely sleep for EXECUTOR_SHUTDOWN_TIME */
            long now = System.currentTimeMillis();
            long stop = now + EXECUTOR_SHUTDOWN_TIME;
            do {
                try {
                    sleep(stop - now);
                }
                catch (InterruptedException e) {
                    // intentionally
                }
                now = System.currentTimeMillis();
            }
            while (stop - now > 0L);

        }


        public final @NotNull <R> Future<R> submitAction(final @NotNull EntityAction<R> action, final @NotNull Log log) {
            final EntityManager ownEm = action.getOwnEntityManager();
            if (ownEm != null)
                return submit_(action, log);
            else {
                final @NotNull EntityManager em = getEntityManagerFactory().createEntityManager();
                return submitAction(em, action, log);
            }
        }

        @SuppressWarnings({ "FeatureEnvy" })
        public @NotNull <R> Future<R> submitAction(final @NotNull EntityManager em,
                                                   final @NotNull EntityAction<R> action,
                                                   final @NotNull Log log) {
            action.setOwnEntityManager(em);
            return submit_(action, log);
        }

        /**
         * Prepares {@code action} using certain setters, before it is submitted to the Executor.
         *
         * <p>
         * If {@code action} is a
         * <ul>
         *  <li>{@link LogAction}, its logger will be set to {@code log} </li>
         *  <li>{@link SystemHolder}, the GNDMSystem ({@code GNDMSystem.this}) will be stored</li>
         *  <li>{@link AbstractEntityAction}, its UUID generator will be set to {@link GNDMSystem#uuidGenDelegate}</li>
         *  <li>{@link TaskAction}, its service will be set to {@code this}.</li>
         * </ul>
         *
         * If {@code action} does not already have postponed actions,
         * they are set to a new {@link de.zib.gndms.logic.model.DefaultBatchUpdateAction}.
         * If {@code action} does not already have listerns for the postponed actions, 
         * they are set to {@link GNDMSystem#getEntityUpdateListener()}.
         *
         * @param action the EntityAction which should be executed
         * @param log A logger, which can be added to the action, if it's a LogAction
         * @param <R> the return type of the action
         * @return A Future Object holding the result of action's computation
         */
        @SuppressWarnings({ "FeatureEnvy" })
        private <R> Future<R> submit_(final EntityAction<R> action, final @NotNull Log log) {
            if (action instanceof LogAction)
                ((LogAction)action).setLog(log);
            if (action instanceof SystemHolder)
                ((SystemHolder)action).setSystem(GNDMSystem.this);
            if (action.getPostponedActions() == null)
                action.setOwnPostponedActions(new DefaultBatchUpdateAction<GridResource>());
            if (action.getPostponedActions().getListener() == null)
                action.getPostponedActions().setListener(getEntityUpdateListener());
            if (action instanceof AbstractEntityAction)
                ((AbstractEntityAction<?>)action).setUUIDGen(uuidGenDelegate);
            if (action instanceof TaskAction) {
                final TaskAction taskAction = (TaskAction) action;
                taskAction.setService(this);
            }
            return getExecutorService().submit(action);
        }


        public Thread newThread(final Runnable r) {
            return Executors.defaultThreadFactory().newThread(r);
        }
    }

    /**
     * A factory class for the <tt>GNDMSystem</tt>.
     *
     *
     * 
     * @see de.zib.gndms.infra.system.GNDMSystem
     */
    public static final class SysFactory {
		private final Log logger;

		private GNDMSystem instance;
		private RuntimeException cachedException;
		private GridConfig sharedConfig;
        private boolean debugMode;

		public SysFactory(
                @NotNull Log theLogger, @NotNull GridConfig anySharedConfig,
                final boolean debugModeParam) {
			logger = theLogger;
			sharedConfig = anySharedConfig;
            debugMode = debugModeParam;
		}

        /**
         * Calls {@code getInstance(true)}
         * 
         * @return
         */
		public synchronized GNDMSystem getInstance() {
			return getInstance(true);
		}

        /**
         * Returns the current GNDMSystem if it has already been created.
         * Otherwise a new instance will be created.
         *
         * <p>The system will be loaded with the values set in the fields {@link #sharedConfig} and {@link #debugMode}.
         * If <tt>setupShellService</tt> is set to <tt>true</tt>, {@code setupShellService()} will be invoked on the new
         * system. The new created system will be stored at {@link #instance}.
         *
         * @param setupShellService a boolean to decide whether setupShellService() is invoked on a new GNDM System or not
         *
         * @return the current used GNDM system
         */
		public synchronized GNDMSystem getInstance(boolean setupShellService) {
			if (cachedException != null)
				throw cachedException;
			if (instance == null) {
				try {
					GNDMSystem newInstance = new GNDMSystem(sharedConfig, debugMode);
					newInstance.initialize();
					try {
						if (setupShellService)
							newInstance.setupShellService();
                        logger.debug("GNDMS_DEBUG "
                                + (sharedConfig.isDebugMode() ? "true" : "false"));
					}

					catch (Exception e) {
						throw new RuntimeException(e);
					}
					try { logger.info(sharedConfig.getGridName() + " initialized"); }
					catch (Exception e) { logger.error(e); }
					instance = newInstance;
				}
				catch (RuntimeException e) {
					cachedException = e;
                    throw cachedException;
				}
				finally {
					// not required after this point
					sharedConfig = null;
				}
			}
			return instance;
		}

        /**
         * Shuts down the current GNDM system by invoking {@code shutdown()} on {@link #instance}.
         *
         * @throws Exception if an error occurs during the initialization or setup of the shell service of the GNDM system
         */
		@SuppressWarnings({ "MethodOnlyUsedFromInnerClass" })
		private synchronized void shutdown() throws Exception {
			if (instance == null)
				return;
			else
				getInstance().shutdown();
		}


        /**
         * Returns a <tt>Runnable</tt> which calls {@link #shutdown()}, when it is executed
         * 
         * @return a <tt>Runnable</tt> which calls {@link #shutdown()}, when it is executed
         */
		public @NotNull Runnable createShutdownAction() {
			return new Runnable() {
				public void run() {
					try {
						shutdown();
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}

	}

    @NotNull
    public File getContainerHome() {
        return containerHome;
    }

    public boolean isGridAdmin(final String tag, final String dn) {
        final boolean ret = isGridAdmin_(dn == null ? "null" : dn);
        NDC.push(tag);
        try {
            if (ret)
                logger.info("Authenticated Grid Admin " + dn);
            else
                logger.info("Denied Access to Non-Grid Admin " + dn);

            return ret;
        }
        finally { NDC.pop(); }
    }

    private boolean isGridAdmin_(final String dn) {
        // This only works on UNIX
        for (final File gridAdminFile : new File[] {
                new File(File.separatorChar + "etc" + File.separatorChar + "grid-security" + File.separatorChar + getInstanceDir().getSubGridName() + "-support-staff"),
                new File(getSharedDir(), getInstanceDir().getSubGridName() + "-support-staff")
        })
            if (isDNInFile(dn, gridAdminFile))
                return true;

        return false;
    }

    private boolean isDNInFile(final String dn, final File file) {
        logger.debug("Checking if '" + dn + "' is in '" + file.getAbsolutePath() + '\'');
        if (! (file.isFile() && file.exists() && file.canRead()))
            return false;

        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(file));
            try {
                String line;

                while ((line = rd.readLine()) != null) {
                    String trLine = line.trim();
                    if (trLine.length() > 0 && trLine.charAt(0) == '\"') {
                        int endIndex = trLine.lastIndexOf('\"');
                        if (endIndex > 0)
                            trLine = trLine.substring(1, endIndex).trim();
                    }
                    if (trLine.equals(dn))
                        return true;
                }
            }
            finally { rd.close(); }
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
    /*
    public NetworkAuxiliariesProvider getNetAux( ) {

        if( netAux == null )
            netAux = new NetworkAuxiliariesProvider( );

        return netAux;
    }
    */
}
