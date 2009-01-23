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
	private Thread thread;

    @Override
	public final synchronized void init(@NotNull final Log loggerParam,
	                                 @NotNull String name, final Serializable data) {
		super.init(loggerParam, name, data);    // Overridden method
		threadInit();
		thread = new Thread(this);
		thread.start();
	}


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
     * 
     */
    protected abstract void threadInit();

    /**
     *
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


	protected abstract void threadStop();


	public synchronized Thread getThread() {
		return thread;
	}
}

