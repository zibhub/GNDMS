package de.zib.gndms.dspace.slice.service;

import de.zib.gndms.dspace.slice.stubs.types.SliceReference;
import de.zib.gndms.dspace.slice.service.globus.resource.SliceResource;
import de.zib.gndms.dspace.slice.service.globus.resource.ExtSliceResourceHome;
import de.zib.gndms.dspace.stubs.types.UnknownSubspace;
import de.zib.gndms.dspace.stubs.types.InternalFailure;
import de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
import de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.logic.model.dspace.TransformSliceAction;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.infra.system.GNDMSystem;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.rmi.RemoteException;

import org.globus.wsrf.ResourceKey;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceImpl extends SliceImplBase {


    private static final String querySupportedSliceKind =
            "SELECT sk FROM MetaSubspaces x INNER JOIN x.creatableSliceKinds sk WHERE sk.URI = :uriParam AND" +
            " x.scopedName.nameScope = (SELECT y.metaSubspace.scopedName.nameScope FROM Subspaces y WHERE y.id = :idParam)" +
            " AND x.scopedName.localName = (SELECT y.metaSubspace.scopedName.localName FROM Subspaces y WHERE y.id = :idParam)";

    public SliceImpl() throws RemoteException {
		super();
	}
	

    public de.zib.gndms.dspace.slice.stubs.types.SliceReference transformSliceTo(types.SliceTransformSpecifierT sliceTransformSpecifier,types.ContextT context) throws RemoteException, UnknownSubspace, OutOfSpace, UnknownOrInvalidSliceKind, InternalFailure {

        ExtSliceResourceHome esr;
        SliceResource sr;
        try {
            esr = (ExtSliceResourceHome) getResourceHome(  );
            sr = esr.getAddressedResource();
        } catch ( Exception e ) {
            throw new RemoteException( "Can't obtain slice resource home." );
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
                // todo uncomment after xsd type change
                // String uri = sliceTransformSpecifier.getSliceKind( );
                String uri = null;
                sp = osl.getOwner( );
                sk = findSliceKind( uri, sp.getId( ), em );
            } else { // must be both
                sp = findSubspace(
                        new ImmutableScopedName (
                                sliceTransformSpecifier.getSliceTypeSpecifier().getSubspaceSpecifier() ),
                        em );
                // todo uncomment after xsd type change
                // String uri = sliceTransformSpecifier.getSliceTypeSpecifier().getSliceKind( );
                String uri = null;
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
                    new TransformSliceAction( nsr.getID(), osl.getTerminationTime( ), sk, sp, osl.getTotalStorageSize(), system.getModelUUIDGen() );


            GridResourceModelHandler mh =
                    new GridResourceModelHandler<Slice, ExtSliceResourceHome, SliceResource> (Slice.class, esr );

            Slice nsl = (Slice) mh.callModelAction( em, system, tsa, osl );
            tsa.getPostponedActions().call( );
            if( nsl == null ) {
                esr.remove( rk );
                throw new InternalFailure( );
            }

            nsr.loadFromModel( nsl );


            try {
                osl.getOwner( ).destroySlice( osl );
                esr.remove( sr.getResourceKey() );
                return esr.getResourceReference( rk );
            } catch ( Exception e ) {
                throw new RemoteException( e.getMessage() );
            }
        } finally {
            if( em != null )
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

