/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.firstrest.aspects;

import de.zib.firstrest.repository.FooDao;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 28.07.11  16:10
 * @brief
 */
@Aspect
public class CheckingAspect {

    private FooDao dao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Before( "de.zib.firstrest.aspects.SomePointcuts.serviceImplPublicMethod()" +
        "&& args(fid)" )
    public void checkFid( String fid ) throws NoSuchTaskFlowTypeException {

        logger.debug( "*** looking up "+ fid );
        if ( dao.getFoo( fid ) == null )
            throw new NoSuchTaskFlowTypeException( fid );
    }


    @Autowired
    public void setDao( FooDao dao ) {
        this.dao = dao;
    }
}
