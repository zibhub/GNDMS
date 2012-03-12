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

package de.zib.gndms.taskflows.esgfStaging.client.tools;

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
public class ESGFStagingOrderProperties {

    public final static DocumentedKey ESGF_STAGING_URL;
    public final static DocumentedKey ESGF_STAGING_CHECKSUM;
    public final static List<DocumentedKey> ESGF_STAGING_KEYS;

    static {
        ArrayList< DocumentedKey > keys = new ArrayList< DocumentedKey >( 2 );
        ESGF_STAGING_URL = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.ESGFStagingRequest.URL",
                "URL of a file for an ESGF request"
        );
        ESGF_STAGING_CHECKSUM = DocumentedKey.createAndRegisterKey(
                keys,
                "c3grid.ESGFStagingRequest.Checksum",
                "Checksum of a file"
        );
        ESGF_STAGING_KEYS = Collections.unmodifiableList( keys );
    }


    public static void createTemplate( PrintWriter out ) {
        for ( DocumentedKey dk : ESGF_STAGING_KEYS )
            dk.asPropertiesTemplate( out );
    }

    // no instance of this one
    private ESGFStagingOrderProperties() {
    }
}
