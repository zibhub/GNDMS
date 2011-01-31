package de.zib.gndms.GORFX.service;

import de.response.TaskFlowResponse;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.repository.ORQDao;
import de.zib.gndms.model.gorfx.repository.TypedUUId;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
 */
@Controller
@RequestMapping( "/gorfx" )
public class TaskFlowService {

    private ORQDao orqDao;
    private String serviceUrl; // inject or read from properties

    @RequestMapping( value = "/{kind}/{id}", method = RequestMethod.GET )
    public @ResponseBody TaskFlowResponse getInfo( @PathVariable String kind, @PathVariable String id ) {
        TaskFlowResponse res = new TaskFlowResponse( serviceUrl + "/" + kind + "/" + id, serviceUrl );
        
        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/order", method = RequestMethod.GET )
    public ModelAndView getOrder( String kind, String id ) {
        TypedUUId uid = orqDao.create( kind );
        return new ModelAndView( "testXmlView", BindingResult.MODEL_KEY_PREFIX + "key", uid );
    }


    @RequestMapping( value = "/{kind}/{id}/order", method = RequestMethod.POST )
    public ModelAndView setOrder( String kind, String id, @RequestBody AbstractORQ orq ) {

        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/quotes", method = RequestMethod.GET )
    public ModelAndView getQuotes( String kind, String id ) {
        // retrieve a list
        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/quotes/{idx}", method = RequestMethod.GET )
    public ModelAndView getQuotes( String kind, String id, int idx ) {
        // retrieve desired quotes for the order.
        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/quotes/{idx}", method = RequestMethod.POST )
    public ModelAndView setOrder( String kind, String id, int idx, @RequestBody TransientContract cont ) {
        // add an desired quotation for the order
        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/task/", method = RequestMethod.POST )
    public ModelAndView getTask( String kind, String id ) {
        // redirects to the task of the taskflow, creates this task if it doesn't exists.
        return null;
    }


    @RequestMapping( value = "/{kind}/{id}/result", method = RequestMethod.GET )
    public ModelAndView getResult( String kind, String id ) {

            // Actual Task Result URL (Maybe as Redirect)
        return null;
    }
    

    @RequestMapping( value = "/{kind}/{id}/errors", method = RequestMethod.GET )
    public ModelAndView getErrors( String kind, String id ) {

		// Actual Task Error URL (Maybe as Redirect)
        return null;
    }
}
