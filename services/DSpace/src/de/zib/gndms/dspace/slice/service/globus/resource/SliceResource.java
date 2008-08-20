package de.zib.gndms.dspace.slice.service.globus.resource;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.apache.axis.types.URI;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.infra.model.GridEntityModelHandler;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.dspace.common.DSpaceTools;
import de.zib.gndms.dspace.subspace.service.globus.resource.ExtSubspaceResourceHome;
import de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference;

import java.rmi.RemoteException;


/** 
 * The implementation of this SliceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceResource extends SliceResourceBase
        implements ReloadablePersistentResource<Slice, ExtSliceResourceHome> {


    private ExtSliceResourceHome resourceHome;
    private GridEntityModelHandler<Slice, ExtSliceResourceHome, SliceResource> mH;


    // override generated setter method to pass changes on the resource directly to the model
    public void setTotalStorageSize(types.StorageSizeT totalStorageSize ) throws ResourceException {
        Slice sl = loadModelById( getID( ) );
        sl.setTotalStorageSize( DSpaceTools.buildSize( totalStorageSize ) );
        mH.mergeModel( null, sl );
    }

    public void setSliceKind(javax.xml.namespace.QName sliceKind ) throws ResourceException {
    }

    public void setSliceLocation(org.apache.axis.types.URI sliceLocation ) throws ResourceException {
    }

    public void setSubspaceReference(de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference subspaceReference ) throws ResourceException {
    }

    @NotNull
    public Slice loadModelById( @NotNull String id ) throws ResourceException {
        return mH.loadModelById( null, id );
    }

    public void loadViaModelId( @NotNull String id ) throws ResourceException {
        loadFromModel(loadModelById( id ) );
    }

    public void loadFromModel( @NotNull Slice model ) throws ResourceException {

        // todo uncomment after xsd change of slice kind uri
   //     super.setSliceKind( model.getKind( ).getURI( ) );

        try {
            super.setSliceLocation( new URI( model.getOwner().getMetaSubspace().getScopedName().getNameScope() ) );
        } catch ( URI.MalformedURIException e ) {
            throw new ResourceException( "Following exception occured: " + e.getMessage( ) );
        }

        super.setTerminationTime( model.getTerminationTime() );
        super.setTotalStorageSize( DSpaceTools.buildSizeT( model.getTotalStorageSize( ) ) );

        GNDMSystem sys = resourceHome.getSystem( );
        ExtSubspaceResourceHome srh = (ExtSubspaceResourceHome) sys.getHome( Subspace.class );
        SubspaceReference subref;
        try {
            subref = srh.getReferenceForSubspace( model.getOwner( ) );
            super.setSubspaceReference( subref );
        } catch ( Exception e ) {
            throw new ResourceException( "Can't obtain reference for subspace: " + e.getMessage( ) );
        }
    }

    @NotNull
    public ExtSliceResourceHome getResourceHome() {
        if( resourceHome == null )
            throw new IllegalStateException( "now slice resource home set" );
        return resourceHome;
    }

    public void setResourceHome( @NotNull ExtSliceResourceHome resourceHomeParam ) {
        if (resourceHome == null) {
			mH = new GridResourceModelHandler<Slice, ExtSliceResourceHome, SliceResource>
				  (Slice.class, resourceHomeParam);
			resourceHome = resourceHomeParam;
		}
		else
			throw new IllegalStateException("resourceHome already set");
    }

    public void load( ResourceKey resourceKey ) throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void store() throws ResourceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getID( ) {
        return (String) getID( );
    }

}
