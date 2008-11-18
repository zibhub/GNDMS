package de.zib.gndms.model.gorfx

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.NamedQuery
import javax.persistence.CascadeType
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.NamedQueries
import javax.persistence.MappedSuperclass

/**
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *   
 * User: mjorra, Date: 17.11.2008, Time: 17:28:21
 */

@Entity(name="SubTasks")
@Table(name="sub_tasks", schema="gorfx")
@MappedSuperclass
@NamedQueries([
@NamedQuery( name="findSubTasks", query="SELECT * FROM SubTasks x WHERE t.parent_id = :idparm" ),
@NamedQuery( name="findSubTasksOfType",  query="SELECT * FROM SubTasks x WHERE t.parent_id = :idParm AND t.offerTypeKey.offer_type_key = :typKeyParm" )])
class SubTask extends Task{

    @ManyToOne( targetEntity=Task.class )
    @JoinColumn( name="parent_id", nullable=false, referencedColumnName="id", updatable=false )
    Task parent;


    SubTask( ) {

    }


    SubTask( Task par ) {
        parent = par;
    }
}