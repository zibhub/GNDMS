package de.zib.gndms.infra.system;

import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.db.EMFactoryProvider;
import de.zib.gndms.infra.db.EMTools;
import de.zib.gndms.infra.db.RestrictedEMFactory;
import de.zib.gndms.infra.monitor.GroovyBindingFactory;
import de.zib.gndms.infra.monitor.GroovyMoniServer;
import de.zib.gndms.infra.service.ServiceInfo;
import de.zib.gndms.model.common.VEPRef;
import de.zib.gndms.model.dspace.DSpaceRef;
import de.zib.gndms.model.util.InstanceResolver;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.ReferencePropertiesType;
import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.jndi.Initializable;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static javax.persistence.Persistence.createEntityManagerFactory;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This sets up the configuration and database storage area shared between
 * GNDMS services.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:09:00
 */
@SuppressWarnings({"OverloadedMethodsWithSameNumberOfParameters", "NestedAssignment"})
public final class GNDMSystem
	  implements Initializable, SystemHolder, InstanceResolver<Object>, EMFactoryProvider {
	private final UUIDGen uuidGen = UUIDGenFactory.getUUIDGen();

	private final Log logger = createLogger();

	@NotNull
	private static Log createLogger() {return LogFactory.getLog(GNDMSystem.class);}


	@NotNull
	private final Map<String, Object> instances;

	@NotNull
	private final GridConfig sharedConfig;

	@NotNull
	private File sharedDir;

	@NotNull
	private File logDir;

	@NotNull
	private File dbDir;

	@NotNull
	private File dbLogFile;

	@NotNull
	private EntityManagerFactory emf;

	@NotNull
	private EntityManagerFactory restrictedEmf;

	@NotNull
	private File monitorConfig;

	@NotNull
	private GroovyMoniServer groovyMonitor;

	private static final int INITIAL_CAPACITY = 32;
	private static final int INSTANCE_RETRIEVAL_INTERVAL = 250;

	/**
	 * Retrieves a GNDMSSystem using context.lookup(name).
	 *
	 * A lightweight factory facade is either atomically retrieved from context or bound under name
	 * iff name is unbound in context. The factory acts as an intermediary and ensures that at most
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
	                                        @NotNull GridConfig anySharedConfig)
		  throws NamingException {
		try {
			final SysFactory theFactory = new SysFactory(createLogger(), anySharedConfig);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((SysFactory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	@NotNull
	public static GNDMSystem lookupSystem(
		  @NotNull Context sharedContext, @NotNull String facadeName,
		  @NotNull GridConfig anySharedConfig)
		  throws NamingException {
		try {
			final SysFactory theFactory = new SysFactory(createLogger(), anySharedConfig);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((SysFactory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	private GNDMSystem(@NotNull GridConfig anySharedConfig)  {
		sharedConfig = anySharedConfig;
		instances = new HashMap<String, Object>(INITIAL_CAPACITY);
		addInstance("sys", this);
		// initialization intentionally deferred to initialize
	}

	public void initialize() throws RuntimeException {
		try {
			// Q: Think about how to correct UNIX directory/file permissions from java-land
			// A: External script during deployment
			sharedDir = new File(sharedConfig.getGridPath()).getCanonicalFile();
			final String gridName = sharedConfig.getGridName();

			logger.info("Initializing for grid: '" + gridName
				  + "' (shared dir: '" + sharedDir.getPath() + "')");

			createDirectories();
			emf = createEMF();
			restrictedEmf = new RestrictedEMFactory(emf);
			EMTools.initialize();
			tryTxExecution();
		}
		catch (Exception e) {
			logger.error("Initialization failed", e);
			throw new RuntimeException(e);
		}
	}


	@SuppressWarnings({ "MethodOnlyUsedFromInnerClass" })
	private void shutdown() throws Exception {
		emf.close();
		groovyMonitor.stopServer();
	}

	private void createDirectories() throws IOException {
		doCheckOrCreateDir(sharedDir);

		logDir = new File(sharedDir, "log");
		doCheckOrCreateDir(logDir);

		prepareDbStorage();

		monitorConfig = new File(sharedDir, "monitor.properties");
	}

	private void prepareDbStorage() throws IOException {
		dbDir = new File(sharedDir, "db");
		doCheckOrCreateDir(dbDir);

		System.setProperty("derby.system.home", dbDir.getCanonicalPath());

		dbLogFile = new File(logDir, "derby.log");
		if (!dbLogFile.exists())
			dbLogFile.createNewFile();
		System.setProperty("derby.stream.error.file",
				  dbLogFile.getCanonicalPath());
	}

	@NotNull
	public EntityManagerFactory createEMF() throws Exception {
		final String gridName = sharedConfig.getGridName();
		final Properties map = new Properties();

		map.put("openjpa.Id", gridName);
		map.put("openjpa.ConnectionURL", "jdbc:derby:" + gridName+";create=true");
		logger.info("Opening JPA Store: " + map.toString());

		return createEntityManagerFactory(gridName, map);
	}

	private void tryTxExecution() {
		final EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
		catch (RuntimeException re)
			{ em.getTransaction().rollback(); }
		finally
			{ em.close(); }
	}

	@SuppressWarnings({ "MethodOnlyUsedFromInnerClass" })
	private void setupShellService() throws Exception {
		groovyMonitor = new GroovyMoniServer(getGridName(),
		                                     monitorConfig, new GNDMSBindingFactory());
		groovyMonitor.startConfigRefreshThread(true);
	}

	public synchronized void addInstance(@NotNull String name, @NotNull Object obj) {
		if ("out".equals(name) || "err".equals(name) || "args".equals(name) || "em".equals(name)
			|| "emg".equals(name))
			throw new IllegalArgumentException("Reserved instance name");

		if (instances.containsKey(name))
			throw new IllegalStateException("Name clash in instance registration");
		else
			instances.put(name, obj);

		logger.debug(getSystemName() + " addInstance: '" + name + '\'');
	}
	
	@NotNull
	public synchronized <T> T getInstance(@NotNull Class<? extends T> clazz, @NotNull String name)
	{
		final Object obj = instances.get(name);
		if (obj == null)
			throw new
				  IllegalStateException("Null instance retrieved or invalid or unregistered name");
		return clazz.cast(obj);
	}

	public synchronized ServiceInfo lookupServiceInfo(@NotNull String instancePrefix) {
		return getInstance(ServiceInfo.class, instancePrefix+"Home");
	}


	private void doCheckOrCreateDir(File mainDir) {
		if (!mainDir.exists()) {
			logger.info("Creating " + mainDir.getPath());
			mainDir.mkdir();
		}
		if (!mainDir.isDirectory() || !mainDir.canRead())
			throw new IllegalStateException(mainDir + " is not accessible");
	}

	@NotNull
	public String getSystemName() {
		try {
			return '\'' + sharedConfig.getGridName() + "' system";
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	public GridConfig getSharedDir() {
		return sharedConfig;
	}

	@NotNull
	public File getLogDir() {
		return logDir;
	}

	@NotNull
	public File getDbDir() {
		return dbDir;
	}

	@NotNull
	public File getDbLogFile() {
		return dbLogFile;
	}

	@NotNull
	public GroovyMoniServer getMonitor() {
		return groovyMonitor;
	}

	@SuppressWarnings({"ReturnOfThis"})
	@NotNull
	public GNDMSystem getSystem() {
		return this;
	}

	public void setSystem(@NotNull GNDMSystem system) throws IllegalStateException {
		throw new IllegalStateException("Cant set this system");
	}

	@NotNull
	public String nextUUID() {
		return uuidGen.nextUUID();
	}

	@NotNull
	public static EndpointReferenceType serviceEPRType(@NotNull URI defAddr, @NotNull VEPRef dSpaceRef)
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

	@NotNull
	public EndpointReferenceType serviceEPRType(@NotNull String instPrefix,
	                                           @NotNull VEPRef dSpaceRef)
		  throws URI.MalformedURIException {
		return serviceEPRType(lookupServiceInfo(instPrefix).getServiceAddress(), dSpaceRef);
	}

	@NotNull
	public static VEPRef modelEPRT(@NotNull QName keyTypeName, @NotNull EndpointReferenceType epr) {
		@NotNull ReferencePropertiesType props = epr.getProperties();
		@NotNull MessageElement msgElem = props.get(keyTypeName);
		SimpleResourceKey key = new SimpleResourceKey(keyTypeName, msgElem.getObjectValue());

		// theVEPREF.setSite("");
		// theVEPREF.setRk(key);
		return new DSpaceRef();
	}

	@NotNull
	public VEPRef modelEPRT(@NotNull String instPrefix, @NotNull EndpointReferenceType epr) {
		return modelEPRT(lookupServiceInfo(instPrefix).getResourceKeyTypeName(), epr);
	}

	@NotNull
	public <T> T retrieveInstance(@NotNull Class<T> clazz, @NotNull String name) {
		T instance;
		try { instance = getInstance(clazz, name); }
		catch (IllegalStateException e) { instance = null; }
		while (instance != null) {
			try {
				Thread.sleep(INSTANCE_RETRIEVAL_INTERVAL);
			}
			catch (InterruptedException e) {
				// inteded
			}
			try { instance = getInstance(clazz, name); }
			catch (IllegalStateException e) { instance = null; }
		}
		return instance;
	}

	public String getGridName() {
		try {
			return sharedConfig.getGridName();
		}
		catch (Exception e) {
			// Dont know what to do here
			throw new RuntimeException(e);
		}
	}


	@NotNull
	public EntityManagerFactory getEntityManagerFactory() {
		return restrictedEmf;
	}


	public static final class SysFactory {
		private final Log logger;

		private GNDMSystem instance;
		private RuntimeException cachedException;
		private GridConfig sharedConfig;

		public SysFactory(@NotNull Log theLogger, @NotNull GridConfig anySharedConfig) {
			logger = theLogger;
			sharedConfig = anySharedConfig;
		}

		public synchronized GNDMSystem getInstance() {
			if (cachedException != null)
				throw cachedException;
			if (instance == null) {
				try {
					GNDMSystem newInstance = new GNDMSystem(sharedConfig);
					newInstance.initialize();
					try {
						newInstance.setupShellService();
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
				}
				finally {
					// not required after this point
					sharedConfig = null;
				}
			}
			return instance;
		}


		@SuppressWarnings({ "MethodOnlyUsedFromInnerClass" })
		private synchronized void shutdown() throws Exception {
			if (instance == null)
				return;
			else
				getInstance().shutdown();
		}

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

	private final class GNDMSBindingFactory implements GroovyBindingFactory {
		@NotNull
		public Binding createBinding(
			  final @NotNull GroovyMoniServer moniServer,
		      final @NotNull Principal principal, final @NotNull String args) {
			final Binding binding = new Binding();
			for (Map.Entry<String, Object> entry : instances.entrySet())
				binding.setProperty(entry.getKey(), entry.getValue());
			return binding;
		}

		@SuppressWarnings({"StringBufferWithoutInitialCapacity"})
		public void initShell(@NotNull GroovyShell shell, @NotNull Binding binding) {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<String, Object> entry : instances.entrySet()) {
				final String key = entry.getKey();
				builder.append("Object.metaClass.");
				builder.append(key);
				builder.append('=');
				builder.append(key);
				builder.append(';');
			}
			shell.evaluate(builder.toString());
		}


		public void destroyBinding(final @NotNull GroovyMoniServer moniServer,
		                           final @NotNull Binding binding) {
			// intended
		}
	}

}
