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
import javax.persistence.Id
import de.zib.gndms.model.ModelEntity
import javax.persistence.Version

@Entity(name="subspaces")
@Table(name="subspaces") @UniqueConstraint(columnNames=["spec"])
class Subspace extends ModelEntity {
	@Id @Column(name="id", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
	String id

	@Column(name="tod", nullable=false)
	Calendar terminationTime

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

	@Column(name="spec", nullable=false, columnDefinition="VARCHAR", updatable=false)
	String subspaceSpecifier

	@ManyToOne(optional=false)
	@JoinColumns([@JoinColumn(name="site", referencedColumnName="site", nullable=true, updatable=false),
	              @JoinColumn(name="dspaceId", referencedColumnName="rkValue", nullable=false, updatable=false)])
	DSpaceVEPRef dSpaceRef

	@Version
	int version
}