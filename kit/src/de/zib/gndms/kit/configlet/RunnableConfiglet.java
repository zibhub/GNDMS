package de.zib.gndms.kit.configlet;

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



import org.apache.commons.logging.Log;
import org.apache.log4j.NDC;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * This abstract class stores a configuration in a map and provides a method which runs concurrently.
 *
 * A method of an implementing subclass, which should run concurrently must be either {@code run_()} or a method,
 * which is invoked by {@code run_().
 * Concurrent execution is done automatically,
 * as soon as {@link #init(org.apache.commons.logging.Log, String, java.io.Serializable)} is invoked.
 *
 * @author: try ste fan pla nti kow zib
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
     *  Initializes Configlet with a logger, a start-configuration, a name and starts a new Thread, executing {@code run()}
     * concurrently.
     *
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
     * Calls {@code run_()} concurrently
     *
     * ensures we're synchronized
     * (required for sensible semantics of thread.interrupt during update)
     */
	public final void run() {
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
		thread.interrupt();
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
     *
     * @return the Thread used by this
     */
	public synchronized Thread getThread() {
		return thread;
	}
}

