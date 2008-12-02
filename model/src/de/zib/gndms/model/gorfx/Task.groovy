package de.zib.gndms.model.gorfx

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
@Entity(name="Tasks")
@Table(name="tasks", schema="gorfx")
@NamedQueries( [
@NamedQuery(name="listAllTaskIds", query="SELECT instance.id FROM Tasks instance"),
@NamedQuery(name="unfinishedTaskIds", query="SELECT t.id FROM Tasks t WHERE t.state <> de.zib.gndms.model.gorfx.types.TaskState.FAILED AND t.state <> de.zib.gndms.model.gorfx.types.TaskState.FINISHED" )
])
@MappedSuperclass
class Task extends AbstractTask {


}


