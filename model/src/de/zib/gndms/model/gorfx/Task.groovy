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
import javax.persistence.NamedQuery
import javax.persistence.MappedSuperclass


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
@NamedQuery(name="listAllTaskIds", query="SELECT instance.id FROM Tasks instance")
@MappedSuperclass
class Task extends TimedGridResource {
    /* Nullable for testing purposes */
    @ManyToOne @JoinColumn(name="offerTypeKey", nullable=true, updatable=false, columnDefinition="VARCHAR")
    OfferType offerType
    
    @Column(name="descr", nullable=false, updatable=false, columnDefinition="VARCHAR")
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

    @Column(name = "done", nullable=false, updatable=true)
    boolean done = false

    /**
     * If true, this task has been reloaded from the db, e.g. after a system crash and recovery.
     *
     * Set to false by the TaskAction(em, pk) constructor after loading the task from the db.
     *
     * TaskActions may use this flag to differentiate between "normal" and "recovery" situations
     * and automatically set it to true on every persisted state transition.
     * 
     **/
    transient boolean newTask = true
    
    @Column(name="progress", nullable=false, updatable=true)
    int progress = 0

    @Column(name="max_progress", nullable=false, updatable=false)
    int max_progress = 100

    @Column(name="orq", nullable=false, updatable=false)
    @Basic Serializable orq


    @Column(name="fault", nullable=true, updatable=true, columnDefinition="VARCHAR")
    @Basic String faultString
    /**
     * Payload depending on state, either task results or a detailed task failure or task arguments 
     **/
    @Column(name="data", nullable=true, updatable=true)
    @Basic Serializable data



   def transit(final TaskState newState) {
        final @NotNull TaskState goalState = newState == null ? getState() : newState;
        setState(getState().transit(goalState))
        setNewTask(true);
    }



    def void fail(final @NotNull Exception e) {
        setState(getState().transit(TaskState.FAILED))
        setFaultString(e.getMessage())
        setData(e)
        setProgress(0)
    }

    def void finish(final Serializable result) {
        setState(getState().transit(TaskState.FINISHED))
        setFaultString("")
        setData(result)
        setProgress(max_progress)
    }
}

@Embeddable
class Contract {

    // the comments denote the mapping to the
    // XSD OfferExecutionContract type

    // can be mapped to IfDesisionBefore
    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar accepted

    // can be mapped to ExecutionLiklyUntil
    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar deadline

    // can be mapped to ResultValidUntil
    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar resultValidity

    // can be mapped to constantExecutionTime
    transient boolean deadlineIsOffset = false
}

