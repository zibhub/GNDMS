package de.zib.gndms.stuff;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



/**
 * This class provides a method to suspend a thread's execution uniterruptible for a specific time  
 *
 * @see Thread
 * @author  try ste fan pla nti kow zib
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
