package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.SimpleRKRef
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Transient
import javax.xml.namespace.QName
import de.zib.gndms.model.common.GridSiteId
import javax.persistence.ManyToOne
import de.zib.gndms.model.common.GridSite
import javax.persistence.Embedded

/**
 * VEPRef to a DSpace instance
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Embeddable
class DSpaceRef extends SimpleRKRef {
	private static final QName RESOURCE_KEY_NAME =
		new QName("http://dspace.gndms.zib.de/DSpace", "DSpaceKey")
	private static final List<String> RESOURCE_NAMES = ["DSpace"].asImmutable()

	private GridSiteId gridSiteId;
	private String resourceKeyValue;

	@Transient
	QName getResourceKeyName() { RESOURCE_KEY_NAME }
	@Transient
	List<String> getResourceNames() { RESOURCE_NAMES }

	@Embedded
	GridSiteId getGridSiteId() { gridSiteId }
	void setGridSiteId(final GridSiteId newSiteId) { gridSiteId = newSiteId }

	@Column(name="key_val", nullable=false, updatable=false, columnDefinition="CHAR", length=36)
	String getResourceKeyValue() { resourceKeyValue }
	void setResourceKeyValue(final String newValue) { resourceKeyValue = newValue }
}
