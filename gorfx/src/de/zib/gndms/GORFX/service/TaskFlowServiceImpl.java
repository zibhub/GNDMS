package de.zib.gndms.GORFX.service;

import com.sun.deploy.util.StringQuoteUtil;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.repository.ORQDao;
import de.zib.gndms.model.gorfx.repository.TypedUUId;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 13.01.2011, Time: 15:17:47
 *
 * A controller for a REST taskflow resource which implements the
 * TaskFlowService interface.
 *
 * The taskflow service acts as interface to instantiated taskflow
 * resouces. The instantitiation or creation happens through the GORFX
 * service itself.
 *
 * This implicates that all method invocation concern a single
 * taskflow, not the service as a whole.
 */
@Controller
@RequestMapping( "/gorfx" )
public class TaskFlowServiceImpl implements TaskFlowService {

    private ORQDao orqDao;
    private String serviceUrl; // inject or read from properties

    @RequestMapping( value = "/{type}/{id}", method = RequestMethod.GET )
    public ResponseEntity<Facets> getFacets( @PathVariable String type, @PathVariable String id ) {

        return null;
    }


    @RequestMapping( value = "/{type}/{id}/order", method = RequestMethod.GET )
    public ResponseEntity<AbstractTF> getOrder( @PathVariable String type, @PathVariable String id ) {
        TypedUUId uid = orqDao.create( type );
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/order", method = RequestMethod.POST )
    public ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id,
                                          @RequestBody AbstractTF orq ) {

        return null;
    }


    @RequestMapping( value = "/{type}/{id}/quotes", method = RequestMethod.GET )
    public ResponseEntity<List<TransientContract>> getQuotes( @PathVariable String type, @PathVariable String id ) {
        // retrieve a list
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/quotes/", method = RequestMethod.POST )
    public ResponseEntity<Void> setOrder( @PathVariable String type, @PathVariable String id, int idx,
                                          @RequestBody TransientContract cont ) {
        // add an desired quotation for the order
        return null;
    }




    @RequestMapping( value = "/{type}/{id}/quotes/{idx}", method = RequestMethod.GET )
    public ModelAndView getQuotes( @PathVariable String type, @PathVariable String id, @PathVariable int idx ) {
        // retrieve desired quotes for the order.
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/quotes/{idx}", method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteQuotes( @PathVariable String type, @PathVariable String id,
                                              @PathVariable int idx ) {
        // retrieve desired quotes for the order.
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/task/", method = RequestMethod.GET )
    public ResponseEntity<String> getTask( @PathVariable String type, @PathVariable String id ) {
        // redirects to the task of the taskflow, creates this task if it doesn't exists.
        boolean taskExists=true;
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        ResponseEntity<String> resp;
        if ( taskExists )
            resp = new ResponseEntity<String>( "the string URL", responseHeaders, HttpStatus.FOUND );
        else
            resp = new ResponseEntity<String>( null, responseHeaders, HttpStatus.NOT_FOUND );

        return resp;
    }


    @RequestMapping( value = "/{type}/{id}/task/", method = RequestMethod.PUT )
    public ResponseEntity<String> createTask( @PathVariable String type, @PathVariable String id,
                                              @RequestParam( value = "quote", required = false ) String quoteId ) {
        // redirects to the task of the taskflow, creates this task if it doesn't exists.
        GNDMSResponseHeader responseHeaders = new GNDMSResponseHeader();
        return new ResponseEntity<String>( "the string URL", responseHeaders, HttpStatus.CREATED );
    }


    @RequestMapping( value = "/{type}/{id}/status", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowStatus> getStatus( @PathVariable String type, @PathVariable String id ) {

        // Actual Task Error URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/result", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowResult> getResult( @PathVariable String type, @PathVariable String id ) {

            // Actual Task Result URL (Maybe as Redirect)
        return null;
    }


    @RequestMapping( value = "/{type}/{id}/errors", method = RequestMethod.GET )
    public ResponseEntity<TaskFlowFailure> getErrors( @PathVariable String type, @PathVariable String id ) {

		// Actual Task Error URL (Maybe as Redirect)
        return null;
    }
}
