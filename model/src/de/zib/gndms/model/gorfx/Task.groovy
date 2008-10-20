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
import org.joda.time.DateTime
import org.joda.time.Duration
import javax.persistence.EntityManager


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
    
    public void stampOn ( EntityManager em, Task newTask) {
        newTask.offerType = offerType == null ? null : em.find(OfferType.class, offerType.getOfferTypeKey());
        if (newTask.offerType != null)
            newTask.offerType.getTasks().add(newTask);
        newTask.description = description;
        newTask.state = state;
        newTask.progress = progress;
        newTask.contract = contract;
        newTask.orq = orq;
        newTask.faultString = faultString;
        newTask.data = data;
    }

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
        final @NotNull TaskState transitState = getState().transit(goalState)
       assert transitState != null
       setState(transitState)
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
    // this must be at least equal to the deadline
    @Temporal(value = TemporalType.TIMESTAMP)
    Calendar resultValidity

    // can be mapped to constantExecutionTime
    transient boolean deadlineIsOffset = false

    Calendar getCurrentDeadline() {
        return  ((deadlineIsOffset) ?
            new DateTime(deadline).plus(new Duration(new DateTime(0L), new DateTime(deadline))).toGregorianCalendar() : deadline)
    }

    public Calendar getCurrentTerminationTime() {
        Calendar deadline = getCurrentDeadline();
        return (deadline.compareTo(resultValidity) <= 0) ? resultValidity : deadline;

    }

    public void setDeadLine( Calendar dl ) {
        deadline = dl;
        if( resultValidity == null )
            resultValidity = dl;
    }
}

