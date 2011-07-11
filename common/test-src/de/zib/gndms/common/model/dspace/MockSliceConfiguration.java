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
 * A mockup for a slice configuration.
 * 
 * @author Ulrike Golas
 */

public class MockSliceConfiguration extends ConfigHolder {

	/**
	 * Constructs a mock config holder for a slice configuration.
	 * 
	 * @param directory The slice's directory.
	 * @param owner The slice's owner.
	 * @param value The slice's termination date.
	 */
	public MockSliceConfiguration(final String directory, final String owner, final long value) {
		Logger logger = LoggerFactory.getLogger(this.getClass());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = newEditor(visitor);
		setObjectMapper(objectMapper);
		
		try {
			JsonNode dn = ConfigHolder.parseSingle(factory, 
					SliceConfiguration.createSingleEntry(SliceConfiguration.DIRECTORY, directory));
			JsonNode on = ConfigHolder.parseSingle(factory, 
					SliceConfiguration.createSingleEntry(SliceConfiguration.OWNER, owner));
			JsonNode tn = ConfigHolder.parseSingle(factory, 
					SliceConfiguration.createSingleEntry(SliceConfiguration.TERMINATION, value));
			update(editor, dn);
			update(editor, on);
			update(editor, tn);
		} catch (IOException e) {
			logger.warn("Mock slice configuration could not be established.");
		} catch (UpdateRejectedException e) {
			logger.warn("Mock slice configuration could not be established.");
		}
	}

}
