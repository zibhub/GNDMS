package de.zib.gndms.dspace.subspace.service.globus.resource;

import de.zib.gndms.dspace.common.DSpaceTools;
import de.zib.gndms.dspace.stubs.types.DSpaceReference;
import de.zib.gndms.dspace.subspace.common.SubspaceConstants;
import de.zib.gndms.dspace.subspace.stubs.SubspaceResourceProperties;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.model.ModelHandler;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.dspace.Subspace;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import types.StorageSizeT;


/**
 * The implementation of this SubspaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
@SuppressWarnings({"FeatureEnvy"})
public class SubspaceResource extends SubspaceResourceBase
	  implements ReloadablePersistentResource<Subspace, ExtSubspaceResourceHome> {

	private ModelHandler<Subspace, ExtSubspaceResourceHome, SubspaceResource> mH;
	private ExtSubspaceResourceHome resourceHome;


	// Overridden setters modify the model and by virtue of a database trigger set the
	// resource properties

	@Override
	public void setDSpaceReference(final DSpaceReference dSpaceReference) throws ResourceException {
		throw new UnsupportedOperationException("Cannot overwrite this read-only property");
	}


	@Override
	public void setAvailableStorageSize(final StorageSizeT availableStorageSize)
		  throws ResourceException {
		Subspace model = loadModelById(getID());
		model.setAvailableSize(DSpaceTools.buildSize(super.getAvailableStorageSize()));
		mH.mergeModel(model);
	}


	@Override
	public void setTotalStorageSize(final StorageSizeT totalStorageSize) throws ResourceException {
		Subspace model = loadModelById(getID());
		model.setTotalSize(DSpaceTools.buildSize(super.getTotalStorageSize()));
		mH.mergeModel(model);
	}



	@NotNull
	public Subspace loadModelById(@NotNull final String id) throws ResourceException {
		return mH.loadModelById(id);
	}


	public void loadViaModelId(@NotNull final String id) throws ResourceException {
		loadFromModel(loadModelById(id));
	}


	public synchronized void loadFromModel(@NotNull final Subspace model) throws ResourceException {
		if (model.getId().equals(getID())) {
			// set resource properties from model
			super.setAvailableStorageSize(DSpaceTools.buildSizeT(model.getAvailableSize()));
			super.setTotalStorageSize(DSpaceTools.buildSizeT(model.getTotalSize()));

			try {
				final DSpaceReference dSpaceReference =
					  DSpaceTools.dSpaceRefsAsReference(model.getDSpaceRef(), getSystem());
				super.setDSpaceReference(dSpaceReference);
			}
			catch (Exception e)
				{ logger.error(e); }
			super.setTerminationTime(model.getTerminationTime());
		}
		else
			throw new ResourceException("Model id mismatch");
	}


	public void load(@NotNull final ResourceKey resourceKeyParam)
		  throws ResourceException {
		// only called once, during find!
		@NotNull final String id = (String)resourceKeyParam.getValue();
		Subspace model = loadModelById(id);
		if (getResourceHome().getKeyTypeName().equals(resourceKeyParam.getName())) {
			setResourceKey(resourceKeyParam);
			initialize(new SubspaceResourceProperties(),
			           SubspaceConstants.RESOURCE_PROPERTY_SET, id);
			loadFromModel(model);
		}
		else
			throw new InvalidResourceKeyException("Invalid resourceKey name");
	}


	public void store() throws ResourceException {
		// done elsewhere
	}


	public void remove() throws ResourceException {
		throw new UnsupportedOperationException(
			  "Currently only possible when offline, via the database");
	}


	@NotNull
	public final ExtSubspaceResourceHome getResourceHome() {
		if (resourceHome == null)
			throw new IllegalStateException("No resourceHome set");
		return resourceHome;
	}


	public final void setResourceHome(
		  @NotNull final ExtSubspaceResourceHome resourceHomeParam) {
		if (resourceHome == null) {
			mH = new ModelHandler<Subspace, ExtSubspaceResourceHome, SubspaceResource>
				  (Subspace.class, resourceHomeParam);
			resourceHome = resourceHomeParam;
		}
		else
			throw new IllegalStateException("resourceHome already set");
	}

	@NotNull
	private GNDMSystem getSystem() throws ResourceException {
		return getResourceHome().getSystem();
	}


	@Override @NotNull
	public String getID() {
		return (String) super.getID();    // Overridden method
	}
}
