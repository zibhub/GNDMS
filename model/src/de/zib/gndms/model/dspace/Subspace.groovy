/**
 * Subspace model class
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */
package de.zib.gndms.model.dspace

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.AttributeOverrides
import javax.persistence.AttributeOverride
import javax.persistence.UniqueConstraint
import de.zib.gndms.model.common.TimedGridResource

@Entity(name="Subspaces")
@Table(name="subspaces") @UniqueConstraint(columnNames=["specifier"])
class Subspace extends TimedGridResource {
	@MappedQuery
	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="totalUnit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="totalAmount", nullable=false))
	])
	StorageSize totalSize

	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="availUnit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="availAmount", nullable=false))
	])
	StorageSize availableSize

	@Column(name="specifier", nullable=false, columnDefinition="VARCHAR", updatable=false)
	String subspaceSpecifier

	@Column(name="isPublic")
	boolean publicSubspace

	@Embedded
	@AttributeOverrides([
		@AttributeOverride(name="gridSiteId", column=@Column(name="dspaceSite", nullable=true, updatable=false)),
	    @AttributeOverride(name="resourceKeyValue", column=@Column(name="dspaceUUID", nullable=false, updatable=false))])
	DSpaceRef dSpaceRef
}