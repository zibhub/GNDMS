package de.zib.gndms.dspace.slice.service;

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



import de.zib.gndms.gritserv.util.LogAux;
import de.zib.gndms.dspace.slice.service.globus.resource.ExtSliceResourceHome;
import de.zib.gndms.dspace.slice.service.globus.resource.SliceResource;
import de.zib.gndms.dspace.stubs.types.InternalFailure;
import de.zib.gndms.dspace.stubs.types.UnknownSubspace;
import de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
import de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.dspace.TransformSliceAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.apache.log4j.Logger;
import org.globus.wsrf.ResourceKey;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceImpl extends SliceImplBase {

    protected Logger logger = Logger.getLogger( this.getClass() );

    private static final String querySupportedSliceKind =
            "SELECT sk FROM MetaSubspaces x INNER JOIN x.creatableSliceKinds sk WHERE sk.URI = :uriParam AND" +
            " x.scopedName.nameScope = (SELECT y.metaSubspace.scopedName.nameScope FROM Subspaces y WHERE y.id = :idParam)" +
            " AND x.scopedName.localName = (SELECT y.metaSubspace.scopedName.localName FROM Subspaces y WHERE y.id = :idParam)";

    public SliceImpl() throws RemoteException {
		super();
	}
	

    public de.zib.gndms.dspace.slice.stubs.types.SliceReference transformSliceTo(types.SliceTransformSpecifierT sliceTransformSpecifier,types.ContextT context) throws RemoteException, UnknownSubspace, OutOfSpace, UnknownOrInvalidSliceKind, InternalFailure {

        LogAux.logSecInfo( logger, "transformSliceTo" );

        ExtSliceResourceHome esr;
        SliceResource sr;
        try {
            esr = (ExtSliceResourceHome) getResourceHome(  );
            sr = esr.getAddressedResource();
        } catch ( Exception e ) {
            throw new RemoteException( "Can't obtain slice resource home.", e );
        }

        EntityManager em = null;
        try{
            GNDMSystem system = esr.getSystem( );
            em = system.getEntityManagerFactory().createEntityManager(  );

            Slice osl = em.find( Slice.class, sr.getID( ) );

            Subspace sp = null;
            SliceKind sk = null;
            // check which attribute shall be transformed.
            if( sliceTransformSpecifier.getSubspaceSpecifier() != null ) {
                sp = findSubspace( new ImmutableScopedName( sliceTransformSpecifier.getSubspaceSpecifier() ), em );
                sk = osl.getKind( );
            } else if( sliceTransformSpecifier.getSliceKind( ) != null ) {
                String uri = sliceTransformSpecifier.getSliceKind( ).toString( );
                sp = osl.getSubspace( );
                sk = findSliceKind( uri, sp.getId( ), em );
            } else { // must be both
                sp = findSubspace(
                        new ImmutableScopedName (
                                sliceTransformSpecifier.getSliceTypeSpecifier().getSubspaceSpecifier() ),
                        em );
                String uri = sliceTransformSpecifier.getSliceTypeSpecifier().getSliceKind( ).toString( );
                sk = findSliceKind( uri, sp.getId( ), em );
            }


            ResourceKey rk = null;
            try {
                rk = esr.createResource( );
            } catch ( Exception e ) {
                throw new InternalFailure( );
            }
            SliceResource nsr = esr.getResource( rk );

            TransformSliceAction tsa =
                    new TransformSliceAction( nsr.getID(), LogAux.getLocalName(), osl.getTerminationTime( ), sk, sp, osl.getTotalStorageSize(), system.getModelUUIDGen() );

            tsa.setClosingEntityManagerOnCleanup( false );

            GridResourceModelHandler mh =
                    new GridResourceModelHandler<Slice, ExtSliceResourceHome, SliceResource> (Slice.class, esr );

            Slice nsl = (Slice) mh.callModelAction( em, system, tsa, osl );
            
            if( nsl == null ) {
                esr.remove( rk );
                throw new InternalFailure( );
            }

            nsr.loadFromModel( nsl );


            try {
                // mark slice for removal
                sr.setTerminationTime( new GregorianCalendar( ) );
                return esr.getResourceReference( rk );
            } catch ( Exception e ) {
                throw new RemoteException(e.getMessage(), e);
            }
        } finally {
            if( em != null && em.isOpen( ) )
                em.close( );
        }
    }

    ///////////////////////
    // some little helpers

    private Subspace findSubspace( ImmutableScopedName isn, EntityManager em ) throws UnknownSubspace {

        MetaSubspace msp = em.find( MetaSubspace.class, isn );
        if( msp == null ) {
            throw new UnknownSubspace( );
        }

        Subspace sp = msp.getInstance( );
        if( sp == null ) {
            throw new UnknownSubspace( );
        }

        return sp;
    }

    private SliceKind findSliceKind( String uriParam, String idParam, EntityManager em ) throws UnknownOrInvalidSliceKind {

        SliceKind sk = em.find( SliceKind.class, uriParam );
        Query q = em.createQuery( querySupportedSliceKind );
        q.setParameter( "uriParam", uriParam );
        q.setParameter( "idParam", idParam );
        if( sk == null || q.getResultList().size() == 0 ) {
            throw new UnknownOrInvalidSliceKind( );
        }

        return sk;
    }

}

