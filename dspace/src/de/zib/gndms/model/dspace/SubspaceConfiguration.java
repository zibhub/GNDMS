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

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor;

/**
 * The subspace configuration checks and accesses a ConfigHolder for a subspace,
 * which has to consist (at least) of the following fields: <br> 
 * path - the path of the subspace <br>
 * gsiFtpPath - the path for gsiFtp access <br>
 * visible - whether the subspaces meta <br>
 * subspace is visible size - the maximal size of the subspace <br>
 * mode - whether in create or update mode.
 * 
 * @author Ulrike Golas
 * 
 */
public final class SubspaceConfiguration {

	/**
	 * Do not use this constructor.
	 */
	private SubspaceConfiguration() {
	}
	
	/**
	 * Checks if a given config holder is a valid subspace configuration.
	 * 
	 * @param config
	 *            The config holder.
	 * @return true, if it is a valid subspace configuration; otherwise false.
	 */
	public static boolean checkSubspaceConfiguration(final ConfigHolder config) {
		JsonNode node = config.getNode();
		try {
			if (!node.findValue("path").isTextual()
					|| !node.findValue("gsiFtpPath").isTextual()
					|| !node.findValue("visible").isBoolean()
					|| !node.findValue("size").isNumber()
					|| !isValidMode(node.findValue("mode"))) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Constructs the subspace configuration of a given subspace.
	 * 
	 * @param sub
	 *            The subspace.
	 * @return The config holder.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	public static ConfigHolder getSubspaceConfiguration(final Subspace sub) throws IOException, UpdateRejectedException {
		String path = sub.getPath();
		String gsiftp = sub.getGsiFtpPath();
		boolean visible = sub.getMetaSubspace().isVisibleToPublic();
		long size = sub.getAvailableSize();

		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		JsonNode pn = ConfigHolder.parseSingle(factory, "{ 'path': '" + path + "' }");
		JsonNode gn = ConfigHolder.parseSingle(factory, "{ 'gsiFtpPath': '" + gsiftp
					+ "' }");
		JsonNode vn = ConfigHolder.parseSingle(factory, "{ 'visible': " + visible + " }");
		JsonNode sn = ConfigHolder.parseSingle(factory, "{ 'size': " + size + " }");
		JsonNode mn = ConfigHolder.parseSingle(factory, "{ 'mode': 'update' }");
		config.update(editor, pn);
		config.update(editor, gn);
		config.update(editor, vn);
		config.update(editor, sn);
		config.update(editor, mn);
			
		return config;
	}

	/**
	 * Returns the path of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The path.
	 * @throws WrongConfigurationException if the configuration does not contain a path.
	 */
	public static String getPath(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (config.getNode().findValue("path").isTextual()) {
				return config.getNode().findValue("path").getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}
	
	/**
	 * Returns the gsi ftp path of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The gsiFtp path.
	 * @throws WrongConfigurationException if the configuration does not contain a gsi ftp path.
	 */
	public static String getGsiFtpPath(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (config.getNode().findValue("gsiFtpPath").isTextual()) {
				return config.getNode().findValue("gsiFtpPath").getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Returns the visibility of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The visibility.
	 * @throws WrongConfigurationException if the configuration does not contain a visibility.
	 */
	public static boolean getVisibility(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (config.getNode().findValue("visible").isBoolean()) {
				return config.getNode().findValue("visible").getBooleanValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Returns the size of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The size.
	 * @throws WrongConfigurationException if the configuration does not contain a size.
	 */
	public static long getSize(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (config.getNode().findValue("size").isNumber()) {
				return config.getNode().findValue("size").getLongValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Returns the mode of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The size.
	 * @throws WrongConfigurationException if the configuration does not contain a size.
	 */
	public static String getMode(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (isValidMode(config.getNode().findValue("mode"))) {
				return config.getNode().findValue("mode").getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}

	/**
	 * Test if the given Json node haas a valid mode value, i.e. "create" or "update".
	 * @param node The node.
	 * @return true, if it is valid.
	 */
	private static boolean isValidMode(final JsonNode node) {
		if ("create".equals(node.getTextValue()) || "update".equals(node.getTextValue())) {
			return true;
		} else {
			return false;
		} 
	}
}
