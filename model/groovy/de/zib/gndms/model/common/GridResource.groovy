package de.zib.gndms.model.common

import javax.persistence.MappedSuperclass
import javax.persistence.Column
import javax.persistence.Id

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
