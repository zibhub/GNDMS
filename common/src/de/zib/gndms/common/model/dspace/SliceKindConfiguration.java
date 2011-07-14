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
 * The slice kind configuration checks and accesses a ConfigHolder for a slice kind,
 * which has to consist (at least) of the following fields: <br> 
 * uri - the uri for the slice kind as text<br>
 * permission - the permissions of the slice kind as text according to an {@link AccessMask}<br>
 * and may also contain: <br>
 * metasubspaces - the meta-subspaces of the slice kind as text.
 * 
 * @author Ulrike Golas
 * 
 */
// TODO does that make sense, how to get the meta-subspaces from the string?

public class SliceKindConfiguration extends ConfigHolder {

	/**
	 * The key for the slice kind's uri.
	 */
	public static final String URI = "uri";
	/**
	 * The key for the slice kind's permission.
	 */
	public static final String PERMISSION = "permission";
	/**
	 * The key for the slice kind's meta-subspaces.
	 */
	public static final String METASUBSPACES = "metasubspaces";

	/**
	 * Checks if a given config holder is a valid slice kind configuration.
	 * 
	 * @param config
	 *            The config holder.
	 * @return true, if it is a valid slice kind configuration; otherwise false.
	 */
	public static boolean checkSliceKindConfiguration(final ConfigHolder config) {
		JsonNode node = config.getNode();
		try {
			if (node.findValue(URI).isTextual()
					&& node.findValue(PERMISSION).isNumber()
				    && isValidPermission(node.findValue(PERMISSION))) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

	
	/**
	 * Returns the uri of a slice kind configuration.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The uri.
	 */
	public static String getUri(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(URI).isTextual()) {
				return config.getNode().findValue(URI).getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}
	
	/**
	 * Returns the permissions of a slice kind configuration.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The permissions.
	 */
	public static long getPermission(final ConfigHolder config) {
		JsonNode node = config.getNode().findValue(PERMISSION);
		if (node == null) {
			throw new WrongConfigurationException("The key " + PERMISSION + " does not exist.");
		}
			if (node.isNumber() && isValidPermission(node)) {
				return node.getLongValue();
			} else {
				throw new WrongConfigurationException();
			}
	}

	/**
	 * Returns the identifiers of the meta-subspaces of a slice kind configuration or null, if it is not specified.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The set of meta-subspaces.
	 */
	public static Set<String> getMetaSubspaces(final ConfigHolder config) {
		try {
		if (config.getNode().findValue(METASUBSPACES).isTextual()) {
			// TODO get meta-subspaces
			return new HashSet<String>();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Tests if the given Json node has a valid permission value.
	 * @param node The node.
	 * @return true, if it is valid.
	 */
	private static boolean isValidPermission(final JsonNode node) {
		
		try {
			long nr = node.getLongValue();

			// TODO
			// should be AccessMask.fromString(Long.toString(nr));
			// but wrong package
	        if (nr < 99 || nr > 9999) {
	            throw new IllegalArgumentException();
	        }
	        long l;
			for (int i=0; i<3; i++) {
		        l = nr % 10;
				if (l > 7) {
		            throw new IllegalArgumentException();
		        }
				nr = nr / 10;
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
