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

	@Column(name="grid", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
	String gridName
}

/**
 *
 * UUID-as-36-char-String identified grid resource
 *
 **/
@MappedSuperclass
abstract class GridResource extends GridEntity {
	@Id @Column(name="id", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
	String id
}

/**
 * GridResource + terminationTime
 **/
@MappedSuperclass
abstract class TimedGridResource extends GridResource {
	@Column(name="tod", nullable=false) @Temporal(value = TemporalType.TIMESTAMP)
	Calendar terminationTime
}



