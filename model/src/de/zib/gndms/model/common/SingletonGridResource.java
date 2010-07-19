package de.zib.gndms.model.common;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;


/**
 * GridResource + terminationTime
 **/
@MappedSuperclass
abstract public class SingletonGridResource extends GridResource {
	// @Column(name="grid", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
	// String gridName
}
