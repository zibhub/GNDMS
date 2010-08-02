package de.zib.gndms.logic.model.gorfx;

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
 *          User: stepn Date: 27.10.2008 Time: 14:39:36
 */
public class UnfulfillableORQException extends RuntimeException {
    private static final long serialVersionUID = -7430762158076623931L;


    public UnfulfillableORQException() {
        super();
    }


    public UnfulfillableORQException(final String message) {
        super(message);
    }


    public UnfulfillableORQException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public UnfulfillableORQException(final Throwable cause) {
        super(cause);
    }
}
