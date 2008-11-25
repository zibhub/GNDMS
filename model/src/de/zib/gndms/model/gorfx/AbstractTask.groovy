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
import javax.persistence.NamedQueries
import javax.persistence.OneToMany
import javax.persistence.CascadeType


/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 05.09.2008 Time: 13:41:58
 */
@MappedSuperclass
abstract class AbstractTask extends TimedGridResource {
    
    public void stampOn ( EntityManager em, AbstractTask newTask ) {
        newTask .offerType = offerType == null ? null : em.find(OfferType.class, offerType.getOfferTypeKey());
        if (newTask.offerType != null)
            newTask.offerType.getTasks().add( newTask  );
        newTask.description = description;
        newTask.state = state;
        newTask.progress = progress;
        newTask.maxProgress = maxProgress;
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
    int maxProgress = 100

    @Column(name="orq", nullable=false, updatable=false)
    @Basic Serializable orq


    @Column(name="fault", nullable=true, updatable=true, columnDefinition="VARCHAR")
    @Basic String faultString
    /**
     * Payload depending on state, either task results or a detailed task failure or task arguments 
     **/
    @Column(name="data", nullable=true, updatable=true)
    @Basic Serializable data


    @Column(name="wid", nullable=true, updatable=true)
    @Basic String wid;

    @OneToMany( targetEntity=SubTask.class, cascade=[CascadeType.REMOVE] )
    List<SubTask> subTasks;
    

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
        setProgress(maxProgress)
    }

    void add( SubTask st ) {
        subTasks.add( st );
    }
}


