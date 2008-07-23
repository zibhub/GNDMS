package de.zib.gndms.infra.db;

import de.zib.gndms.infra.GNDMSConfig;
import de.zib.gndms.infra.monitor.GroovyBindingFactory;
import de.zib.gndms.infra.monitor.GroovyMoniServer;
import groovy.lang.Binding;
import org.apache.log4j.Logger;
import org.globus.wsrf.jndi.Initializable;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public final class DbSetupFacade implements Initializable {
	private static final Lock factoryLock = new ReentrantLock();

	private final Logger logger = Logger.getLogger(DbSetupFacade.class);

	@NotNull
	private final GNDMSConfig sharedConfig;

	@NotNull
	private EntityManagerFactory emf;

	@NotNull
	private File dbDir;

	@NotNull
	private File logDir;

	@NotNull
	private File dbLogFile;

	@NotNull
	private File sharedDir;

	@NotNull
	private File monitorConfig;

	@NotNull
	private GroovyMoniServer gromi;

//	@Nullable
//	private GroovyShellService shellService;

	private DbSetupFacade(@NotNull GNDMSConfig anySharedConfig)  {
		sharedConfig = anySharedConfig;
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
			emf = createEMF(gridName);
			tryTxExecution();

			setupShellService(gridName);
		}
		catch (Exception e) {
			logger.error("Initialization failed");
			throw new RuntimeException(e);
		}
	}


	private void setupShellService(String gridName) throws Exception {
		gromi = new GroovyMoniServer(gridName,  monitorConfig,
			  new GroovyBindingFactory() {

			@NotNull
			public Binding createBinding(
				  @NotNull GroovyMoniServer moniServer, @NotNull Principal principal,
				  String args) {
				return new Binding();
			}
		});
		gromi.startConfigRefreshThread(true);
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

	private void tryTxExecution() {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			EntityTransaction tx;
			tx = em.getTransaction();
			tx.begin();
			tx.commit();
		}
		finally {
			if (em != null)
				em.close();
		}
	}

	public EntityManagerFactory createEMF(String gridName) {
		Properties map = new Properties();
		map.put("openjpa.Id", gridName);
		map.put("openjpa.ConnectionDriverName", "org.apache.derby.jdbc.EmbeddedDriver");
		map.put("openjpa.ConnectionURL", "jdbc:derby:" + gridName);

		Properties driverProps = new Properties();
		driverProps.setProperty("create", "true");

		map.put("openjpa.ConnectionProperties", driverProps.toString());
		map.put("openjpa.jdbc.DBDictionary", "derby(JoinSyntax=sql92)");
		map.put("openjpa.slice.ConnectionRetainMode", "on-demand");
		map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		map.put("openjpa.jdbc.TransactionIsolation", "serializable");

		logger.info("Opening JPA Store: " + map.toString());

		return Persistence.createEntityManagerFactory(null, map);
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
	public EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Retrieves a DbSetupFacade using context.lookup(name).
	 *
	 * A lightweight factory facade is either atomically retrieved from context or bound under name
	 * iff name is unbound in context. The factory acts as an intermediary and ensures that at most
	 * one DbSetupFacade ever gets instantiated and initialized.
	 *
	 * This instance is returned by this call from the factory facade.
	 *
	 * @param sharedContext
	 * @param facadeName
	 * @return DbSetupFacade singleton
	 * @throws NamingException
	 */
	@NotNull
	public static DbSetupFacade lookupInstance(@NotNull Context sharedContext,
	                                        @NotNull Name facadeName,
	                                        @NotNull GNDMSConfig anySharedConfig)
		  throws NamingException {
		try {
			final Factory theFactory = new Factory(anySharedConfig);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((Factory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
	@NotNull
	public static DbSetupFacade lookupInstance(
		  @NotNull Context sharedContext, @NotNull String facadeName,
		  @NotNull GNDMSConfig anySharedConfig)
		  throws NamingException {
		try {
			final Factory theFactory = new Factory(anySharedConfig);
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((Factory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	@NotNull
	public GNDMSConfig getSharedDir() {
		return sharedConfig;
	}

	@NotNull
	public File getDbDir() {
		return dbDir;
	}

	@NotNull
	public File getLogDir() {
		return logDir;
	}

	@NotNull
	public File getDbLogFile() {
		return dbLogFile;
	}

	@NotNull
	public GroovyMoniServer getMonitor() {
		return gromi;
	}

	private static final class Factory {
		private DbSetupFacade instance;
		private RuntimeException exception;
		private GNDMSConfig sharedConfig;

		Factory(GNDMSConfig anySharedConfig) {
			sharedConfig = anySharedConfig;
		}

		private DbSetupFacade getInstance() {
			factoryLock.lock();
			try {
				if (exception != null)
					throw exception;
				if (instance == null) {
					try {
						DbSetupFacade newInstance = new DbSetupFacade(sharedConfig);
						newInstance.initialize();
						instance = newInstance;
					}
					catch (RuntimeException e) {
						exception = e;
					}
					finally {
						// not required after this point
						sharedConfig = null;
					}
				}
				return instance;
			}
			finally {
				factoryLock.unlock();
			}
		}
	}
}
