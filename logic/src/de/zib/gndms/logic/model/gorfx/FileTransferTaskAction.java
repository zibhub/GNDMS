package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.logic.model.TaskAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends TaskAction<Task> {

    // note set new task to false when restoring a task form data base
    
    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull Task model ) {
        super( em, model );
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull String pk ) {
        super( em, pk );
    }


    @NotNull
    protected Class<Task> getTaskClass() {
        return null;  // Not required here
    }


    protected void onInProgress( @NotNull Task model ) {
        // Not required here
    }
}
