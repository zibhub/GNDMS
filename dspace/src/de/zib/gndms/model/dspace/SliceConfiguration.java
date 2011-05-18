package de.zib.gndms.model.dspace;

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

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

/**
 * The slice configuration checks and accesses a ConfigHolder for a slice, which
 * has to consist (at least) of the following fields: <br>
 * directory - the (relative) path of the slice as text<br>
 * owner - the owner of the slice as text<br>
 * termination - the termination time of the slice as number representing
 * standard base time
 * 
 * @author Ulrike Golas
 * 
 */

public class SliceConfiguration extends ConfigHolder {

	public static String DIRECTORY = "directory";
	public static String OWNER = "owner";
	public static String TERMINATION = "termination";

	/**
	 * Do not use this constructor.
	 */
	private SliceConfiguration() {
	}

	/**
	 * Checks if a given config holder is a valid slice configuration.
	 * 
	 * @param config
	 *            The config holder.
	 * @return true, if it is a valid slice configuration; otherwise false.
	 */
	public static boolean checkSliceConfiguration(final ConfigHolder config) {
		JsonNode node = config.getNode();
		try {
			if (!node.findValue(DIRECTORY).isTextual()
					|| !node.findValue(OWNER).isTextual()
					|| !node.findValue(TERMINATION).isNumber()) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Constructs the slice configuration of a given slice.
	 * 
	 * @param slice
	 *            The slice.
	 * @return The config holder.
	 * @throws IOException
	 * @throws UpdateRejectedException
	 */
	public static ConfigHolder getSliceConfiguration(final Slice slice)
			throws IOException, UpdateRejectedException {
		String directory = slice.getDirectoryId();
		String owner = slice.getOwner();
		Long termination = slice.getTerminationTime().getTimeInMillis();
		
		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		JsonNode dn = ConfigHolder.parseSingle(factory, "{ '"+ DIRECTORY + "': '" + directory
				+ "' }");
		JsonNode on = ConfigHolder.parseSingle(factory, "{ '"+ OWNER + "': '" + owner
				+ "' }");
		JsonNode tn = ConfigHolder.parseSingle(factory, "{ '"+ TERMINATION + "': '" + termination
				+ "' }");
		config.update(editor, dn);
		config.update(editor, on);
		config.update(editor, tn);

		return config;
	}

	/**
	 * Returns the directory of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The directory.
	 * @throws WrongConfigurationException
	 *             if the configuration does not contain a path.
	 */
	public static String getDirectory(final ConfigHolder config)
			throws WrongConfigurationException {
		try {
			if (config.getNode().findValue(DIRECTORY).isTextual()) {
				return config.getNode().findValue(DIRECTORY).getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Returns the owner of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The owner.
	 * @throws WrongConfigurationException
	 *             if the configuration does not contain a path.
	 */
	public static String getOwner(final ConfigHolder config)
			throws WrongConfigurationException {
		try {
			if (config.getNode().findValue(OWNER).isTextual()) {
				return config.getNode().findValue(OWNER).getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Returns the termination time of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The directory.
	 * @throws WrongConfigurationException
	 *             if the configuration does not contain a path.
	 */
	public static Calendar getTerminationTime(final ConfigHolder config)
			throws WrongConfigurationException {
		try {
			if (config.getNode().findValue(TERMINATION).isNumber()) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTimeInMillis(config.getNode().findValue(TERMINATION).getLongValue());
				return cal;
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}
}
