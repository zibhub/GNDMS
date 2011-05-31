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
import de.zib.gndms.infra.system.SysTaskExecutionService;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.UUID;

/**
* @author try ma ik jo rr a zib
* @date 30.05.11  16:38
* @brief
*/
public class TaskServiceAux<T extends Serializable> {

    private boolean myResult;
    private String type;
    private String id;
    private String dn;
    private String wid;
    private T taskModel;
    private Task task;
    private TaskAction taskAction;
    private ResponseEntity<Specifier<Facets>> res;
    private SysTaskExecutionService executorService;
    private Dao dao;
    private Logger logger = LoggerFactory.getLogger( this.getClass() );


    public TaskServiceAux( String type, String id, TaskAction taskAction, T model, String dn, String wid ) {
        this.type = type;
        this.id = id;
        this.taskAction = taskAction;
        this.taskModel = taskModel;
        this.dn = dn;
        this.wid = wid;
    }


    boolean is() {
        return myResult;
    }


    public ResponseEntity<Specifier<Facets>> getRes() {
        return res;
    }


    public TaskServiceAux invoke() {

        String id = UUID.randomUUID().toString();
        dao.createTask( id );
        Session ses = dao.beginSession();
        task = ses.findTask( id );
        task.setPayload( taskModel );
        taskAction.setModel( task.getTaskling() );
        executorService.submitDaoAction( taskAction, dao );
        logger.debug( "Done, requesting facets" );
        res = getTask( type, id, dn, wid );
        if( HttpStatus.OK.equals( res.getStatusCode() ) ) {
            logger.debug( "OK, returning CREATED" );
            WidAux.removeWid();
            myResult = true;
            return this;
        } else {
          logger.debug( "unexpected status: " + res.getStatusCode().name() );
        }
        myResult = false;
        return this;
    }
}
