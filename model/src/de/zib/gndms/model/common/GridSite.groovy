package de.zib.gndms.model.common

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import de.zib.gndms.model.ModelId
import javax.persistence.Id
import javax.persistence.MappedSuperclass

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
@MappedSuperclass
class TaskGridSite extends GridEntity {
	@Id
    @Column(name="site_id", nullable=true, updatable=false, columnDefinition="CHAR", length=64)
	String siteId;

	@Column(name="transort_url", nullable=false, columnDefinition="VARCHAR")
	String transportURL;
}
