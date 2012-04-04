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

import com.sun.servicetag.UnauthorizedAccessException;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.dspace.Slice;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Before( value = "inSliceServiceImpl() && args( subspaceId, sliceKindId, sliceId, .. )" )
    public void checkOwner( final String subspaceId, final String sliceKindId, final String sliceId )
            throws NoSuchElementException
    {
        final Slice slice = sliceProvider.getSlice( subspaceId, sliceId );
        final UserDetails userDetails = ( UserDetails )SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        final String dn = userDetails.getUsername();

        if( ! slice.getOwner().equals( dn ) ) {
            logger.debug( "User " + dn + " tried to access slice " + sliceId + ", owned by " + slice.getOwner() + "." );
            throw new UnauthorizedAccessException( "User " + dn + " does not own slice " + sliceId + "." );
        }
    }

    @Before( value = "( inSliceKindServiceImpl() || inSliceServiceImpl() ) && args( subspaceId, sliceKindId, .. )" )
    public void checkSliceKindExistence( final String subspaceId, final String sliceKindId ) {
        if( !sliceKindProvider.exists( subspaceId, sliceKindId ) ) {
            throw new NoSuchResourceException( "SliceKind: " + sliceKindId );
        }
    }

    @Before( value = "( inSubspaceServiceImpl() || inSliceKindServiceImpl() || inSliceServiceImpl() ) && args( subspaceId, .. )" )
    public void checkSubspaceExistence( final String subspaceId ) {
        if( !subspaceProvider.exists( subspaceId ) ) {
            throw new NoSuchResourceException( "Subspace: " + subspaceId );
        }
    }
}
