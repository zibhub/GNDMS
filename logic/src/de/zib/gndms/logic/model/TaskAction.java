package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.09.2008 Time: 11:26:48
 */
@SuppressWarnings({ "EmptyClass" })
public abstract class TaskAction<M extends Task, R> extends AbstractModelAction<M, R> {
    private ExecutorService service;


    public ExecutorService getService() {
        if (service == null) {
            final TaskAction<?, ?> taskAction = nextParentOfType(TaskAction.class);
            return taskAction == null ? null : taskAction.getService();
        }
        return service;
    }


    public void setService(final @NotNull ExecutorService serviceParam) {
        if (service == null)
            service = serviceParam;
        else
           throw new IllegalStateException("Can't overwrite service");
    }
}
