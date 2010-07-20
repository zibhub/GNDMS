package de.zib.gndms.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;


/**
 * GridResource + terminationTime
 **/
@MappedSuperclass
public abstract class TimedGridResource extends GridResource {

    private Calendar terminationTime;


    @Column(name="tod", nullable=false) @Temporal(value = TemporalType.TIMESTAMP)
    public Calendar getTerminationTime() {
        return terminationTime;
    }


    public void setTerminationTime( Calendar terminationTime ) {
        this.terminationTime = terminationTime;
    }
}

