package de.zib.gndms.dspace.slice.service.globus.resource;

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



import de.zib.gndms.dspace.common.DSpaceTools;
import de.zib.gndms.dspace.slice.common.SliceConstants;
import de.zib.gndms.dspace.slice.stubs.SliceResourceProperties;
import de.zib.gndms.dspace.subspace.service.globus.resource.ExtSubspaceResourceHome;
import de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference;
import de.zib.gndms.infra.model.GridResourceModelHandler;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.logic.model.dspace.DeleteSliceAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import org.apache.axis.types.URI;
import org.apache.axis.types.UnsignedLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/** 
 * The implementation of this SliceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceResource extends SliceResourceBase
        implements ReloadablePersistentResource<Slice, ExtSliceResourceHome> {


    private ExtSliceResourceHome resourceHome;
    private GridResourceModelHandler<Slice, ExtSliceResourceHome, SliceResource> mH;
    private final Log logger = LogFactory.getLog( SliceResource.class );

    // override generated setter method to pass changes on the resource directly to the model
    public void setTotalStorageSize( UnsignedLong totalStorageSize ) throws ResourceException {
        Slice sl = loadModelById( getID( ) );
        sl.setTotalStorageSize( totalStorageSize.longValue( ) );
        mH.mergeModel( (EntityManager) null, sl );
    }

    public void setSliceKind(javax.xml.namespace.QName sliceKind ) throws ResourceException {
        throw new UnsupportedOperationException( "Setting the sliceKind directly is not allowed (use SliceImpl.transformSliceTo instead)" );
    }

    public void setSliceLocation(org.apache.axis.types.URI sliceLocation ) throws ResourceException {
        throw new UnsupportedOperationException( "Changing the slice location is not supported" );
    }

    public void setSubspaceReference(de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference subspaceReference ) throws ResourceException {
        throw new UnsupportedOperationException( "Changing the slices subspace is not supported (use SliceImpl.transformSliceTo instead)" );
    }


    @NotNull
    public Slice loadModelById( @NotNull String id ) throws ResourceException {
        return (Slice) mH.loadModelById( null, id );
    }

    public void loadViaModelId( @NotNull String id ) throws ResourceException {
        loadFromModel(loadModelById( id ) );
    }

    public void loadFromModel( @NotNull Slice model ) throws ResourceException {

        loadFromModel( model, ( SliceResourceProperties ) getResourceBean() );
    }


    public void loadFromModel( @NotNull Slice model, @NotNull SliceResourceProperties bean ) throws ResourceException {

        try {
            bean.setSliceKind( new URI ( model.getKind( ).getURI( ) ) );
        } catch ( URI.MalformedURIException e ) {
            throw new ResourceException( e.getMessage(), e );
        }

        bean.setTerminationTime( model.getTerminationTime() );
        bean.setTotalStorageSize( DSpaceTools.unsignedLongFromLong( model.getTotalStorageSize( ) ) );

        GNDMSystem sys = resourceHome.getSystem( );
        ExtSubspaceResourceHome srh = (ExtSubspaceResourceHome) sys.getInstanceDir().getHome( Subspace.class );
        SubspaceReference subref;
        try {
            Subspace sp =  model.getSubspace( );
            subref = srh.getReferenceForSubspace( sp );
            bean.setSubspaceReference( subref );
            bean.setSliceLocation( new URI( sp.getGsiFtpPathForSlice( model ) ) );
        } catch ( Exception e ) {
            throw new ResourceException( "Can't obtain reference for subspace: " + e.getMessage( ) );
        }
    }


    @NotNull
    public ExtSliceResourceHome getResourceHome() {
        if( resourceHome == null )
            throw new IllegalStateException( "No slice resource home set" );
        return resourceHome;
    }

    public void setResourceHome( @NotNull ExtSliceResourceHome resourceHomeParam ) {
        if (resourceHome == null) {
			mH = new GridResourceModelHandler<Slice, ExtSliceResourceHome, SliceResource>
				  (Slice.class, resourceHomeParam);
			resourceHome = resourceHomeParam;
		}
		else
			throw new IllegalStateException("Slice resource home already set");
    }


    public void load( ResourceKey resourceKey ) throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {
        
        if ( getResourceHome().getKeyTypeName().equals( resourceKey.getName() ) ) {
            String id = ( String ) resourceKey.getValue();
            setResourceKey( resourceKey );

            Slice sl = loadModelById(id);
            SliceResourceProperties bean = new SliceResourceProperties();
            loadFromModel( sl, bean );
            initialize( bean, SliceConstants.RESOURCE_PROPERTY_SET, id );
        }
        else
            throw new InvalidResourceKeyException("Invalid resourceKey name");
    }

    public void store() throws ResourceException {
        // done one model layer
    }

    public String getID( ) {
        return (String) super.getID( );
    }


    @Override
    public void refreshRegistration(final boolean forceRefresh) {
        // nothing
    }


    @Override
    public void remove( ) {

        logger.debug( "removing slice resource: " + getID() );
        EntityManager em = resourceHome.getEntityManagerFactory().createEntityManager();
        Slice sl = em.find( Slice.class, getID() );
        logger.debug( "removing slice directory: " + sl.getDirectoryId() );
        // the action takes care of the EntityManager from now on
        /*Future f =*/ resourceHome.getSystem().submitAction( em, new DeleteSliceAction( sl ), logger );
    }
}
