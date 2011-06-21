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
import de.zib.gndms.stuff.devel.NotYetImplementedException;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.UriFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * @author try ma ik jo rr a zib
 * @date 07.03.11  18:35
 * @brief Client for TaskService.
 *
 * @see de.zib.gndms.GORFX.service.TaskService for details.
 */
public class TaskClient extends AbstractClient implements TaskService {

   private UriFactory uriFactory;


    public TaskClient() {
    }


    public TaskClient( String serviceURL ) {
        super( serviceURL );
        uriFactory = new UriFactory( serviceURL );
    }


    public ResponseEntity<TaskServiceInfo> getServiceInfo() {
        return unifiedGet( TaskServiceInfo.class,
            uriFactory.taskServiceUri( new HashMap<String,String> () {{put( "service", "gorfx" );}} ), null );
    }


    public ResponseEntity<TaskServiceConfig> getServiceConfig( String dn ) {
        return unifiedGet( TaskServiceConfig.class,
            uriFactory.taskServiceUri( new HashMap<String,String> () {{put( "service", "gorfx" );}} ), null );
    }


    public ResponseEntity<String> setServiceConfig( TaskServiceConfig cfg, String dn ) {
        throw new NotYetImplementedException();
    }


    public ResponseEntity<Facets> getTaskFacets( final String id, String dn ) {
        return unifiedGet( Facets.class,
            uriFactory.taskUri( new HashMap<String, String>() {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, null ), dn );
    }


    public ResponseEntity<Void> deleteTask( final String id, String dn, String wid ) {
        return unifiedDelete(
            uriFactory.taskUri( new HashMap<String, String>() {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, null ), dn, wid );
    }


    public ResponseEntity<TaskStatus> getStatus( final String id, String dn, String wid ) {
        return unifiedGet( TaskStatus.class,
            uriFactory.taskUri( new HashMap<String, String>() {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, "status" ), dn, wid );
    }


    public ResponseEntity<Void> changeStatus( final String id, TaskControl status, String dn, String wid ) {
        return unifiedPost( Void.class,
            uriFactory.taskUri( new HashMap<String, String>() {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, "status" ), dn, wid );
    }


    public ResponseEntity<TaskResult> getResult( final String id, String dn, String wid ) {
        return unifiedGet( TaskResult.class,
            uriFactory.taskUri( new HashMap<String,String> () {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, "result" ), dn, wid );
    }


    public ResponseEntity<TaskFailure> getErrors( final String id, String dn, String wid ) {
        return unifiedGet( TaskFailure.class,
            uriFactory.taskUri( new HashMap<String,String> () {{
                put( "service", "gorfx" );
                put( "taskId", id );
            }}, "errors" ), dn, wid );
    }


    @Override
    public void setServiceURL( String serviceURL ) {
        uriFactory = new UriFactory( serviceURL );
        super.setServiceURL( serviceURL );
    }
}
