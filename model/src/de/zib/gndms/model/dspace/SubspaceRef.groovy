package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.SimpleRKRef
import javax.xml.namespace.QName
import javax.persistence.Transient
import javax.persistence.Embeddable
import javax.persistence.Column

/**
 * VEPRef to a Subspace instance
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Embeddable
class SubspaceRef extends SimpleRKRef {
	private static RESOURCE_KEY_NAME =
		new QName("http://dspace.gndms.zib.de/DSpace/Subspace", "SubspaceKey")
	private static final List<String> RESOURCE_NAMES = ["DSpace", "Subspace"].asImmutable()

	private String gridSiteId;
	private String resourceKeyValue;

	@Transient
	def public QName getResourceKeyName() { RESOURCE_KEY_NAME }

	@Transient
	def public List<String> getResourceNames() { RESOURCE_NAMES }

	@Column(name="site_id", nullable=true, updatable=false)
	def public String getGridSiteId() { gridSiteId }
	def public void setGridSiteId(final String newSiteId) { gridSiteId = newSiteId }

	@Column(name="key_val", nullable=false, updatable=false, columnDefinition="CHAR", length=36)
	def public String getResourceKeyValue() { resourceKeyValue }
	def public void setResourceKeyValue(final String newValue) { resourceKeyValue = newValue }
}
