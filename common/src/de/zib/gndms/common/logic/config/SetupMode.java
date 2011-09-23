/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.common.logic.config;

/**
 * The enumeration of setup modes, which can be of the values create, read, update, or delete.
 *
 * @author Ulrike Golas
 *
 */

public enum SetupMode { 
	/**
	 * The mode for creation.
	 */
	CREATE, 
	/**
	 * The mode for reading.
	 */
	READ, 
	/**
	 * The mode for updating.
	 */
	UPDATE, 
	/**
	 * The mode for deletion.
	 */
	DELETE 
}

