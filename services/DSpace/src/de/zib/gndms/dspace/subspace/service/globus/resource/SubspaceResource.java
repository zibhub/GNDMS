package de.zib.gndms.dspace.subspace.service.globus.resource;

import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.infra.db.SystemHolder;
import de.zib.gndms.infra.db.DefaultSystemHolder;
import de.zib.gndms.infra.db.GNDMSystem;
import org.globus.wsrf.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;


/** 
 * The implementation of this SubspaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SubspaceResource extends SubspaceResourceBase
	  implements PersistentResource, SystemHolder {

	@Nullable
	private Subspace model;

	private SystemHolder holder = new DefaultSystemHolder();


	@Override
	public void initialize(
		  Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
		super.initialize(new ExtSubspaceResourceProperties(), resourceElementQName, id);

	}

	public void remove() throws ResourceException {
	}

	public void load(ResourceKey resourceKey)
		  throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {
	}

	public void store() throws ResourceException {
	}

	@NotNull
	public GNDMSystem getSystem() throws IllegalStateException {return holder.getSystem();}

	public void setSystem(@NotNull GNDMSystem system) throws IllegalStateException {
		holder.setSystem(system);
	}
}
