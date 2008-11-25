package de.zib.gndms.model.gorfx

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.NamedQuery
import javax.persistence.CascadeType
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.NamedQueries
import javax.persistence.MappedSuperclass
import de.zib.gndms.model.gorfx.Task
import javax.persistence.EntityManager

/**
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *   
 * User: mjorra, Date: 17.11.2008, Time: 17:28:21
 */

@Entity(name="SubTasks")
@Table(name="sub_tasks", schema="gorfx")
@NamedQueries([
@NamedQuery( name="findSubTasks", query="SELECT * FROM SubTasks x WHERE t.parent_id = :idparm" ),
@NamedQuery( name="findSubTasksOfType",  query="SELECT * FROM SubTasks x WHERE t.parent_id = :idParm AND t.offerTypeKey.offer_type_key = :typKeyParm" )])
@MappedSuperclass
class SubTask extends AbstractTask {

   // @ManyToOne( targetEntity=Task.class )
   // @JoinColumn( name="parent_id", nullable=false, referencedColumnName="id", updatable=false )
   // Task parent;

    SubTask( ) {

    }


    SubTask( AbstractTask par ) {
        parent = par;
        par.add( this );
    }


    /**
     * Uses the static entiries of task tsk to update this subtask.
     *
     * Note: Neither the state nor the progress are updated.
     */
    void fromTask( EntityManager em, AbstractTask tsk ) {

        if( tsk.getOfferType( ) == null )
            setOfferType( null );
        else
            setOfferType( em.find( OfferType.class, tsk.getOfferType( ).getOfferTypeKey() ) );


        setContract( tsk.getContract( ) );
        setDescription( tsk.getDescription( ) );
        setData( tsk.getData( ) );
        setFaultString( tsk.getFaultString( ) );
        setMaxProgress( tsk.getMaxProgress( ) );
        setOrq( tsk.getOrq( ) );
    }
}
