package de.zib.gndms.gndmc.gorfx;
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

import de.zib.gndms.GORFX.service.TaskService;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import org.springframework.http.ResponseEntity;

/**
 * @author try ma ik jo rr a zib
 * @date 07.03.11  18:35
 * @brief
 */
public class TaskClient extends AbstractClient implements TaskService {

    public TaskClient() {
    }


    public TaskClient( String serviceURL ) {
        super( serviceURL );
    }


    public ResponseEntity<TaskServiceInfo> getServiceInfo() {
        return null;  // not required here
    }


    public ResponseEntity<TaskServiceConfig> getServiceConfig( String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<String> setServiceConfig( TaskServiceConfig cfg, String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<Facets> getTaskFacets( String id, String dn ) {
        return null;  // not required here
    }


    public ResponseEntity<Void> deleteTask( String id, String dn, String wid ) {
        return null;  // not required here
    }


    public ResponseEntity<TaskStatus> getStatus( String id, String dn, String wid ) {
        return null;  // not required here
    }


    public ResponseEntity<Void> changeStatus( String id, TaskControl status, String dn, String wid ) {
        return null;  // not required here
    }


    public ResponseEntity<TaskResult> getResult( String id, String dn, String wid ) {
        return null;  // not required here
    }


    public ResponseEntity<TaskFailure> getErrors( String id, String dn, String wid ) {
        return null;  // not required here
    }
}
