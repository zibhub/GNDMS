package de.zib.gndms.kit.config;

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
 * Thrown by CommandAction.getOption()
 *
 * @see
 * @see logic.CommandAction
 * 
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 18:06:09
 */
public class MandatoryOptionMissingException extends Exception {
    private static final long serialVersionUID = -8091452559507426974L;


    public MandatoryOptionMissingException() {
        super();
    }


    public MandatoryOptionMissingException(final String message) {
        super(message);
    }


    public MandatoryOptionMissingException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public MandatoryOptionMissingException(final Throwable cause) {
        super(cause);
    }
}
