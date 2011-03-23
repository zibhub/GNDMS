package de.zib.gndms.kit.config;

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



import de.zib.gndms.kit.logging.LDPHolder;
import de.zib.gndms.kit.logging.LoggingDecisionPoint;
import de.zib.gndms.stuff.config.InfiniteEnumeration;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Provides a stream of config/properties objects as Map<Object,Object>'s
 *
 * tryNextElement() will only get you a new element if either there was no config file in the
 * first place (than a default one is written to disk) or if it has changed since the last time
 * it was read.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.07.2008 Time: 23:05:39
 */
public class PropertiesFromFile extends InfiniteEnumeration<Map<Object, Object>> implements LoggingDecisionPoint, LDPHolder {
	@NotNull
	private final File configFile;

	@Nullable
	private Map<Object, Object> current;

	@NotNull
	private final String desciptiveName;

	@NotNull
	private final String defaultsComment;

	@NotNull
	private final Properties defaults;
	private Logger logger;

	@Nullable
	private LoggingDecisionPoint ldp;

	public PropertiesFromFile(@NotNull File theConfigFile, @NotNull String aDescriptiveName,
	                          @NotNull Properties theDefaults, @NotNull String aDefaultComment,
							  @Nullable Logger aLogger) {
		super();
		configFile = theConfigFile;
		desciptiveName = aDescriptiveName;
		defaultsComment = aDefaultComment;
		defaults = theDefaults;
		logger = aLogger;
	}

	@Override @Nullable
	synchronized protected Map<Object, Object> tryNextElement() {
		try {
			return tryLoadProperties();
		}
		catch (IOException e) {
			logger.warn("Ignoring failure while attempting to load " + desciptiveName, e);
			return null;
		}
	}

	@Override
	@NotNull
	synchronized protected Map<Object, Object> createInitialDefaultElement() {
		try {
            final Map<Object, Object> map = tryLoadProperties();
            if (map == null)
                throw new NullPointerException("Could not create initial default element");
            else
                return map;
		}
		catch (IOException e) {
			logger.warn("Failure while attempting to initialize " + desciptiveName, e);
			throw new RuntimeException(e);
		}
	}

	@Nullable
	private Map<Object, Object> tryLoadProperties() throws IOException {
		Properties props;
		FileInputStream in = null;
		if (shouldLog("load"))
			logger.debug("Loading " + desciptiveName);
		try {
			if (! configFile.exists() || (configFile.length() == 0)) {
				tryWriteDefaults();
                return null;
            }
            in = new FileInputStream(configFile);
            props = new Properties();
            props.load(in);
            return props;
		}
		finally {
			if (in != null)
				try {
					in.close();
				}
				catch (IOException e) {
					// intentionally left empty
				}
		}
	}

	private  void tryWriteDefaults() throws IOException {
		if (shouldLog("newdefaults"))
			logger.info("Creating default " + desciptiveName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(configFile);
			defaults.store(out, defaultsComment);
		}
		catch (IOException e) {
			logger.warn("Failed creating default " + desciptiveName);
			throw e;
		}
		finally {
			if (out != null)
				try {
					out.close();
				}
				catch (IOException e) {
					// skip this one
				}
		}
	}

	@Nullable
	public synchronized LoggingDecisionPoint getLDP() {
		return ldp;
	}

	public synchronized void setLDP(@Nullable LoggingDecisionPoint newLDP) {
		ldp = newLDP;
	}

	public synchronized boolean shouldLog(@NotNull String token) {
		return ldp == null || ldp.shouldLog(token);
	}
}
