package de.zib.gndms.model.dspace

import de.zib.gndms.model.ModelEntity
import de.zib.gndms.model.dspace.types.SliceKindMode
import javax.persistence.*

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
	SliceKindMode mode

	@ManyToMany(fetch=FetchType.LAZY, mappedBy="creatableSliceKinds", cascade=[CascadeType.REFRESH, CascadeType.MERGE])
	Set<MetaSubspace> metaSubspaces
}
