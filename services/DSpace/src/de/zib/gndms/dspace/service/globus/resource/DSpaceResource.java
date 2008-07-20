package de.zib.gndms.dspace.service.globus.resource;

import org.globus.wsrf.ResourceException;

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
