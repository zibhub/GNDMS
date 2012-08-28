/*
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

package de.zib.gndms.common.model.gorfx.types.io;

import de.zib.gndms.stuff.misc.DocumentedKey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @date: 28.08.12
 * @time: 10:06
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SliceOrderProperties {
    public final static DocumentedKey SLICE_ID;
    public final static DocumentedKey SLICE_SIZE;
    public final static DocumentedKey SLICE_TERMINATION_TIME;

    public final static List< DocumentedKey > SLICE_KEYS;

    static {
        ArrayList< DocumentedKey > keys = new ArrayList< DocumentedKey >( 2 );
        SLICE_ID = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.SliceRequest.id",
                "SliceId of the slice for the request."
        );
        SLICE_SIZE = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.SliceRequest.size",
                "Size of the slice for request."
        );
        SLICE_TERMINATION_TIME = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.SliceRequest.terminationTime",
                "Termination time of the slice for request."
        );
        SLICE_KEYS = Collections.unmodifiableList( keys );
    }


    public static void createTemplate( PrintWriter out ) {
        for ( DocumentedKey dk : SLICE_KEYS )
            dk.asPropertiesTemplate( out );
    }
}
