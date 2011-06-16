package de.zib.gndms.GORFX.service;
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

import de.zib.gndms.GORFX.service.util.WidAux;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.TaskExecutionService;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import de.zib.gndms.rest.UriFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
* @author try ma ik jo rr a zib
* @date 30.05.11  16:38
* @brief
*/
public class TaskServiceAux {

    private TaskExecutionService executorService;
    private Logger logger = LoggerFactory.getLogger( this.getClass() );


    public TaskServiceAux( TaskExecutionService executorService ) {

        this.executorService = executorService;
    }



    public Taskling submitTaskAction( Dao dao, TaskAction taskAction, Serializable taskModel, String wid ) {

        String id = UUID.randomUUID().toString();
        Taskling taskling = null;
        dao.createTask( id );
        Session ses = dao.beginSession();
        try {
            Task task = ses.findTask( id );
            task.setWID( wid );
            task.setPayload( taskModel );
            taskling = task.getTaskling();
            ses.success();
        } finally {
            ses.finish();
        }

        taskAction.setModel( taskling );
        taskAction.setOwnDao( dao );
        executorService.submitDaoAction( taskAction, null );

        return taskling;
    }


    public Specifier<Facets> getTaskSpecifier( TaskClient taskClient, Taskling taskling, UriFactory uriFactory, Map<String,String> urimap, String dn ) {

        Specifier<Facets> spec = new Specifier<Facets>();

        Map<String, String> taskurimap = taskUriMap( taskling, urimap );

        spec.setURL( uriFactory.taskUri( urimap, null ) );
        spec.setUriMap( urimap );

        ResponseEntity<Facets> res = taskClient.getTaskFacets( taskling.getId(), dn );
        if( res != null )
            if( HttpStatus.OK.equals( res.getStatusCode() ) ) {
                spec.setPayload( res.getBody() );
            } else {
                throw new IllegalStateException( "unexpected status: " + res.getStatusCode().name() );
            }
        else
            logger.debug( "getTaskFacets returned null" );

        return spec;
    }


    public static Map<String, String> taskUriMap( Taskling t, Map<String,String> urimap ) {

        HashMap<String,String> newUrimap = new HashMap<String, String>( 2 );
        newUrimap.put( UriFactory.SERVICE, "gorfx" );
        newUrimap.put( UriFactory.TASK_ID, t.getId() );

        if ( urimap != null )
            newUrimap.putAll( urimap ); // this might overwrite TASK_ID and SERVICE

        return newUrimap;
    }
}
