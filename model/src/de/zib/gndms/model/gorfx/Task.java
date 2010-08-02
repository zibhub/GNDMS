package de.zib.gndms.model.gorfx;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import javax.persistence.*;


/**
 * ThingAMagic.
 * 
 * @author: try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 05.09.2008 Time: 13:41:58
 */
@Entity(name="Tasks")
@Table(name="tasks", schema="gorfx")
@NamedQueries( {
@NamedQuery(name = "listAllTaskIds", query="SELECT instance.id FROM Tasks instance"),
@NamedQuery(name="unfinishedTaskIds", query="SELECT t.id FROM Tasks t WHERE t.state <> de.zib.gndms.model.gorfx.types.TaskState.FAILED AND t.state <> de.zib.gndms.model.gorfx.types.TaskState.FINISHED AND t.postMortem=false" )
})
//@MappedSuperclass
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


