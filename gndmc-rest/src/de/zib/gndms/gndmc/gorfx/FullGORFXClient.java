package de.zib.gndms.gndmc.gorfx;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.GORFX.service.GORFXService;
import de.zib.gndms.kit.config.ActionMeta;
import de.zib.gndms.kit.config.ConfigMeta;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 08.02.11, Time: 12:10
 */
public class FullGORFXClient extends GORFXClient implements GORFXService {

    /// \note no gorfx foo in the url

    public FullGORFXClient() {
    }


    public FullGORFXClient( String serviceURL ) {
        super( serviceURL );
    }


    public ResponseEntity<List<String>> listConfigActions( String dn ) {
        return (ResponseEntity<List<String>>) unifiedGet( List.class, getServiceURL() + "/gorfx/config", dn );
    }


    public ResponseEntity<ConfigMeta> getContigActionInfo( String actionName, String dn ) {
        return unifiedGet( ConfigMeta.class, getServiceURL() + "/gorfx/config/" + actionName, dn );
    }


    public ResponseEntity<String> callConfigAction( String actionName, String args, String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<List<String>> listBatchActions( String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<ActionMeta> getBatchActionInfo( String actionName, String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<String> callBatchAction( String actionName, String args, String dn ) {
        return null;  // not required here
    }
}
