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

package de.zib.gndms.dspace.service.utils;

import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.common.types.GNDMSUserDetailsInterface;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.util.TxFrame;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @date: 29.03.12
 * @time: 11:19
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
@Aspect
public class DSpaceAspects {
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    final private SliceProvider sliceProvider;
    final private SliceKindProvider sliceKindProvider;
    final private SubspaceProvider subspaceProvider;
    private EntityManagerFactory emf;

    public DSpaceAspects(
            SliceProvider sliceProvider,
            SliceKindProvider sliceKindProvider,
            SubspaceProvider subspaceProvider ) {
        this.sliceProvider = sliceProvider;
        this.sliceKindProvider = sliceKindProvider;
        this.subspaceProvider = subspaceProvider;
    }

    @Pointcut( "execution(* de.zib.gndms.dspace.service.SliceServiceImpl.*(..))" )
    public void inSliceServiceImpl() {}

    @Pointcut( "execution(* de.zib.gndms.dspace.service.SliceKindServiceImpl.*(..))" )
    public void inSliceKindServiceImpl() {}

    @Pointcut( "execution(* de.zib.gndms.dspace.service.SubspaceServiceImpl.*(..))" )
    public void inSubspaceServiceImpl() {}

    @Around( value = "inSliceServiceImpl() && args( subspaceId, sliceKindId, sliceId, .. )" )
    public Object handleSlice( final ProceedingJoinPoint pjp,
                               final String subspaceId, final String sliceKindId, final String sliceId )
            throws NoSuchElementException, Throwable
    {
        // check Rights
        {
            final Slice slice = sliceProvider.getSlice( subspaceId, sliceId );

            final GNDMSUserDetailsInterface userDetails = ( GNDMSUserDetailsInterface )SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            final String dn = userDetails.getUsername();
            final String owner = slice.getOwner();

            // TODO: check for user / group rights here
            // ATTENTION: group can be null

            if( ! owner.equals( dn ) ) {
                logger.debug( "User " + dn + " tried to access slice " + sliceId + ", owned by " + slice.getOwner() + "." );
                throw new UnauthorizedException( "User " + dn + " does not own slice " + sliceId + "." );
            }
        }

        Object returnvalue = pjp.proceed();
        
        // extend termination time, if it had been published
        {
            EntityManager entityManager = emf.createEntityManager();
            final TxFrame txf = new TxFrame( entityManager );
            try {
                final Slice slice = entityManager.find(Slice.class, sliceId);
                if( slice.getPublished() ) {
                    final DateTime now = new DateTime( DateTimeUtils.currentTimeMillis() );
                    final DateTime month = now.plusMonths( 1 );
                    
                    if( slice.getTerminationTime().isBefore( month ) ) {
                        slice.setTerminationTime( month );
                    }
                }
                txf.commit();
            }
            finally { txf.finish();  }
        }
        
        return returnvalue;
    }

    @Before( value = "( inSliceKindServiceImpl() || inSliceServiceImpl() ) && args( subspaceId, sliceKindId, .. )" )
    public void checkSliceKindExistence( final String subspaceId, final String sliceKindId ) {
        if( !sliceKindProvider.exists( subspaceId, sliceKindId ) ) {
            throw new NoSuchResourceException( "SliceKind: " + sliceKindId );
        }
    }


    @Around(
            value = "( inSubspaceServiceImpl() || inSliceKindServiceImpl() || inSliceServiceImpl() ) " +
                    "&& args( subspaceId, .. )"
    )
    public Object checkSubspaceExistence( final ProceedingJoinPoint pjp, final String subspaceId ) throws Throwable {
        if( pjp.getSignature().getName().equals( "createSubspace" ) ) {
            if( subspaceProvider.exists( subspaceId ) ) {
                // TODO: this should be another exception...
                throw new UnauthorizedException( "Subspace " + subspaceId + " does already exist." );
            }
        }
        else {
            if( !subspaceProvider.exists( subspaceId ) ) {
                throw new NoSuchResourceException( "Subspace: " + subspaceId );
            }
        }

        return pjp.proceed();
    }
    
    
    public void setEntityManagerFactory( final EntityManagerFactory entityManagerFactory ) {
        this.emf = entityManagerFactory;
    }
}
