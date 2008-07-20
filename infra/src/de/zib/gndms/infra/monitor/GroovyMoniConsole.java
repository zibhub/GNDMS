package de.zib.gndms.infra.monitor;

import de.zib.gndms.infra.InfiniteEnumeration;
import de.zib.gndms.infra.PropertiesFromFile;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.SessionIdManager;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.*;
import org.mortbay.thread.BoundedThreadPool;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple management console that runs a groovy shell over http using
 * jetty.  Plain text authentication with a single username:password is used
 * to limit console access.
 *
 * If an instance is executed in a thread, it tries to reread the configruation file
 * in regular intervals and restarts the server iff any change is found.
 *
 * The config file is a plain java properties file. All properties start with
 * "monitor.". Existing properies are: host (for selecting the socket interface),
 * port, enabled ("true" or "false"), user, password (cleartext), and refreshCycle.
 *
 * If no config file is found, a default file is created.  The console is always disabled
 * by default and configured for localhost:23232 with an unguessable default password and
 * a refresh cycle of 30 seconds.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.07.2008 Time: 14:33:37
 */
@SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "FieldCanBeLocal"})
public class GroovyMoniConsole implements Runnable {
	private static final Logger logger;
	private static final Properties DEFAULT_PROPERTIES;
	private static final String DEFAULT_COMMENT;

	static {
		logger = Logger.getLogger(GroovyMoniConsole.class);

		DEFAULT_COMMENT = "Default properties for monitoring console. "
	   +"Set monitor.enabled=true to access via "
	   +"http://<host>:<port>/ using plain text authentication "
	   +"(all connections are *unencrpyted*)";

		Properties props = new Properties();
		props.put("monitor.enabled", "true");
		props.put("monitor.host", "localhost");
		props.put("monitor.port", "23232");
		props.put("monitor.user", "admin");
		// random, unguessable default password
		props.put("monitor.password", "fonsy"); // UUID.randomUUID().toString());
		props.put("monitor.refreshCycle", "30000");
		props.put("monitor.maxScriptSize", "65535");
		props.put("monitor.maxConnections", "8");
		props.put("monitor.minConnections", "5");
		props.put("monitor.roleName", "moniadmin");
		props.put("monitor.defaultMode", "SCRIPT");
		props.put("monitor.maxIdleTime", "5000");

		DEFAULT_PROPERTIES = props;
	}


	@NotNull
	final String unitName;

	@NotNull
	private final InfiniteEnumeration<? extends Map<Object, Object>> configStream;

	@Nullable
	private BindingFactory bindingFactory;

	private boolean enabled; // = false initially

	@NotNull
	private String host;

	private int port;

	@NotNull
	private String user;

	@NotNull
	private String password;

	@NotNull
	private GroovyMoniSession.RunMode defaultMode;

	private long refreshCycle;

	private int maxIdleTime;

	private int maxScriptSize;

	private int maxConnections;
	private int minConnections;

	@NotNull

	private String roleName;

	@Nullable
	private Server server;


	private ReentrantLock lock = new ReentrantLock(true);
	private Thread thread;
	private boolean skip;

	/**
	 * Creates a facade class for running a monitoring console server based on
	 * groovy and jetty.
	 *
	 * @param theUnitName descriptive name for this server (used in logs)
	 * @param theConfig continuous stream of properties for this server
	 * @param theBindingFactory for creating groovy biding objects per console instance/connection
	 */
	public GroovyMoniConsole(@NotNull String theUnitName,
	                       @NotNull InfiniteEnumeration<? extends Map<Object,Object>> theConfig,
	                       @Nullable BindingFactory theBindingFactory) {

		unitName = theUnitName;
		bindingFactory = theBindingFactory;
		configStream = theConfig;
		setupState(configStream.nextElement());
	}

	/**
	 * Creates a facade class for running a monitor server based on
	 * groovy and jetty.
	 *
	 * @param theUnitName descriptive name for this server (used in logs)
	 * @param theConfigFile used to load server configuration
	 * @param theBindingFactory for creating groovy biding objects per console instance/connection
	 * @throws Exception
	 */
	@SuppressWarnings({"MagicNumber"})
	public GroovyMoniConsole(@NotNull String theUnitName, @NotNull File theConfigFile,
	                       @Nullable BindingFactory theBindingFactory) throws Exception
	{
		this(theUnitName,
			 new PropertiesFromFile(theConfigFile, theUnitName + " monitor config",
			    DEFAULT_PROPERTIES, DEFAULT_COMMENT, logger), theBindingFactory);
		logger.info(theUnitName + " monitor is " + theConfigFile.getCanonicalPath());
	}

	@SuppressWarnings({"InfiniteLoopStatement", "ConstantConditions"})
	public void run() {
		boolean skipNext = false;

		try {
			try {
				lock.lock();
				if (thread != null)
					throw new RuntimeException("Attempt to run multiple refresh threads");
				thread = Thread.currentThread();
			}
			finally { lock.unlock(); }

			while(true) {
				try {
					if (skipNext)
						skipNext = false;
					else
						refresh();
					Thread.sleep(getRefreshCycle());
				}
				catch (InterruptedException e) {
					// intentionally
					try {
						lock.lock();
						skipNext = skip;
						skip = false;
					}
					finally { lock.unlock(); }
				}
				catch (Exception e) {
					logger.warn("Could not refresh() " + unitName + " monitor: "
						  + e.toString());
				}
			}
		}
		finally {
			try {
				lock.lock();
				thread = null;
			}
			finally { lock.unlock(); }
		}
	}

	public static void main(String[] args) throws Exception {
		GroovyMoniConsole console;
		File configFile;
		boolean killConfig = false;
		if (args.length > 0) configFile = new File(args[0]);
		else {
			configFile = new File(System.getProperty("user.home")
				  + File.separatorChar
				  + "monitor.properties");
			configFile.delete();
			killConfig = true;
		}
		try {
			console = new GroovyMoniConsole("plain", configFile, null);
			Thread thread = new Thread(console);
			thread.start();
			thread.join();
		}
		finally {
			if (killConfig)
				configFile.delete();
		}
	}

	/**
	 * Start server if it is not running, otherwise,
	 * restart server if a new configuration is available (kills current connections)
	 *
	 * @return true if the server is running after this call
	 * @throws Exception
	 */
	@SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "StatementWithEmptyBody"})
	public boolean refresh() throws Exception {
		lock.lock();
		try {
			if (skip)
				return isServerRunning();

			synchronized(this) {
				if (server == null && enabled)
						;
				else {
					Map<Object, Object> next = configStream.nextElementIfDifferent();
					if (next == null)
						return server != null;
					else {
						stopServer();
						setupState(next);
					}
				}
			}
			
			if (thread != null) {
				skip = true;
				thread.interrupt();
			}
			return startServer();
		}
		finally { lock.unlock(); }
	}

	/**
	 * Start the servlet server for serving monitorconsoles
	 *
	 * @return true if the server was started succesfully
	 * @throws Exception
	 */
	@SuppressWarnings(
		  {"HardcodedFileSeparator", "AccessToStaticFieldLockedOnInstance",
			    "BooleanMethodNameMustStartWithQuestion"})
	public synchronized boolean startServer() throws Exception {
		if (enabled) {
			logger.info("Starting monitor server for '" +
				  unitName + "' on " + host + ':' + port);
			try {
					Server aServer = createServerObject();

					Connector connector = createConnector();

					SecurityHandler secH = createSecurityHandler();
					ContextHandler conH = new ContextHandler();
					conH.setAttribute("roleName", roleName);
					conH.setAttribute("console", this);

					SessionHandler sesH = createSessionHandler();
					ServletHandler srvH = createServletHandler();

					aServer.setConnectors(new Connector[] { connector });
					conH.setHandler(secH);
					secH.setHandler(sesH);				
					sesH.setHandler(srvH);
					aServer.setHandler(conH);
					aServer.setStopAtShutdown(true);

					aServer.start();
					server = aServer;
					return true;
			}
			catch (Exception e) {
				logger.warn("Failed starting monitor server for '" + unitName + '\'');
				throw e;
			}
		}
		return false;
	}

	private Server createServerObject() {
		Server aServer = new Server();
		BoundedThreadPool pool = new BoundedThreadPool();
		aServer.setThreadPool(pool);
		pool.setMinThreads(minConnections);
		pool.setMaxThreads(maxConnections);
		return aServer;
	}

	@SuppressWarnings({"HardcodedFileSeparator"})
	private static ServletHandler createServletHandler() {
		ServletHandler srvH = new ServletHandler();
		ServletHolder holder = new ServletHolder(GroovyMoniServlet.class);
		srvH.addServletWithMapping(holder, "/");
		return srvH;
	}

	private Connector createConnector() {
		Connector connector = new SelectChannelConnector();
		connector.setHost(host);
		connector.setPort(port);
		connector.setMaxIdleTime(maxIdleTime);
		return connector;
	}

	private SessionHandler createSessionHandler() {
		SessionManager sesM = new HashSessionManager();
		SessionIdManager sidM = new HashSessionIdManager();
		sesM.setIdManager(sidM);

		SessionHandler sesH = new SessionHandler();
		sesH.setSessionManager(sesM);
		sesH.setServer(server);
		return sesH;
	}

	@SuppressWarnings({"HardcodedFileSeparator"})
	private SecurityHandler createSecurityHandler() {
		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__DIGEST_AUTH);
		constraint.setRoles(new String[] { roleName });
		constraint.setAuthenticate(true);

		ConstraintMapping cm = new ConstraintMapping();
		cm.setConstraint(constraint);
		cm.setPathSpec("/*");

		HashUserRealm ur = new HashUserRealm(unitName);
		ur.put(user, password);
		ur.addUserToRole(user, roleName);

		SecurityHandler secH = new SecurityHandler();
		secH.setConstraintMappings(new ConstraintMapping[] { cm });
		secH.setUserRealm(ur);
		return secH;
	}

	/***
	 * Stop the monitorserver
	 *
	 * @throws Exception
	 */
	@SuppressWarnings({"AccessToStaticFieldLockedOnInstance"})
	public synchronized void stopServer() throws Exception {
		try {
			final Server aServer = server;
			if (aServer != null) {
				logger.info("Stopping monitor server for '" + unitName + '\'');
				aServer.stop();
			}
		}
		finally {
			server = null;
		}
	}


	public synchronized boolean isServerEnabled() {
		return enabled;
	}

	public synchronized boolean isServerRunning() {
		return server != null;
	}

	@Nullable
	public BindingFactory getBindingFactory() {
		return bindingFactory;
	}

	public void setBindingFactory(@Nullable BindingFactory newBindingFactory) {
		bindingFactory = newBindingFactory;
	}

	/**
	 * Setup this object's state based on a newly retrieved configuration
	 * @param props
	 */
	@SuppressWarnings({"MagicNumber"})
	private synchronized void setupState(@NotNull Map<Object, Object> props) {
		logger.info("Reconfiguration of " + unitName + " monitor using: " + props.toString());
		host = getProperty(props, "monitor.host").trim();
		user = getProperty(props, "monitor.user").trim();
		defaultMode = GroovyMoniSession.RunMode.valueOf(
			  getProperty(props, "monitor.defaultMode").trim().toUpperCase());
		password = getProperty(props, "monitor.password").trim();
		setupNumbers(props);
		roleName = getProperty(props, "monitor.roleName").trim();

		enabled = "true".equals(getProperty(props, "monitor.enabled").trim());
		enabled &= isSaneSetup();
	}

	@SuppressWarnings({"MagicNumber"})
	private boolean isSaneSetup() {
		if (port < 0 || port > 65535) {
			logger.warn("Invalid port number; monitor disabled");
		}
		else if (host.length() == 0) {
			logger.warn("Invalid (empty) host name; monitor disabled");
		}
		else if (user.length() == 0) {
			logger.warn("Invalid (empty) user name; monitor disabled");
		}
		else if (password.length() == 0) {
			logger.warn("Invalid (empty) password; monitor disabled");
		}
		else return true;
		return false;
	}

	private void setupNumbers(Map<Object, Object> props) {
		try {
			port = parseInt(props, "monitor.port");
			minConnections = Math.max(2, parseInt(props, "monitor.minConnections"));
			maxConnections = Math.max(minConnections, parseInt(props, "monitor.maxConnections"));
			// 1 sec min hopefully is a sensible lower bound
			refreshCycle = parseLong(props, "monitor.refreshCycle");
			maxScriptSize = parseInt(props, "monitor.maxScriptSize");
			maxIdleTime = parseInt(props, "monitor.maxIdleTime");
		}
		catch (NumberFormatException n) {
			enabled = false;
			logger.warn(n.getMessage());
			throw n;
		}
	}

	private static int parseInt(Map<Object, Object> props, String propName) {
		try {
			return Integer.parseInt(getProperty(props, propName).trim());
		}
		catch (NumberFormatException nfe) {
			throw new RuntimeException("Could not parse parameter " + propName, nfe);
		}
	}

	private static long parseLong(Map<Object, Object> props, String propName) {
		try{
			return Long.parseLong(getProperty(props, propName).trim());
		}
		catch (NumberFormatException nfe) {
			throw new RuntimeException("Could not parse parameter " + propName, nfe);
		}
	}
	/**
	 *
	 * @param map
	 * @param key
	 * @return value for key in map or DEFAULT_PROPERTIES
	 */
	@NotNull
	private static String getProperty(@NotNull Map<Object, Object> map, @NotNull String key)
	{
		final Object candidate = map.get(key);
		final String ret =  (String)
			  (candidate instanceof String ? candidate : DEFAULT_PROPERTIES.get(key));
		return ret == null ? "" : ret;
	}


	public synchronized long getRefreshCycle() {
		return refreshCycle;
	}

	public synchronized int getMaxScriptSize() {
		return maxScriptSize;
	}

	@NotNull
	public synchronized GroovyMoniSession.RunMode getDefaultMode() {
		return defaultMode;
	}
}
