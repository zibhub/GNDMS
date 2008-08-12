package de.zib.gndms.model.common

import de.zib.gndms.model.ModelEntity
import javax.persistence.*

/**
 * Superclass of grid entities with a version field
 *
 **/
@MappedSuperclass
abstract class GridEntity extends ModelEntity {
	@Version
	int version

	@Column(name="sys_id", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
	String systemId
}






