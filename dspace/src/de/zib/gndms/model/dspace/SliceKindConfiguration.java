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
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

/**
 * The slice kind configuration checks and accesses a ConfigHolder for a slice kind,
 * which has to consist (at least) of the following fields: <br> 
 * uri - the uri for the slice kind as text<br>
 * permission - the permissions of the slice kind as text according to an {@link AccessMask}<br>
 * and may also contain: <br>
 * metasubspaces - the meta-subspaces of the slice kind as text 
 * 
 * @author Ulrike Golas
 * 
 */
// TODO does that make sense, how to get the meta-subspaces from the string?

public class SliceKindConfiguration extends ConfigHolder {

	public static String URI = "uri";
	public static String PERMISSION = "permission";
	public static String METASUBSPACES = "metasubspaces";

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
	 * Constructs the slice kind configuration of a given slice kind.
	 * 
	 * @param slicekind
	 *            The slice kind.
	 * @return The config holder.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	public static ConfigHolder getSliceKindConfiguration(final SliceKind slicekind) throws IOException, UpdateRejectedException {
		String uri = slicekind.getURI();
		AccessMask permission = slicekind.getPermission();
		Set<MetaSubspace> metaSubspaces = slicekind.getMetaSubspaces();
		
		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		JsonNode un = ConfigHolder.parseSingle(factory, createSingleEntry(URI, uri));
		JsonNode pn = ConfigHolder.parseSingle(factory, createSingleEntry(PERMISSION, permission));
		// TODO: how to store meta-subspaces
		JsonNode mn = ConfigHolder.parseSingle(factory, createSingleEntry(METASUBSPACES, metaSubspaces.toString()));
		config.update(editor, un);
		config.update(editor, pn);
		config.update(editor, mn);
			
		return config;
	}
	
	/**
	 * Returns the uri of a slice kind configuration.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The uri.
	 * @throws WrongConfigurationException if the configuration does not contain a uri.
	 */
	public static String getUri(final ConfigHolder config) throws WrongConfigurationException {
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
	 * @throws WrongConfigurationException if the configuration does not contain a valid permission.
	 */
	public static AccessMask getPermission(final ConfigHolder config) throws WrongConfigurationException {
		JsonNode node = config.getNode().findValue(PERMISSION);
		if (node == null) {
			throw new WrongConfigurationException("The key " + PERMISSION + " does not exist.");
		}
			if (node.isNumber() && isValidPermission(node)) {
				long nr = node.getLongValue();
				return AccessMask.fromString(Long.toString(nr));
			} else {
				throw new WrongConfigurationException();
			}
	}

	/**
	 * Returns the meta-subspaces of a slice kind configuration or null, if it is not specified.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The set of meta-subspaces.
	 * @throws WrongConfigurationException if the configuration does not contain a path.
	 */
	public static Set<MetaSubspace> getMetaSubspaces(final ConfigHolder config) throws WrongConfigurationException {
		try {
		if (config.getNode().findValue(METASUBSPACES).isTextual()) {
			// TODO get meta-subspaces
			return new HashSet<MetaSubspace>();
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
			AccessMask.fromString(Long.toString(nr));
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}

}
