package de.zib.gndms.model.common;

import javax.persistence.MappedSuperclass;

/**
 * GridResource + terminationTime
 **/
@MappedSuperclass
public abstract class SingletonGridResource extends GridResource {
    // @Column(name="grid", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
    // String gridName
}
