/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.failure.client.tools;

import de.zib.gndms.stuff.misc.DocumentedKey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @date: 12.03.12
 * @time: 16:13
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class FailureOrderProperties {

    public final static DocumentedKey FAILURE_WHERE;
    public final static DocumentedKey FAILURE_BEFORE_SUPER;
    public final static DocumentedKey FAILURE_THROW_IN_SESSION;
    public final static DocumentedKey FAILURE_SLEEP_BEFORE;
    public final static DocumentedKey FAILURE_SLEEP_AFTER;
    public final static List<DocumentedKey> FAILURE_KEYS;

    static {
        ArrayList< DocumentedKey > keys = new ArrayList< DocumentedKey >( 5 );
        FAILURE_WHERE = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FailureRequest.where",
                "Where to throw an Exception in processing a task."
        );
        FAILURE_BEFORE_SUPER = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FailureRequest.beforeSuper",
                "Throw the exception before calling the super function (ignore, if not sure)."
        );
        FAILURE_THROW_IN_SESSION = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FailureRequest.",
                "Throw the exception while having a database session opened."
        );
        FAILURE_SLEEP_BEFORE = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FailureRequest.sleepBefore",
                "Sleep n milliseconds before calling the super function."
        );
        FAILURE_SLEEP_AFTER = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.FailureRequest.sleepAfter",
                "Sleep n milliseconds after calling the super function."
        );
        FAILURE_KEYS = Collections.unmodifiableList( keys );
    }


    public static void createTemplate( PrintWriter out ) {
        for ( DocumentedKey dk : FAILURE_KEYS )
            dk.asPropertiesTemplate( out );
    }

    // no instance of this one
    private FailureOrderProperties() {
    }
}
