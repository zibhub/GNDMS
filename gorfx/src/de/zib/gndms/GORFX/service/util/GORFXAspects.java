/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.GORFX.service.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @date: 30.03.12
 * @time: 07:48
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
@Aspect
public class GORFXAspects {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut( "execution(* de.zib.gndms.GORFX.service.TaskServiceImpl.*(..))" )
    public void inTaskServiceImpl() {}

    @Pointcut( "execution(* de.zib.gndms.GORFX.service.TaskFlowServiceImpl.*(..))" )
    public void inTaskFlowServiceImpl() {}

    @Around( value = "inTaskServiceImpl() && args( taskId, .., String )" )
    public void logTaskId( ProceedingJoinPoint pjp, final String taskId ) throws Throwable {
        MDC.put( "TaskID", taskId );

        pjp.proceed();

        MDC.remove( "TaskID" );
    }

    @Around( value = "inTaskFlowServiceImpl() && args( type, taskFlowId, .., dn, wID )" )
    public void logTaskFlowId( ProceedingJoinPoint pjp,
                               final String type,
                               final String taskFlowId,
                               final String dn,
                               final String wID ) throws Throwable {
        MDC.put( "TaskFlowType", type ) ;
        MDC.put( "TaskFlowID", taskFlowId ) ;
        MDC.put( "WorkFlowID", wID ) ;

        pjp.proceed();

        MDC.remove( "TaskFlowType" );
        MDC.remove( "TaskFlowID" );
        MDC.remove( "WorkFlowID" );
    }
}
