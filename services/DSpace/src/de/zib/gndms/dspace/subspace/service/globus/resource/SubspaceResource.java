package de.zib.gndms.dspace.subspace.service.globus.resource;

import de.zib.gndms.dspace.common.DSpaceTools;
import de.zib.gndms.dspace.stubs.types.DSpaceReference;
import de.zib.gndms.infra.db.DefaultSystemHolder;
import de.zib.gndms.infra.db.GNDMSystem;
import de.zib.gndms.infra.db.ModelHandler;
import de.zib.gndms.infra.db.SystemHolder;
import static de.zib.gndms.infra.db.GNDMSystem.currentEMG;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.VEPRef;
import de.zib.gndms.model.dspace.DSpaceRef;
import de.zib.gndms.model.dspace.Subspace;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import types.StorageSizeT;

import javax.xml.namespace.QName;
import java.util.Calendar;


/** 
 * The implementation of this SubspaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
@SuppressWarnings({"FeatureEnvy"})
public class SubspaceResource extends SubspaceResourceBase
	  implements ReloadablePersistentResource, SystemHolder {

	private SystemHolder sysH = new DefaultSystemHolder();

	private ModelHandler<Subspace> mH = new ModelHandler<Subspace>(Subspace.class);

	@Override
	public void initialize(
		  Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
		super.initialize(resourceBean, resourceElementQName, id);    // Overridden method
		
	}

	@Override  @NotNull
	public StorageSizeT getAvailableStorageSize() {
		final Subspace model;
		try {
			model = mH.loadModel(getSystem().currentEMG(), this);
			StorageSizeT valueT = DSpaceTools.buildSizeT(model.getAvailableSize());
			super.setAvailableStorageSize(valueT);
			return valueT;
		}
		catch (ResourceException e) { logUnhandledAndThrow(e); }
		throw new RuntimeException("unreachable");
	}

	@Override
	public void setAvailableStorageSize(@NotNull StorageSizeT valueT)
		  throws ResourceException {
		try {
			super.setAvailableStorageSize(valueT);
			boolean flag = currentEMG(this).begin();
			try {
				Subspace model = mH.loadModel(currentEMG(this), this);
				model.setAvailableSize(DSpaceTools.buildSize(valueT));
				mH.storeModel(currentEMG(this), model);
			}
			catch (RuntimeException e) { currentEMG(this).rollback(flag, e); }
			finally { currentEMG(this).commitAndClose(flag); }
		}
		catch (ResourceException r) { reload(); }
	}

	@Override @NotNull
	public StorageSizeT getTotalStorageSize() {
		final Subspace model;
		try {
			model = mH.loadModel(getSystem().currentEMG(), this);
			StorageSizeT valueT = DSpaceTools.buildSizeT(model.getTotalSize());
			super.setTotalStorageSize(valueT);
			return valueT;
		}
		catch (ResourceException e) { logUnhandledAndThrow(e); }
		throw new RuntimeException("unreachable");
	}

	@Override
	public void setTotalStorageSize(@NotNull StorageSizeT valueT)
		  throws ResourceException {
		try {
			super.setTotalStorageSize(valueT);
			boolean flag = currentEMG(this).begin();
			try {
				Subspace model = mH.loadModel(currentEMG(this), this);
				model.setTotalSize(DSpaceTools.buildSize(valueT));
				mH.storeModel(currentEMG(this), model);
			}
			catch (RuntimeException e) { currentEMG(this).rollback(flag, e); }
			finally { currentEMG(this).commitAndClose(flag); }
		}
		catch (ResourceException r) { reload(); }
	}

	/*
	@Override @NotNull
	public QName getSubspaceSpecifier() {
		final Subspace model;
		try {
			model = mH.loadModel(getSystem().currentEMG(), this);
			QName valueT = QName.valueOf(model.getMetaSubspace().getSpecifier());
			super.setSubspaceSpecifier(valueT);
			return valueT;
		}
		catch (ResourceException e) { logUnhandledAndThrow(e); }
		throw new RuntimeException("unreachable");
	}

	@Override
	public void setSubspaceSpecifier(@NotNull QName valueT)
		  throws ResourceException {
		try {
			super.setSubspaceSpecifier(valueT);
			boolean flag = currentEMG(this).begin();
			try {
				Subspace model = mH.loadModel(currentEMG(this), this);
				// TODO: This is wrong! Very!
				model.getMetaSubspace().setSpecifier(valueT.toString());
				mH.storeModel(currentEMG(this), model);
			}
			catch (RuntimeException e) { currentEMG(this).rollback(flag, e); }
			finally { currentEMG(this).commitAndClose(flag); }
		}
		catch (ResourceException r) { reload(); }
	}
 */
	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
	@Override @NotNull
	public DSpaceReference getDSpaceReference() {
		final Subspace model;
		try {
			model = mH.loadModel(getSystem().currentEMG(), this);
			final EndpointReferenceType eprType =
				  getSystem().serviceEPRType("dspace", model.getDSpaceRef());
			DSpaceReference valueT = new DSpaceReference(eprType);
			super.setDSpaceReference(valueT);
			return valueT;
		}
		catch (ResourceException e) { logUnhandledAndThrow(e); }
		catch (URI.MalformedURIException e) { logUnhandledAndThrow(new ResourceException(e)); }
		throw new RuntimeException("unreachable");
	}

	@Override
	public void setDSpaceReference(@NotNull DSpaceReference valueT)
		  throws ResourceException {
		try {
			super.setDSpaceReference(valueT);
			boolean flag = currentEMG(this).begin();
			try {
				Subspace model = mH.loadModel(currentEMG(this), this);
				VEPRef theVEPREF = getSystem().modelEPRT("dspace", valueT.getEndpointReference());
				model.setDSpaceRef((DSpaceRef)theVEPREF);
				mH.storeModel(currentEMG(this), model);
			}
			catch (RuntimeException e) { currentEMG(this).rollback(flag, e); }
			finally { currentEMG(this).commitAndClose(flag); }
		}
		catch (ResourceException r) { reload(); }
	}

	@Override @NotNull
	public Calendar getTerminationTime() {
		final Subspace model;
		try {
			model = mH.loadModel(getSystem().currentEMG(), this);
			Calendar valueT = model.getTerminationTime();
			super.setTerminationTime(valueT);
			return valueT;
		}
		catch (ResourceException e) { logUnhandledAndThrow(e); }
		throw new RuntimeException("unreachable");
	}

	@Override
	public void setTerminationTime(@NotNull Calendar valueT) {
		try {
			super.setTerminationTime(valueT);
			boolean flag = currentEMG(this).begin();
			try {
				Subspace model = mH.loadModel(currentEMG(this), this);
				model.setTerminationTime(valueT);
				mH.storeModel(currentEMG(this), model);
			}
			catch (RuntimeException e) { currentEMG(this).rollback(flag, e); }
			finally { currentEMG(this).commitAndClose(flag); }
		}
		catch (ResourceException r) { safeReload(); }
	}

	private void safeReload()  {
		try {
			load(getResourceKey());
		}
		catch (ResourceException e) {
			logUnhandled(e);
		}
	}

	public void reload() throws ResourceException {
		load(getResourceKey());
	}

	public final void load(ResourceKey resourceKey) throws ResourceException {
		final Subspace model = mH.loadModel(currentEMG(this), this);

		// Recreate *all* resource properties
		super.setAvailableStorageSize(DSpaceTools.buildSizeT(model.getAvailableSize()));
		super.setTotalStorageSize(DSpaceTools.buildSizeT(model.getTotalSize()));
		super.setTerminationTime(model.getTerminationTime());
		// super.setSubspaceSpecifier(QName.valueOf(model.getMetaSubspace().getSpecifier()));
	}


	public final void store() throws ResourceException {
		final Subspace model = new Subspace();

		// build initial model; only called during instance creation
		model.setAvailableSize(DSpaceTools.buildSize(super.getAvailableStorageSize()));
		model.setTotalSize(DSpaceTools.buildSize(super.getTotalStorageSize()));
		model.setTerminationTime(super.getTerminationTime());
		// TODO This is wrong! Very!
		// model.getMetaSubspace().setSpecifier(super.getSubspaceSpecifier().toString());
		mH.storeModel(currentEMG(this), model);
	}

	public final void remove() throws ResourceException {
		mH.removeModel(currentEMG(this), this);
	}

	@NotNull
	public GNDMSystem getSystem() throws IllegalStateException {return sysH.getSystem();}

	public void setSystem(@NotNull GNDMSystem system) throws IllegalStateException {
		sysH.setSystem(system);
	}

	private void logUnhandledAndThrow(ResourceException e) {
		logUnhandled(e);
		throw new RuntimeException(e);
	}

	@SuppressWarnings({"MethodMayBeStatic"})
	private void logUnhandled(ResourceException e) {
		logger.error("Unhandled exception.  Resource state might have been corrupted. ", e);
	}
}
