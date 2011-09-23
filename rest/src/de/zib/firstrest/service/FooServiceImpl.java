package de.zib.firstrest.service;

import de.zib.firstrest.aspects.NoSuchTaskFlowTypeException;
import de.zib.firstrest.domain.Foo;
import de.zib.firstrest.repository.FooDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
 *          Date: 21.12.2010, Time: 11:45:44
 */
@Controller
public class FooServiceImpl implements ServletContextAware, FooService {

    private FooDao dao;
    private ServletContext servletContext;
    private Logger logger = LoggerFactory.getLogger( this.getClass() );

    @PostConstruct
    public void init( ) {
        logger.info(  servletContext.getRealPath( "" ) );
        logger.info(  servletContext.getServerInfo( ) );
    }

    @Override
    @RequestMapping( value = "/foo/{fid}" )
    public ResponseEntity<Foo> findFoo( @PathVariable String fid ) {

    /*
     * Note: This method together with the dispacher-context.xml
     * contains changes to test content-type dispatching which aren't
     * currently working, however futher testing is delayed due to
     * other tasks.
     */
        final Foo foo = dao.getFoo( fid );
        return new ResponseEntity<Foo>( foo, HttpStatus.OK );
//        if( foo != null )
//            mv = new ModelAndView( "xmlMarshall", "foo", foo );
//        else {
//            RedirectView rv = new RedirectView( );
//            rv.setStatusCode( HttpStatus.NOT_FOUND );
//            mv = new ModelAndView( rv );
//        }
//
//        return mv;
    }

    @Override
    @RequestMapping( value = "/allfoo" )
    public ResponseEntity<List<Foo>> allFoo() {
        return new ResponseEntity<List<Foo>>( dao.getAllFoo(), HttpStatus.OK  );
    }
    

    @Override
    @RequestMapping( value = "/newfoo", method = RequestMethod.POST)
    public View savePerson( @RequestBody Foo foo ) {
        logger.info( "post with: " + foo.getId() + ":" + foo.getValue() ) ;
        dao.putFoo( foo );
        
        return new RedirectView("/foo/" + foo.getId());
    }

    
    public FooDao getDao() {
        return dao;
    }


    @Autowired
    public void setDao( FooDao dao ) {
        this.dao = dao;
    }


    public void setServletContext( ServletContext servletContext ) {
        this.servletContext = servletContext;
    }


    @ExceptionHandler( NoSuchTaskFlowTypeException.class )
    public void handleNoSuchTaskFlowTypeException( NoSuchTaskFlowTypeException ex, HttpServletResponse response ) {

        logger.warn( "exception handler for " + ex.getMessage() );
        try {
            //response.setStatus( HttpStatus.INTERNAL_SERVER_ERROR.value() );
            //response.getOutputStream().print( ex.getMessage() );
            response.sendError( HttpStatus.NOT_FOUND.value(), ex.getMessage() );
        } catch ( IOException e ) {
            response.setStatus( HttpStatus.INTERNAL_SERVER_ERROR.value() );
        }
    }
}
