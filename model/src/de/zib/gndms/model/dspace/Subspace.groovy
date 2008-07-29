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
import javax.persistence.Id
import javax.persistence.Version
import javax.persistence.Table
import javax.persistence.Column
import org.jetbrains.annotations.NotNull
import de.zib.gndms.model.common.EPRT
import javax.persistence.Embedded

@Entity(name="subspace") @Table(name="subspace")
class Subspace {
	@Id @Column(name="uuid", nullable=false, length=36, columnDefinition="CHAR") @NotNull
	String uuid;

	@Embedded @Column(name="total", nullable=false) @NotNull
	StorageSize totalSize;

	@Embedded @Column(name="avail", nullable=false) @NotNull
	StorageSize availableSize;

	@Column(name="tod", nullable=false) @NotNull
	Calendar terminationTime;

	@Column(name="spec", nullable=false, columnDefinition="VARCHAR") @NotNull
	String subspaceSpecifier;

	@Embedded @Column(name="dspace", nullable=false) @NotNull
	EPRT dSpaceRef;

	@Version
	int version;
}