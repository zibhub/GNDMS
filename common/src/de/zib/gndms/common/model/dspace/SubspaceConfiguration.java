package de.zib.gndms.common.model.dspace;

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

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonNode;

import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * The subspace configuration checks and accesses a ConfigHolder for a subspace,
 * which has to consist (at least) of the following fields: <br> 
 * path - the path of the subspace as text <br>
 * gsiFtpPath - the path for gsiFtp access as text <br>
 * visible - whether the subspaces meta subspace is visible as boolean<br>
 * size - the maximal size of the subspace as number<br>
 * mode - an entry in {@link SetupMode}.
 * 
 * @author Ulrike Golas
 * 
 */
public final class SubspaceConfiguration extends ConfigHolder {

	/**
	 * The key for the subspace's path.
	 */
	public static final String PATH = "path";
	/**
	 * The key for the subspace's gsiftp-path.
	 */
	public static final String GSIFTPPATH = "gsiFtpPath";
	/**
	 * The key for the subspace's visibility.
	 */
	public static final String VISIBLE = "visible";
	/**
	 * The key for the subspace's size.
	 */
	public static final String SIZE = "size";
	/**
	 * The key for the subspace's mode.
	 */
	public static final String MODE = "mode";
	
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
			if (!node.findValue(PATH).isTextual()
					|| !node.findValue(GSIFTPPATH).isTextual()
					|| !node.findValue(VISIBLE).isBoolean()
					|| !node.findValue(SIZE).isNumber()
					|| !isValidMode(node.findValue(MODE))) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the path of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The path.
	 */
	public static String getPath(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(PATH).isTextual()) {
				return config.getNode().findValue(PATH).getTextValue();
			} else {
				throw new WrongConfigurationException("The key " + PATH + " exists but is no text value.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + PATH + " does not exist.");
		}
	}
	
	/**
	 * Returns the gsi ftp path of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The gsiFtp path.
	 */
	public static String getGsiFtpPath(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(GSIFTPPATH).isTextual()) {
				return config.getNode().findValue(GSIFTPPATH).getTextValue();
			} else {
				throw new WrongConfigurationException("The key " + GSIFTPPATH + " exists but is no text value.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + GSIFTPPATH + " does not exist.");
		}
	}

	/**
	 * Returns the visibility of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The visibility.
	 */
	public static boolean getVisibility(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(VISIBLE).isBoolean()) {
				return config.getNode().findValue(VISIBLE).getBooleanValue();
			} else {
				throw new WrongConfigurationException("The key " + VISIBLE + " exists but is no boolean.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + VISIBLE + " does not exist.");
		}
	}

	/**
	 * Returns the size of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The size.
	 */
	public static long getSize(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(SIZE).isNumber()) {
				return config.getNode().findValue(SIZE).getLongValue();
			} else {
				throw new WrongConfigurationException("The key " + SIZE + " exists but is no numer.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + SIZE + " does not exist.");
		}
	}

	/**
	 * Returns the mode of a subspace configuration.
	 * @param config The config holder, which has to be a valid subspace configuration.
	 * @return The size.
	 */
	public static String getMode(final ConfigHolder config) {
			JsonNode node = config.getNode().findValue(MODE);
			if (node == null) {
				throw new WrongConfigurationException("The key " + MODE + " does not exist.");
			}
			if (isValidMode(node)) {
				return node.getTextValue();
			} else {
				throw new WrongConfigurationException("The key " + MODE + " exists but is no valid mode.");
			}
	}

	/**
	 * Tests if the given Json node has a valid mode value, i.e. "create" or "update".
	 * @param node The node.
	 * @return true, if it is valid.
	 */
	private static boolean isValidMode(final JsonNode node) {
		
		try {
			// TODO
			// should be SetupMode.valueOf(node.getTextValue());
			// but wrong package
			String s = node.getTextValue();
			Set<String> valid = new HashSet<String>();
			valid.add("CREATE");
			valid.add("READ");
			valid.add("UPDATE");
			valid.add("DELETE");
			if (!valid.contains(s)) {
				throw new IllegalArgumentException();				
			}
			
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}


}
