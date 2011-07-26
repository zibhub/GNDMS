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

/**
 * A configuration, which provides the necessary properties for some entity.
 * 
 * @author Ulrike Golas
 *
 */
public interface Configuration {

	/**
	 * Test, if this configuration is valid, i.e. satisfies all its requirements.
	 * @return true, if this configuration is valid, else false.
	 */
	boolean isValid();
	
	/**
	 * The String representation of a configuration, which should display key-value pairs as:
	 * key1 : value1 ; key2 : value2 ; ...
	 * @return The String representation.
	 */
	String displayConfiguration();
}
