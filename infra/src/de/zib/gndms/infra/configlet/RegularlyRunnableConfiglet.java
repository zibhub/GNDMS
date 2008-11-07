package de.zib.gndms.infra.configlet;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:30:51
 */
public abstract class RegularlyRunnableConfiglet extends RunnableConfiglet {
	private long initDelay;
	private long delay;
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


	private void configDelays() {
		setInitDelay(getMapConfig().getLongOption("initialDelay", 3000L));
		setDelay(getMapConfig().getLongOption("delay", 5000L));
	}


	@Override
	public void run_() {
		try { Thread.sleep(initDelay); }
		catch (InterruptedException e) { /* intentional */ }
		while (! stop) {
			threadRun();
			try { Thread.sleep(delay); }
			catch (InterruptedException e) { /* intentional */ }
		}

	}


	protected abstract void threadRun();


	@Override
	protected void threadStop() {
		stop = true;
		getThread().interrupt();
	}


	public synchronized long getInitDelay() {
		return initDelay;
	}


	public synchronized void setInitDelay(final long initDelayParam) {
		initDelay = initDelayParam;
	}


	public synchronized long getDelay() {
		return delay;
	}


	public synchronized void setDelay(final long delayParam) {
		delay = delayParam;
	}
}
