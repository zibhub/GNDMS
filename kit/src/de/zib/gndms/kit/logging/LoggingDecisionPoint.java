package de.zib.gndms.kit.logging;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.HashSet;

/**
 * Logging decision points decide wether a certain type of log message should be emitted.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 29.07.2008 Time: 18:04:37
 */
public interface LoggingDecisionPoint {
	boolean shouldLog(@NotNull String token);

	final class Parser {
		private Parser() {}


		@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
        public static @Nullable Set<String> parseTokenSet(@NotNull String s) {
			try {
				final StringTokenizer tokenizer = new StringTokenizer(s, ",");
				Set<String> set = new HashSet<String>(tokenizer.countTokens());
				while (tokenizer.hasMoreTokens()) {
					@NotNull String candidate = tokenizer.nextToken().trim();
					if (candidate.length() > 0 && candidate.charAt(0) == '!')
						;
					else
						set.add(candidate);
				}
				return set;
			}
			catch (RuntimeException e) {
				return null;
			}
		}
	}
}
