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
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceKey;
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
        Subspace sp = subref.loadModelById( subref.getID() );

        if( sp == null )
           throw new RemoteException( "No subspace found" );


        SliceReference sref;
        SliceResourceHome srh = null;
        ResourceKey rk = null;
        EntityManager em = null;
        try {
            Long ssize = sliceCreationSpecifier.getTotalStorageSize().longValue();

            srh = getSliceResourceHome( );
            rk = srh.createResource( );
            SliceResource sr = srh.getResource( rk );

            GNDMSystem system = subref.getResourceHome( ).getSystem( );

            em = system.getEntityManagerFactory().createEntityManager(  );
            SliceKind sk = em.find( SliceKind.class, sliceCreationSpecifier.getSliceKind( ).toString( ) );
            CreateSliceAction csa =
                    new CreateSliceAction( (String) sr.getID(),
                            sliceCreationSpecifier.getTerminationTime(),
                            system.getModelUUIDGen(),
                            sk,
                            ssize != null ? ssize : 0
                    );

            GridResourceModelHandler mh = new GridResourceModelHandler<Subspace, ExtSubspaceResourceHome, SubspaceResource>
                    (Subspace.class, (ExtSubspaceResourceHome) subref.getResourceHome( ) );

            Slice ns = (Slice) mh.callNewModelAction( system, csa, sp );

            csa.getPostponedActions().call( );

            sr.loadFromModel( ns );
            sref = srh.getResourceReference( rk );
        } catch ( OutOfSpace e ) {
            throw e;
        } catch ( Exception e ) {
            if( srh != null && rk != null )
                srh.remove( rk );
            throw new RemoteException( e.toString() );
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

        ArrayList<String> al = new ArrayList<String>( rl.size( ) );
        for( SliceKind sk: rl ) {
            al.add( sk.getURI() );
        }

        // todo uncommend when sig is changed
        // return al.toArray( new String[0] );
        return null;
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

