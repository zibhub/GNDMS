package de.zib.gndms.service;

import de.zib.gndms.stubs.types.UnknownORFType;
import org.apache.axis.types.URI;
import org.apache.log4j.Logger;
import types.ContextT;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** 
 * I am a service for looking up orf port endpoint URIs based on a type URIs.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public final class WHORFXImpl extends WHORFXImplBase {
	private static final int INITIAL_MAPPING_CAPACITY = 32;
	private static final Logger logger = Logger.getLogger(WHORFXImpl.class);

	private final Map<URI, URI> mapping = new HashMap<URI, URI>(INITIAL_MAPPING_CAPACITY);
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	private AtomicLong lastUpdate = new AtomicLong();

	public WHORFXImpl() throws RemoteException {
		super();
		try {
			reloadMapping();
		}
		catch (Exception e) {
			throw new RemoteException(e.toString());
		}
	}

	public org.apache.axis.types.URI lookupORF(org.apache.axis.types.URI orfType, ContextT context)
		  throws RemoteException, de.zib.gndms.stubs.types.UnknownORFType {
		try {
			lock.readLock().lock();
			URI result = mapping.get(orfType);
			if (result == null) {
				UnknownORFType error = new UnknownORFType();
				error.setFaultString(
					  "ORFType '" + (orfType == null ? "null" : orfType.toString()) +
							"' is unknown to this service.");
				throw error;
			}
			return result;
		}
		finally {
			lock.readLock().unlock();
		}
	}

	public void updateMappings() throws RemoteException {
		// Avoid system blocking by permanent update requests
		if (!hasMinimumUpdateIntervalPassed())
			return;

		threadSafeReloadMapping();
	}

	private void threadSafeReloadMapping() throws RemoteException {
		if (lock.writeLock().tryLock()) {
			try {
				reloadMapping();
			}
			catch (Exception e) {
				throw new RemoteException(e.getMessage());
			}
			finally {
				lock.writeLock().unlock();
			}
		}
	}

	private boolean hasMinimumUpdateIntervalPassed() throws RemoteException {
		try {
			if (System.currentTimeMillis() - lastUpdate.get() < getMinimumUpdateInterval())
				return false;
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		return true;
	}

	@SuppressWarnings({"ObjectAllocationInLoop"})
	public void reloadMapping() throws Exception {
		Properties props = loadProperties();
		mapping.clear();
		logger.info("Updating orfMapping");
		for (Object key : props.keySet()) {
			try {
				String keyName = key.toString();
				if (keyName.endsWith(".orfType")) {
					URI orfType = new URI(props.get(keyName).toString());
					String orfKey = (String) props.get(keyName + ".orfPort");
					URI orfPort = new URI(orfKey);
					logger.debug('\'' + orfType.toString()  + "' => '"
									  + orfPort.toString() + '\'');
					mapping.put(orfType, orfPort);
				}
			}
			catch (RuntimeException re) {
				logger.debug(re);
			}
		}
		lastUpdate.set(System.currentTimeMillis());
	}

	public static Properties loadProperties() throws Exception {
		final File mappingFile = getOrfMappingFile(WHORFXConfiguration.getConfiguration());
		logger.info("Loading orfMappingFile from " + mappingFile.toString());
		final Properties props = new Properties();
		FileInputStream theInStream = null;
		try {
			theInStream = new FileInputStream(mappingFile);
			props.load(theInStream);
		}
		finally {
			if (theInStream != null)
				theInStream.close();
		}
		return props;
	}

	private static File getOrfMappingFile(WHORFXConfiguration config) throws Exception {
		return new File(config.getOrfMappingFile());
	}

	private static long getMinimumUpdateInterval() throws Exception {
		return Long.parseLong(WHORFXConfiguration.getConfiguration().getMinimumUpdateInterval());
	}

}

