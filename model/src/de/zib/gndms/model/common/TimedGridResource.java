package de.zib.gndms.model.common;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




/**
 * GridResource + terminationTime
 **/
@MappedSuperclass
abstract class TimedGridResource extends GridResource {
    @Column(name="tod", nullable=false) @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar terminationTime;
}

