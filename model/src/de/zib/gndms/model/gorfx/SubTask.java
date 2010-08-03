package de.zib.gndms.model.gorfx;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.MappedSuperclass;
import de.zib.gndms.model.gorfx.Task;
import javax.persistence.EntityManager;

/**
 *
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *
 * User: mjorra, Date: 17.11.2008, Time: 17:28:21
 */

@Entity(name="SubTasks")
@Table(name="sub_tasks", schema="gorfx")
@NamedQueries({
@NamedQuery( name="findSubTasks", query="SELECT * FROM SubTasks x WHERE t.parent_id = :idparm" ),
@NamedQuery( name="findSubTasksOfType",  query="SELECT * FROM SubTasks x WHERE t.parent_id = :idParm AND t.offerTypeKey.offer_type_key = :typKeyParm" )})
//@MappedSuperclass
public class SubTask extends AbstractTask {

   // @ManyToOne( targetEntity=Task.class )
   // @JoinColumn( name="parent_id", nullable=false, referencedColumnName="id", updatable=false )
   // Task parent;

    public SubTask( ) {
        super();
    }


    public SubTask( AbstractTask par ) {
    //    parent = par;
        par.add( this );
    }


    /**
     * Uses the static entiries of task tsk to update this subtask.
     *
     * Note: Neither the state nor the progress are updated.
     */
    public void fromTask ( EntityManager em, AbstractTask tsk ) {

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
        setWid( tsk.getWid( ) );
        setPermissions( tsk.getPermissions() ) ;
    }

}
