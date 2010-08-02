package de.zib.gndms.logic.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
 * ThingAMagic.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 13.08.2008 Time: 10:32:17
 */
public class ActionInitializationException extends RuntimeException {
	private static final long serialVersionUID = 161746780645573512L;


	public ActionInitializationException() {
		super();
	}


	public ActionInitializationException(final String message) {
		super(message);
	}


	public ActionInitializationException(final String message, final Throwable cause) {
		super(message, cause);
	}


	public ActionInitializationException(final Throwable cause) {
		super(cause);
	}
}
