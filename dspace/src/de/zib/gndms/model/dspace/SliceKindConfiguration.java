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

import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

/**
 * The slice kind configuration checks and accesses a ConfigHolder for a slice kind,
 * which has to consist (at least) of the following fields: <br> 
 * ToDo
 * 
 * @author Ulrike Golas
 * 
 */

public class SliceKindConfiguration extends ConfigHolder {

	/**
	 * Do not use this constructor.
	 */
	private SliceKindConfiguration() {
	}

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
			// TODO anpassen
			if (!node.findValue("xxx").isTextual()) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return true;
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
		// TODO auslesen
		String xxx = "";
		
		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		// TODO anpassen
		JsonNode pn = ConfigHolder.parseSingle(factory, "{ 'xxx': '" + xxx + "' }");
		config.update(editor, pn);
			
		return config;
	}
	
	// TODO getter der Einträge
	/**
	 * Returns the xxx of a slice kind configuration.
	 * @param config The config holder, which has to be a valid slice kind configuration.
	 * @return The xxx.
	 * @throws WrongConfigurationException if the configuration does not contain a path.
	 */
	public static String getXxx(final ConfigHolder config) throws WrongConfigurationException {
		try {
			if (config.getNode().findValue("xxx").isTextual()) {
				return config.getNode().findValue("xxx").getTextValue();
			} else {
				throw new WrongConfigurationException();
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException();
		}
	}
}
