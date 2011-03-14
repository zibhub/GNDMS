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

import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 *         Date: 09.02.11, Time: 11:35
 *
 * This is the essential interface of the GORFX service. Since its the services responsibility to create and manage
 * complex tasks ( hence called taskflows ) this interface only provides methods necessary for this job.
 *
 * The interface should be implemented by clients which aren't required to perform tasks like system configuration
 * and maintenance.
 *
 * Some general remarks on the parameters:
 * <ul>
 *  <li> The <b>dn</b> parameter is a identifier for the user responsible for the method invocation. It is required to check if
 *       the user has the necessary permissions for the call.  <br>
 *          Using REST the attribute should be provided using a http-header property called "DN".
 *  <li> <b>wid</b> stands for Workflow-ID and is used to associate calls to a single workflow mainly for logging and
 *      debugging. <br>
 *          Using REST the attribute should be provided using a http-header property "wid".
 * </ul>
 */
public interface GORFXServiceEssentials {

    /**
     * Lists all facets of the service.
     *
     * @param dn The dn of the user invoking the method.
     * @return The Facets.
     */
    ResponseEntity<Facets> listAvailableFacets( String dn );

    /**
     * Delivers a list of all supported taskflows.
     *
     * @param dn The dn of the user invoking the method.
     * @return A list of taskflow names.
     */
    ResponseEntity<List<String>> listTaskFlows( String dn );

    /**
     * Get information about a specific taskflow.
     *
     * Information include a description of the task flow, and some possible usage statistics.
     *
     * \note This method is intended for human interaction, e.g. rendering the response using jsp.
     *
     * @param type Type name of the taskflow.
     * @param dn The dn of the user invoking the method.
     * @return Some aggregated info about a task flow.
     */
    ResponseEntity<TaskFlowInfo> getTaskFlowInfo( String type, String dn );

    /**
     * Instantiates  a taskflow.
     *
     * @param type Type name of the taskflow.
     * @param order The order consists parameters for the taskflow.
     * @param dn The dn of the user invoking the method.
     * @param wid The workflow id.
     * @return The specifier of the newly created task, with HTTPStatus created.
     */
    ResponseEntity<Specifier<Facets>> createTaskFlow( String type, AbstractTF order, String dn, String wid );
}
