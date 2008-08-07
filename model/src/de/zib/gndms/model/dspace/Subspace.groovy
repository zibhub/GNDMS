/**
 * Subspace model class
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */
package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.TimedGridResource
import de.zib.gndms.model.util.LifecycleEventDispatcher
import javax.persistence.*

/**
 *
 * Instances represent concrete subspaces in the local DSpace
 *
 */
@Entity(name="Subspaces") @EntityListeners([LifecycleEventDispatcher.class])
@Table(name="subspaces", schema="dspace")
class Subspace extends TimedGridResource {
	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="avail_unit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="avail_amount", nullable=false))
	])
	StorageSize availableSize

	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="total_unit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="total_amount", nullable=false))
	])
	StorageSize totalSize

	@OneToOne(targetEntity=MetaSubspace.class, optional=false, fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH])
	@PrimaryKeyJoinColumns([@PrimaryKeyJoinColumn(name="schema_uri"),
	                        @PrimaryKeyJoinColumn(name="specifier")])
	MetaSubspace metaSubspace

	@Embedded
	@AttributeOverrides([
		@AttributeOverride(name="gridSiteId", column=@Column(name="dspace_site", nullable=true, updatable=false)),
	    @AttributeOverride(name="resourceKeyValue", column=@Column(name="dspace_uuid", nullable=false, updatable=false))])
	DSpaceRef dSpaceRef
}