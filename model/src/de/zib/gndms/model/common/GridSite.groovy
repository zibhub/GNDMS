package de.zib.gndms.model.common

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import de.zib.gndms.model.ModelEntity


/**
 * A grid site maps from a unique id to a transportURL of the relevant container.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 12:53:02
 */
@Entity(name="sites")
@Table(name="sites") @UniqueConstraint(columnNames = ["transportURL"])
class GridSite extends ModelEntity {
	@Id @Column(name="id", nullable=false, updatable=false, columnDefinition="VARCHAR")
	String id;

	@Column(name="transortURL", nullable=false, columnDefinition="VARCHAR")
	String transportURL;
}