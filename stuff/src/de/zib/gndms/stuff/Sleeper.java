package de.zib.gndms.stuff;

/**
 * This class provides a method to suspend a thread's execution uniterruptible for a specific time  
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.12.2008 Time: 18:12:15
 */
public class Sleeper {
	private Sleeper() {}

    /**
     * Causes the currently executing thread to suspend execution uniterruptible for a specific time of milliseconds
     * 
     * @param millis time in milliseconds
     */
	public static void sleepUninterruptible(final long millis) {
		final long end = System.currentTimeMillis() + millis;
		while (true) {
			final long remaining = end - System.currentTimeMillis();
			if (remaining <= 0L)
				return;
			try {
				Thread.sleep(remaining);
			}
			catch (InterruptedException e) { /* intendend */ }
		}
	}


	// public static void main(String[] args) {
	//	sleepUninterruptible(5000L);
	// }
}
