package de.zib.gndms.kit.configlet;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * This abstract class stores a configuration in a map and will executed a loop concurrently.
 *
 * Implement {@code threadRun()}  to be executed concurrently in a loop after the {@code init} method has been invoked.
 *
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:30:51
 */
public abstract class RegularlyRunnableConfiglet extends RunnableConfiglet {

    /**
     * delay in miliseconds after that the {@code run_()}'s loop-Method executes the first iteration.
     */
    private long initDelay;
    /**
     * delay in miliseconds after that the {@code run_()}'s loop-Method executes every iteration.
     */
    private long delay;
    /**
     * flag needed to stop loop
     */
    private volatile boolean stop;

	@Override
	protected synchronized void threadInit() {
		configDelays();
	}


	@Override
	public synchronized void update(@NotNull final Serializable data) {
		super.update(data);
		configDelays();
	}

    /**
     * Sets the {@code delay} and {@code initDelay} according to the values as set in the current configuration. If not set,
     * {@code initDelay} will be set to 3 seconds and {@code delay} will be set to 5 seconds.Should be invoked by {@link RegularlyRunnableConfiglet#update(java.io.Serializable)} 
     */
	private void configDelays() {
		setInitDelay(getMapConfig().getLongOption("initialDelay", 3000L));
		setDelay(getMapConfig().getLongOption("delay", 5000L));
	}


    /**
     * Loop which invokes {@link RegularlyRunnableConfiglet#threadRun()} after {@code delay} seconds. Do not call this method directly !
     * Will be invoked by {@code RunnableConfiglet}'s {@code run()}-Method to run concurrent.
     */
    @Override
	public void run_() {
		try { Thread.sleep(initDelay); }
		catch (InterruptedException e) { /* intentional */ }
		while (! stop) {
			threadRun_();
			try { Thread.sleep(delay); }
			catch (InterruptedException e) { /* intentional */ }
		}

	}

	private synchronized void threadRun_() { threadRun(); }

	
    /**
     *  This method is invoked by {@link RegularlyRunnableConfiglet#run_()}'s loop, will be executed concurrently  
     */
	protected abstract void threadRun();


    @Override
	protected void threadStop() {
		stop = true;
		getThread().interrupt();
	}

    /**
     * Returns the initial delay for {@code run-()}'s loop in miliseconds
     * @return the initial delay for {@code run-()}'s loop in miliseconds
     */
	public synchronized long getInitDelay() {
		return initDelay;
	}

    /**
     * Sets the value for the inital Delay in miliseconds, after that {@code threadRun()} will be invoked for the first time}
     * @param initDelayParam the value for the inital delay in miliseconds, after that {@code threadRun()} will be invoked for the first time}
     */
	public synchronized void setInitDelay(final long initDelayParam) {
		initDelay = initDelayParam;
	}

    /**
    * Returns the delay for {@code run-()}'s loop in miliseconds
     * @return the delay for {@code run-()}'s loop in miliseconds
     */
	public synchronized long getDelay() {
		return delay;
	}

    /**
     * Sets the value for the delay in miliseconds, after that {@code threadRun()} will do the next interation in the loop
     * @param delayParam the value for the delay in miliseconds, after that {@code threadRun()} will do the next interation in the loop
     */
	public synchronized void setDelay(final long delayParam) {
		delay = delayParam;
	}
}
