package de.zib.gndms.logic.dspace;

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
 * The no such element exception, which is thrown when a collection does not contain 
 * a requested element.
 * 
 * @author Ulrike Golas
 */

public class NoSuchElementException extends Exception {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6714125977083001944L;

	/**
	 * The constructor.
	 */
    public NoSuchElementException() {
    }

    /**
     * The constructor.
     * @param message The exception message.
     */
    public NoSuchElementException(final String message) {
        super(message);
    }

}
