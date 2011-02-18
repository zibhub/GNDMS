package de.zib.gndms.dspace.subspace.service;

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
import de.zib.gndms.dspace.slice.service.globus.resource.SliceResource;
import de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome;
import de.zib.gndms.dspace.slice.stubs.types.SliceReference;
import de.zib.gndms.dspace.stubs.types.InternalFailure;
import de.zib.gndms.dspace.subspace.service.globus.resource.SubspaceResource;
import de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
import de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.util.TxFrame;
import org.apache.axis.types.URI;
import org.apache.log4j.Logger;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceKey;
import types.ContextT;
import types.SliceCreationSpecifier;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SubspaceImpl extends SubspaceImplBase {

    protected Logger logger = Logger.getLogger( this.getClass() );

    public SubspaceImpl() throws RemoteException {
		super();
	}

    public SliceReference createSlice( SliceCreationSpecifier sliceCreationSpecifier, ContextT context ) throws RemoteException, OutOfSpace, UnknownOrInvalidSliceKind, InternalFailure {

        LogAux.logSecInfo( logger, "createSliceInSubspace" );

        SubspaceResource subref = getResource();
        //Subspace sp = subref.loadModelById( subref.getID() );


        SliceReference sref;
        SliceResourceHome srh = null;
        ResourceKey rk = null;
        EntityManager em = null;
        TxFrame tx = null;
        try {
            Long ssize = null;

            if( sliceCreationSpecifier.getTotalStorageSize() != null )
                ssize = sliceCreationSpecifier.getTotalStorageSize().longValue();
            else
                ssize = 0l;

            srh = getSliceResourceHome( );
            rk = srh.createResource( );
            SliceResource sr = srh.getResource( rk );

            GNDMSystem system = subref.getResourceHome( ).getSystem( );

            em = system.getEntityManagerFactory().createEntityManager(  );
            tx = new TxFrame( em );


            String id =  subref.getID();

            Query q = em.createNamedQuery( "getMetaSubspaceKey" );
            q.setParameter( "idParam", id );
            ImmutableScopedName isn = (ImmutableScopedName) q.getSingleResult();

            final MetaSubspace msp = em.find( MetaSubspace.class, isn );

            id = sliceCreationSpecifier.getSliceKind( ).toString( );
            final SliceKind sk = em.find( SliceKind.class, id );

            if( sk == null )
                throw new IllegalArgumentException( "Slice kind doesn't exist: " + id );


            final CreateSliceAction csa =
                    new CreateSliceAction( (String) sr.getID(),
                            LogAux.getLocalName(),
                            sliceCreationSpecifier.getTerminationTime(),
                            system.getModelUUIDGen(),
                            sk,
                            ssize
                    );
            csa.setClosingEntityManagerOnCleanup( false );
            csa.setOwnEntityManager( em );
            csa.setModel( msp.getInstance( ) );
            DefaultBatchUpdateAction bua = new DefaultBatchUpdateAction<GridResource>();
            bua.setListener( system );
            csa.setOwnPostponedEntityActions(bua);

            final Slice ns = csa.call();

            csa.getPostponedEntityActions().call( );

            sr.loadFromModel( ns );
            sref = srh.getResourceReference( rk );
            tx.commit( );
        } catch ( OutOfSpace e ) {
            throw e;
        } catch ( Exception e ) {
            if( srh != null && rk != null )
                srh.remove( rk );
            throw new RemoteException( e.toString(), e );
        } finally  {

            if( tx != null )
                tx.finish();
            
            if( em != null && em.isOpen( ) )
                em.close( );
        }

        return sref;
    }


    public org.apache.axis.types.URI[] listCreatableSliceKinds() throws RemoteException {

        SubspaceResource subref = getResource();

        EntityManager em = null ;
        try {
            em = subref.getResourceHome().getSystem().getEntityManagerFactory().createEntityManager( );

            Query q = em.createNamedQuery( "listCreatableSliceKinds" );
            q.setParameter( "idParam", subref.getID() );
            List<SliceKind> rl = (List<SliceKind>) q.getResultList();
            int l = rl.size( );

            try {
                ArrayList<URI> al = new ArrayList<URI>( rl.size( ) );
                for( SliceKind sk: rl )
                    al.add( new URI( sk.getURI() ) );
                return al.toArray( new URI[al.size( )] );

            } catch ( URI.MalformedURIException e ) {
                throw new RemoteException( e.getMessage(), e );
            }
        } finally {
            if( em != null && em.isOpen( ) )
                em.close( );
        }
    }

    /**
     * Method find the resource associated with the caller
     *
     * ( this mehtod is "borrowed" from the Globus4 book.
     *
     * @return The resource or null if there is none.
     * @throws RemoteException
     */
    private SubspaceResource getResource() throws RemoteException {

        Object resource = null;

        try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (NoSuchResourceException e) {
			throw new RemoteException("Specified resource does not exist", e);
		} catch (ResourceContextException e) {
			throw new RemoteException("Error during resource lookup", e);
		} catch (Exception e) {
			throw new RemoteException("", e);
		}

		SubspaceResource res = (SubspaceResource) resource;
		return res;
	}

}

