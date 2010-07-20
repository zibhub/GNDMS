package de.zib.gndms.model.gorfx;

import javax.persistence.*;


/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 05.09.2008 Time: 13:41:58
 */
@Entity(name="Tasks")
// @Table(name="tasks", schema="gorfx")
@NamedQueries( {
@NamedQuery(name = "listAllTaskIds", query="SELECT instance.id FROM Tasks instance"),
@NamedQuery(name="unfinishedTaskIds", query="SELECT t.id FROM Tasks t WHERE t.state <> de.zib.gndms.model.gorfx.types.TaskState.FAILED AND t.state <> de.zib.gndms.model.gorfx.types.TaskState.FINISHED AND t.postMortem=false" )
})
@MappedSuperclass
public class Task extends AbstractTask {

    private boolean postMortem = false;

    @Column(name="post_mortem", nullable=false)
    public boolean isPostMortem() {
        return postMortem;
    }

    public void setPostMortem(boolean postMortem) {
        this.postMortem = postMortem;
    }
}


