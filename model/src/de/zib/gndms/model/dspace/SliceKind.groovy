package de.zib.gndms.model.dspace

import de.zib.gndms.model.ModelEntity
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.Inheritance
import javax.persistence.Table
import javax.persistence.DiscriminatorType
import javax.persistence.DiscriminatorColumn
import javax.persistence.ManyToMany
import javax.persistence.FetchType
import javax.persistence.CascadeType
//import de.zib.gndms.model.dspace.types.SliceKindMode
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

/**
 * SliceKinds are identified by a kindURI
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:17:45
 */
@Entity(name="SliceKinds")
@Inheritance
@DiscriminatorColumn(name="class", discriminatorType=DiscriminatorType.STRING, length=8)
@DiscriminatorValue("PLAIN")
@Table(name="slice_kinds", schema="dspace")
class SliceKind extends ModelEntity {
	@Id @Column(name="uri", nullable=false, updatable=false, columnDefinition="VARCHAR")
	String URI

	@Enumerated(EnumType.STRING)
	@Column(name="mode", nullable=false, updatable=false, columnDefinition="CHAR", length=8)
	de.zib.gndms.model.dspace.types.SliceKindMode mode

	@ManyToMany(fetch=FetchType.LAZY, mappedBy="creatableSliceKinds", cascade=[CascadeType.REFRESH, CascadeType.MERGE])
	Set<MetaSubspace> metaSubspaces
}
