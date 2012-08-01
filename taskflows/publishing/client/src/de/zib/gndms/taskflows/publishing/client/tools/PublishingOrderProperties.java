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

package de.zib.gndms.taskflows.publishing.client.tools;

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
public class PublishingOrderProperties {

    public final static DocumentedKey PUBLISHING_SLICE;
    public final static List<DocumentedKey> PUBLISHING_KEYS;

    static {
        ArrayList< DocumentedKey > keys = new ArrayList< DocumentedKey >( 1 );
        PUBLISHING_SLICE = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.PublishingRequest.Slice",
                "SliceId of the slice to publish"
        );
        PUBLISHING_KEYS = Collections.unmodifiableList( keys );
    }


    public static void createTemplate( PrintWriter out ) {
        for ( DocumentedKey dk : PUBLISHING_KEYS )
            dk.asPropertiesTemplate( out );
    }

    // no instance of this one
    private PublishingOrderProperties() {
    }
}
