package de.zib.gndms.dspace.service.globus.resource;

import de.zib.gndms.infra.GNDMSConstants;
import de.zib.gndms.infra.GNDMSConfig;
import de.zib.gndms.infra.db.DbSetupFacade;
import org.globus.wsrf.ResourceException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.namespace.QName;

/**
 * The implementation of this DSpaceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceResource extends DSpaceResourceBase {

	@Override
	public void initialize(
		  Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
		super.initialize(resourceBean, resourceElementQName, id);    // Overridden method
	}
}
