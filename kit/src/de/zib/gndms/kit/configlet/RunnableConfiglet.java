package de.zib.gndms.kit.configlet;

import org.apache.commons.logging.Log;
import org.apache.log4j.NDC;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:26:42
 */
@SuppressWarnings({ "NoopMethodInAbstractClass" })
public abstract class RunnableConfiglet extends DefaultConfiglet implements Runnable {
    /**
     * The current Thread used by this-Object
     */
    private Thread thread;
    
    /**
      *  initializes Configlet with a logger, a start-configuration, a name and starts a new Thread 
     * @param loggerParam
     * @param name the name of the configuration
     * @param data the configuration, expected to be a {@code Map<String, String>}
     */
    @Override
	public final synchronized void init(@NotNull final Log loggerParam,
	                                 @NotNull String name, final Serializable data) {
		super.init(loggerParam, name, data);    // Overridden method
		threadInit();
		thread = new Thread(this);
		thread.start();
	}

    /**
     * Calls {@code run_()}
     */
	public void run() {
		NDC.push(getName());
		try {
			run_();
		}
		finally {
			NDC.pop();
		}
	}

    /**
     *  Initializes settings concerning this.getThread()
     */
    protected abstract void threadInit();

    /**
     * This method will be executed concurrent. Do not call it directly !
     */
    public abstract void run_();

	@Override
	public synchronized void update(@NotNull final Serializable data) {
		super.update(data);    // Overridden method
	}


	@Override
	public synchronized void shutdown() {
        super.shutdown();    // Overridden method
		threadStop();
	}

    /**
     * Stops the current running Thread
     */
	protected abstract void threadStop();

    /**
     * Returns the Thread used by this
     * @return the Thread used by this
     */
	public synchronized Thread getThread() {
		return thread;
	}
}

