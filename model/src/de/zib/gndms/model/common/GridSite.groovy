package de.zib.gndms.model.common

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import de.zib.gndms.model.ModelId

/**
 * A grid site maps from a unique id to a transportURL of the relevant container.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 12:53:02
 */
@Entity(name="GridSites")
@Table(name="grid_sites", schema="common")
class GridSite extends GridEntity {
	@EmbeddedId
	GridSiteId siteId;

	@Column(name="transort_url", nullable=false, columnDefinition="VARCHAR")
	String transportURL;
}

@Embeddable
class GridSiteId extends ModelId {
	@Column(name="grid_name", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
	String gridName

	@Column(name="site_id", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64)
	String siteId;


	protected boolean equalFields(Object obj) {
		GridSiteId other = ((GridSiteId)obj)
		return gridName == other.gridName && siteId == other.siteId
	}


	public int hashCode() {
		return hashCode0(gridName) ^ hashCode0(siteId)
	}
}