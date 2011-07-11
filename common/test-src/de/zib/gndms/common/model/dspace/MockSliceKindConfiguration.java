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

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

/**
 * A mockup for a slice kind configuration.
 * 
 * @author Ulrike Golas
 */

public class MockSliceKindConfiguration extends ConfigHolder {

	/**
	 * Constructs a mock config holder for a slice kind configuration.
	 *
	 * @param uri The slice kind's uri.
	 * @param permission The slice kind's permission.
	 * @param meta The slice kind's meta-subspace.
	 */
	public MockSliceKindConfiguration(final String uri, final long permission, final String meta) {
		Logger logger = LoggerFactory.getLogger(this.getClass());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = newEditor(visitor);
		setObjectMapper(objectMapper);
		
		try {
			JsonNode un = ConfigHolder.parseSingle(factory, 
					SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.URI, uri));
			JsonNode pn = ConfigHolder.parseSingle(factory, 
					SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.PERMISSION, permission));
			update(editor, un);
			update(editor, pn);
	      	// TODO add metasubspaces and test for it
	 		if (meta != null) {
	 			JsonNode mn = ConfigHolder.parseSingle(factory, 
	 					SliceKindConfiguration.createSingleEntry(SliceKindConfiguration.METASUBSPACES, meta));
	 			update(editor, mn);
	 		}
		} catch (IOException e) {
			logger.warn("Mock slice kind configuration could not be established.");
		} catch (UpdateRejectedException e) {
			logger.warn("Mock slice kind configuration could not be established.");
		}
	}

}
