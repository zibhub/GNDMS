package de.zib.gndms.dspace.subspace.service;

import de.zib.gndms.dspace.slice.service.globus.resource.SliceResource;
import de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome;
import de.zib.gndms.dspace.slice.stubs.types.SliceReference;
import de.zib.gndms.dspace.stubs.types.InternalFailure;
import de.zib.gndms.dspace.subspace.service.globus.resource.SubspaceResource;
import de.zib.gndms.dspace.subspace.service.globus.resource.ExtSubspaceResourceHome;
import de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
import de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.EntityUpdateListener;
import de.zib.gndms.logic.model.ModelAction;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceKey;
import org.apache.axis.types.URI;
import types.ContextT;
import types.SliceCreationSpecifier;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SubspaceImpl extends SubspaceImplBase {

    private static final String queryCreatableSliceKinds =
            "SELECT sk FROM MetaSubspaces x INNER JOIN x.creatableSliceKinds sk WHERE " +
            "x.scopedName.nameScope = (SELECT y.metaSubspace.scopedName.nameScope FROM Subspaces y WHERE y.id = :idParam)" +
            " AND x.scopedName.localName = (SELECT y.metaSubspace.scopedName.localName FROM Subspaces y WHERE y.id = :idParam)";

    public SubspaceImpl() throws RemoteException {
		super();
	}

    public SliceReference createSlice( SliceCreationSpecifier sliceCreationSpecifier, ContextT context ) throws RemoteException, OutOfSpace, UnknownOrInvalidSliceKind, InternalFailure {

        SubspaceResource subref = getResource();
        //Subspace sp = subref.loadModelById( subref.getID() );


        SliceReference sref;
        SliceResourceHome srh = null;
        ResourceKey rk = null;
        EntityManager em = null;
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
            String id = sliceCreationSpecifier.getSliceKind( ).toString( );
            SliceKind sk = em.find( SliceKind.class, id );

            if( sk == null )
                throw new IllegalArgumentException( "Slice kind doesn't exist: " + id );

            id =  subref.getID();
            Subspace sp = em.find( Subspace.class, id );
            if( sp == null )
                throw new RemoteException( "Subspace doesn't exist: " + id );

            CreateSliceAction csa =
                    new CreateSliceAction( (String) sr.getID(),
                            sliceCreationSpecifier.getTerminationTime(),
                            system.getModelUUIDGen(),
                            sk,
                            ssize
                    );
            csa.setClosingEntityManagerOnCleanup( false );

            GridResourceModelHandler mh = new GridResourceModelHandler<Subspace, ExtSubspaceResourceHome, SubspaceResource>
                    (Subspace.class, (ExtSubspaceResourceHome) subref.getResourceHome( ) );

            Slice ns = (Slice) mh.callModelAction( em, system,  csa, sp );

            csa.getPostponedActions().call( );

            sr.loadFromModel( ns );
            sref = srh.getResourceReference( rk );
        } catch ( OutOfSpace e ) {
            throw e;
        } catch ( Exception e ) {
            if( srh != null && rk != null )
                srh.remove( rk );
            throw new RemoteException( e.toString(), e );
        } finally  {
            if( em != null && em.isOpen( ) )
                em.close( );
        }

        return sref;
    }


    public org.apache.axis.types.URI[] listCreatableSliceKinds() throws RemoteException {

        SubspaceResource subref = getResource();

        EntityManager em = subref.getResourceHome().getSystem().getEntityManagerFactory().createEntityManager( );

        Query q = em.createQuery( queryCreatableSliceKinds );
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

