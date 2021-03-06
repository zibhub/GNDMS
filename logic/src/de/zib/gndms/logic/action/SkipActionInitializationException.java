package de.zib.gndms.logic.action;

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
 * Used to skip action initialization and executing; i.e. when printing a help screen.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 17:56:50
 */
public class SkipActionInitializationException extends ActionInitializationException {
    private static final long serialVersionUID = 5225015991599636796L;


    public SkipActionInitializationException() {
    }


    public SkipActionInitializationException(final String message) {
        super(message);
    }


    public SkipActionInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public SkipActionInitializationException(final Throwable cause) {
        super(cause);
    }
}
