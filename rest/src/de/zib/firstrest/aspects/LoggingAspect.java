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


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 26.07.11  15:43
 *
 * @brief
 */
@Aspect
public final class LoggingAspect {

    protected Logger logger = LoggerFactory.getLogger( this.getClass() );


    @Around( "de.zib.firstrest.aspects.SomePointcuts.serviceImplPublicMethod()" )
    public Object loggingAspect( ProceedingJoinPoint joinPoint ) throws Throwable {
        Signature sig = joinPoint.getSignature();
        logger.debug( "**** pre: " + sig.getDeclaringType().getName() + " and " +
                sig.getName() );
        Object res = joinPoint.proceed();
        logger.debug( "**** post: " + sig.getDeclaringType().getName() + " and " +
                sig.getName() );

        return res;
    }
}
