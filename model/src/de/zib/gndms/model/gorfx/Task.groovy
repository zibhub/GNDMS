package de.zib.gndms.model.gorfx

import javax.persistence.Embeddable
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Basic
import de.zib.gndms.model.common.TimedGridResource
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import de.zib.gndms.model.gorfx.types.TaskState
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.Enumerated
import javax.persistence.EnumType
import org.jetbrains.annotations.NotNull


/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 05.09.2008 Time: 13:41:58
 */
@Entity(name="Tasks")
@Table(name="tasks", schema="gorfx")
class Task extends TimedGridResource {
    @ManyToOne @JoinColumn(name="key", nullable=false, updatable=false, columnDefinition="LONG VARCHAR")
    OfferType type
    
    @Column(name="descr", nullable=false, updatable=false, columnDefinition="LONG VARCHAR")
    String description

    /* always this.tod >= this.validity */
    @Embedded
    @AttributeOverrides([
        @AttributeOverride(name="accepted", column=@Column(name="accepted", nullable=false, updatable=false)),
        @AttributeOverride(name="deadline", column=@Column(name="deadline", nullable=true, updatable=false)),
        @AttributeOverride(name="resultValidity", column=@Column(name="validity", nullable=true, updatable=false)),
    ])
    Contract contract

    @Column(name="broken")
    boolean broken = false

    @Enumerated(EnumType.STRING)
    @Column(name="state", nullable=false, updatable=true)
    TaskState state = TaskState.CREATED

    @Column(name="progress", nullable=false, updatable=true)
    int progress = 0

    @Column(name="max_progress", nullable=false, updatable=false)
    int max_progress = 100

    @Column(name="fault", nullable=true, updatable=true, columnDefinition="LONG VARCHAR")
    @Basic String faultString

    /**
     * Payload depending on state, either task results or a detailed task failure or task arguments 
     **/
    @Column(name="data", nullable=true, updatable=true)
    @Basic Serializable data

    def void fail(final @NotNull Exception e) {
        state = TaskState.FAILED
        setFaultString(e.getMessage())
        setData(e)
        setProgress(0)
    }

    def void finish(final Serializable result) {
        state = TaskState.FINISHED
        setFaultString("")
        setData(result)
        setProgress(max_progress)
    }
}

@Embeddable
class Contract {
    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar accepted

    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar deadline

    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar resultValidity
    
    transient boolean deadlineIsOffset = false
}

