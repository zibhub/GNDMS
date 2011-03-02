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

import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author try ma ik jo rr a zib
 * @date 01.03.11  12:06
 * @brief
 */
@Controller
@RequestMapping( "/gorfx/tasks" )
public class TaskServiceImpl implements TaskService {

    private TaskDao taskDao;

    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ResponseEntity<TaskServiceInfo> getServiceInfo() {
        return new ResponseEntity<TaskServiceInfo>( new TaskServiceInfo(), null, HttpStatus.OK );
    }


    @RequestMapping( value = "/config", method = RequestMethod.GET )
    public ResponseEntity<TaskServiceConfig> getServiceConfig( @RequestHeader String dn ) {
        throw new NotImplementedException();
    }


    @RequestMapping( value = "/config", method = RequestMethod.POST )
    public ResponseEntity<String> setServiceConfig( @RequestBody TaskServiceConfig cfg, @RequestHeader String dn ) {
        throw new NotImplementedException();
    }


    @RequestMapping( value = "/_{id}", method = RequestMethod.GET )
    public ResponseEntity<Facets> getTaskFacets( @PathVariable String id, @RequestHeader( "DN" ) String dn ) {
        return null;
    }

    @RequestMapping( value = "/_{id}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteTask( @PathVariable String id, @RequestHeader( "DN" ) String dn,
                                              @RequestHeader( "WId" ) String wid ) {
        return null;
    }


    @RequestMapping( value = "/_{id}/status", method = RequestMethod.GET )
    public ResponseEntity<TaskStatus> getStatus( @PathVariable String id, @RequestHeader( "DN" ) String dn,
                                                 @RequestHeader( "WId" ) String wid ) {

        // Actual Task Error URL (Maybe as Redirect)
        return null;
    }

    @RequestMapping( value = "/_{id}/status", method = RequestMethod.POST )
    public ResponseEntity<Void> changeStatus( @PathVariable String id, @RequestBody TaskControl status,
                                              @RequestHeader( "DN" ) String dn,
                                              @RequestHeader( "WId" ) String wid ) {

        // Actual Task Error URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/_{id}/result", method = RequestMethod.GET )
    public ResponseEntity<TaskResult> getResult( @PathVariable String id, @RequestHeader( "DN" ) String dn,
                                                 @RequestHeader( "WId" ) String wid ) {

        // Actual Task Result URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/_{id}/errors", method = RequestMethod.GET )
    public ResponseEntity<TaskFailure> getErrors( @PathVariable String id,
                                                  @RequestHeader( "DN" ) String dn,
                                                  @RequestHeader( "WId" ) String wid ) {

        // Actual Task Error URL (Maybe as Redirect)
        return null;
    }
}
