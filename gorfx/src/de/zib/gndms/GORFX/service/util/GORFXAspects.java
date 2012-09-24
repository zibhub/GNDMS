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

import de.zib.gndms.kit.util.WidAux;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

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

    @Pointcut( "execution(* de.zib.gndms.GORFX.service.GORFXServiceImpl.*(..))" )
    public void inGORFXServiceImpl() {}

    @Pointcut( "inTaskServiceImpl()  || inTaskFlowServiceImpl() || inGORFXServiceImpl()" )
    public void inGorfxCall() {}

    @Around( value = "inGorfxCall()" )
    public Object putGorfxId( ProceedingJoinPoint pjp ) throws Throwable {
        
        WidAux.initGORFXid( UUID.randomUUID().toString() );
        
        final Object ret = pjp.proceed();

        WidAux.removeGORFXid();
        
        return ret;
    }

    @Around( value = "inTaskServiceImpl() && args( taskId, .., String )" )
    public Object logTaskId( ProceedingJoinPoint pjp, final String taskId ) throws Throwable {
        MDC.put("TaskID", taskId);
        MDC.put( "ServiceCall", pjp.getSignature().toLongString() );

        final Object ret = pjp.proceed();

        MDC.remove( "TaskID" );
        MDC.remove( "ServiceCall" );

        return ret;
    }

    @Around( value = "inTaskFlowServiceImpl() && args( type, taskFlowId, .., dn, wID )" )
    public Object logTaskFlowId( ProceedingJoinPoint pjp,
                               final String type,
                               final String taskFlowId,
                               final String dn,
                               final String wID ) throws Throwable {
        MDC.put( "TaskFlowType", type ) ;
        MDC.put( "TaskFlowID", taskFlowId ) ;
        MDC.put( "WorkFlowID", wID ) ;
        MDC.put( "ServiceCall", pjp.getSignature().toLongString() );

        final Object ret = pjp.proceed();

        MDC.remove( "TaskFlowType" );
        MDC.remove( "TaskFlowID" );
        MDC.remove( "WorkFlowID" );
        MDC.remove( "ServiceCall" );
        
        return ret;
    }

    @Around( value = "inGORFXServiceImpl()" )
    public Object logGORFXWithNewUUID( ProceedingJoinPoint pjp ) throws Throwable {
        MDC.put( "ServiceCall", pjp.getSignature().toLongString() );

        final Object ret = pjp.proceed();

        MDC.remove("GORFXCall");

        return ret;
    }
}
