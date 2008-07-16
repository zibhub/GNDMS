package de.zib.gndms.infra.db;

import org.apache.log4j.Logger;
import org.globus.wsrf.jndi.Initializable;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.06.2008 Time: 23:09:00
 */
@SuppressWarnings({"OverloadedMethodsWithSameNumberOfParameters"})
public final class DbSetupFacade implements Initializable {
	private static final Lock factoryLock = new ReentrantLock();

	private static final Logger logger = Logger.getLogger(DbSetupFacade.class);

	private DbSetupFacade()  {
		// initialization intentionally deferred to initialize
	}

	public void initialize() throws RuntimeException {
		System.out.println("YAY!");
		logger.warn("DbSetupFacade initialized");
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
	public static DbSetupFacade lookupInstance(@NotNull Context sharedContext, @NotNull Name facadeName)
		  throws NamingException {
		try {
			final Factory theFactory = new Factory();
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((Factory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	@NotNull
	public static DbSetupFacade lookupInstance(@NotNull Context sharedContext, @NotNull String facadeName)
		  throws NamingException {
		try {
			final Factory theFactory = new Factory();
			sharedContext.bind(facadeName, theFactory);
			return theFactory.getInstance();
		}
		catch (NameAlreadyBoundException n) {
			return ((Factory) sharedContext.lookup(facadeName)).getInstance();
		}
	}

	private static final class Factory {
		private DbSetupFacade instance;
		private RuntimeException exception;

		private DbSetupFacade getInstance() {
			factoryLock.lock();
			try {
				if (exception != null)
					throw exception;
				if (instance == null) {
					try {
						instance = new DbSetupFacade();
						instance.initialize();
					}
					catch (RuntimeException e) {
						exception = e;
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
