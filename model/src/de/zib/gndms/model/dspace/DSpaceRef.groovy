package de.zib.gndms.model.dspace

import javax.persistence.Entity
import javax.persistence.IdClass
import de.zib.gndms.model.common.SimpleRKVEPRef
import de.zib.gndms.model.common.SimpleRKVEPRefId
import javax.xml.namespace.QName
import javax.persistence.Transient
import javax.persistence.Embeddable

/**
 * VEPRef to a DSpace instance
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Entity(name="dspaceRefs") @IdClass(DSpaceVEPRefId.class)
class DSpaceVEPRef extends SimpleRKVEPRef {
	private static RESOURCE_KEY_NAME =
		new QName("http://dspace.gndms.zib.de/DSpace/Subspace", "SubspaceKey");

	@Transient
	def public QName getResourceKeyName() {
		return RESOURCE_KEY_NAME;
	}

	@Transient
	def public String getParentResourceName() {
		return "DSpace";
	}
}

@Embeddable
class DSpaceVEPRefId extends SimpleRKVEPRefId {

}