package de.zib.gndms.dspace.service.globus.resource;

import de.zib.gndms.dspace.common.DSpaceConstants;
import de.zib.gndms.dspace.stubs.DSpaceResourceProperties;
import de.zib.gndms.infra.model.SingletonGridResourceModelHandler;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.dspace.DSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


/**
 * The implementation of this DSpaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceResource extends DSpaceResourceBase
	  implements ReloadablePersistentResource<DSpace, ExtDSpaceResourceHome> {
	@NotNull @NonNls public static final String QUERY_INSTANCES = "findDSpaceInstances";

	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private static final Log logger = LogFactory.getLog(DSpaceResource.class);

	// Set during initialize or construction
	private ExtDSpaceResourceHome resourceHome;
	private SingletonGridResourceModelHandler<DSpace, ExtDSpaceResourceHome, DSpaceResource> mH;


	public void load(final ResourceKey resourceKeyParam) throws ResourceException {
		// only called once during find
		final DSpace model;
		final String id;

		if (resourceKeyParam == null) {
			model = (DSpace) mH.getSingleModel(null, QUERY_INSTANCES, null);
			id = model.getId();
			logger.debug("dspace Singleton found with id: " + id);
		}
		else if (getResourceHome().getKeyTypeName().equals(resourceKeyParam.getName())) {
			id = (String) resourceKeyParam.getValue();
			model = loadModelById(id);
		}
		else
			throw new InvalidResourceKeyException("Invalid resourceKey name");

		initialize(new DSpaceResourceProperties(), DSpaceConstants.RESOURCE_PROPERTY_SET, id);
		loadFromModel(model);
	}

	public void loadViaModelId(@NotNull String id) throws ResourceException {
		loadFromModel(loadModelById(id));
	}

	@NotNull
	public DSpace loadModelById(@NotNull String id) throws ResourceException {
		return (DSpace) mH.loadModelById(null, id);
	}

	public void loadFromModel(@NotNull DSpace model) throws ResourceException {
		if (model.getId().equals(getID())) {
			// currently nothing
		}
		else
			throw new ResourceException("Model id mismatch");
	}

	public void remove() throws ResourceException {
		throw new UnsupportedOperationException();
	}

	public void store() throws ResourceException {
		// Done elsewhere
	}
	@NotNull
	public final ExtDSpaceResourceHome getResourceHome() {
		if (resourceHome == null)
			throw new IllegalStateException("No resourceHome set");
		return resourceHome;
	}

	public final void setResourceHome(@NotNull ExtDSpaceResourceHome resourceHomeParam)
	{
		if (resourceHome == null) {
			mH = new SingletonGridResourceModelHandler<DSpace, ExtDSpaceResourceHome, DSpaceResource>
				  (DSpace.class, resourceHomeParam);
			resourceHome = resourceHomeParam;
		}
		else
			throw new IllegalStateException("resourceHome already set");
	}

	@NotNull
	private GNDMSystem getSystem() throws ResourceException {
		return getResourceHome().getSystem();
	}

	@Override
	public String getID() {
		return (String) super.getID();    // Overridden method
	}


	public void initializeModel(final @NotNull DSpace model) {
		// intended
	}
}
