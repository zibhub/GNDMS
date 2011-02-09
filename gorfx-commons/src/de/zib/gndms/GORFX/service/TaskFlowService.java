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

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFlowFailure;
import de.zib.gndms.model.gorfx.types.TaskFlowResult;
import de.zib.gndms.model.gorfx.types.TaskFlowStatus;
import de.zib.gndms.rest.Facets;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 *         Date: 09.02.11, Time: 18:45
 */
public interface TaskFlowService {

    ResponseEntity<Facets> getFacets( @PathVariable String type, @PathVariable String id );

    ResponseEntity<AbstractTF> getOrder( @PathVariable String type, @PathVariable String id );

    ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id,
                                   @RequestBody AbstractTF orq );

    ResponseEntity<List<TransientContract>> getQuotes( @PathVariable String type, @PathVariable String id );

    ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id, int idx,
                                   @RequestBody TransientContract cont );

    ModelAndView getQuotes( @PathVariable String type, @PathVariable String id, @PathVariable int idx );

    ResponseEntity<Void> deleteQuotes( @PathVariable String type, @PathVariable String id,
                                       @PathVariable int idx );

    ResponseEntity<String> getTask( @PathVariable String type, @PathVariable String id );

    ResponseEntity<String> createTask( @PathVariable String type, @PathVariable String id,
                                       @RequestParam( value = "quote", required = false ) String quoteId );

    ResponseEntity<TaskFlowStatus> getStatus( @PathVariable String type, @PathVariable String id );

    ResponseEntity<TaskFlowResult> getResult( @PathVariable String type, @PathVariable String id );

    ResponseEntity<TaskFlowFailure> getErrors( @PathVariable String type, @PathVariable String id );
}
