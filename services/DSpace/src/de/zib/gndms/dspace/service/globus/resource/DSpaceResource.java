package de.zib.gndms.dspace.service.globus.resource;

import de.zib.gndms.dspace.common.DSpaceConstants;
import de.zib.gndms.dspace.stubs.DSpaceResourceProperties;
import de.zib.gndms.infra.db.GNDMSystem;
import de.zib.gndms.infra.db.ModelCreator;
import de.zib.gndms.infra.db.ModelHandler;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.dspace.DSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;

/**
 * The implementation of this DSpaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceResource extends DSpaceResourceBase
	  implements ReloadablePersistentResource<DSpace>, ModelCreator<DSpace> {

	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private static final Log logger = LogFactory.getLog(DSpaceResource.class);

	// Set during initialize or construction
	private ExtDSpaceResourceHome resourceHome;
	private ModelHandler<DSpace> mH;

	protected DSpaceResource() {
		super();
		throw new UnsupportedOperationException();
	}
	
	protected DSpaceResource(@NotNull ExtDSpaceResourceHome home) throws ResourceException {
		super();
		setResourceHome(home);
		DSpace model = mH.getSingleModel("findDSpaceInstance", this);
		logger.debug("Singleton found with id: " + model.getId());
		initialize(new DSpaceResourceProperties(), DSpaceConstants.RESOURCE_PROPERTY_SET, model);
	}


	@Override
	public void initialize(
		  Object resourceBean, QName resourceElementQName, Object modelObject) throws ResourceException {
		logger.info("resource init");
		DSpace model = (DSpace) modelObject;
		super.initialize(resourceBean, resourceElementQName, model.getId());
		loadFromModel(model);
	}

	@NotNull
	public DSpace createInitialModel(@NotNull String id, @NotNull String gridName) {
		final DSpace model = new DSpace();
		model.setId(id);
		model.setGridName(gridName);
		return model;
	}


	public void load(ResourceKey resourceKey)
		  throws ResourceException, InvalidResourceKeyException {
		loadById((String) resourceKey.getValue());
	}

	public void loadById(@NotNull String id) throws ResourceException {
		loadFromModel(loadModel(id));
	}

	@NotNull
	public DSpace loadModel(@NotNull String id) throws ResourceException {
		return mH.loadModelById(id);
	}

	public void loadFromModel(@NotNull DSpace model) throws ResourceException {
		if (model.getId() == getID()) {
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
	protected final ExtDSpaceResourceHome getResourceHome() {
		return resourceHome;
	}

	protected final void setResourceHome(@NotNull ExtDSpaceResourceHome newResourceHome) {
		resourceHome = newResourceHome;
		mH = new ModelHandler<DSpace>(DSpace.class, resourceHome);
	}

	@NotNull
	private GNDMSystem getSystem() throws ResourceException {
		return resourceHome.getSystem();
	}

}
