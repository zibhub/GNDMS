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
import javax.persistence.ManyToOne
import javax.persistence.JoinColumns
import javax.persistence.JoinColumn
import javax.persistence.AttributeOverrides
import javax.persistence.AttributeOverride
import javax.persistence.UniqueConstraint
import de.zib.gndms.model.common.TimedGridResource

@Entity(name="Subspaces")
@Table(name="subspaces") @UniqueConstraint(columnNames=["specifier"])
class Subspace extends TimedGridResource {	
	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="totalUnit")),
		  @AttributeOverride(name="amount", column=@Column(name="totalAmount"))
	])
	StorageSize totalSize

	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="availUnit")),
		  @AttributeOverride(name="amount", column=@Column(name="availAmount"))
	])
	StorageSize availableSize

	@Column(name="specifier", nullable=false, columnDefinition="VARCHAR", updatable=false)
	String subspaceSpecifier

	@Column(name="isPublic")
	boolean publicSubspace

	@ManyToOne(optional=false)
	@JoinColumns([@JoinColumn(name="siteId", referencedColumnName="siteId", nullable=true, updatable=false),
	              @JoinColumn(name="dspaceId", referencedColumnName="rkValue", nullable=false, updatable=false)])
	DSpaceVEPRef dSpaceRef
}