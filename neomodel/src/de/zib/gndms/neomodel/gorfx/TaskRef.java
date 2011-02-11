package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 11.02.11
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class TaskRef extends GridEntity {
    final @NotNull String taskId;
    final @NotNull NeoDao dao;

    public TaskRef(@NotNull String taskId, @NotNull NeoDao dao) {
        this.taskId = taskId;
        this.dao    = dao;
    }

    @NotNull String getTaskId() {
        return taskId;
    }

    @NotNull NeoDao getDao() {
        return dao;
    }

    @NotNull NeoTask getTask(@NotNull NeoSession neoSession) {
        return neoSession.findTask(getTaskId());
    }
}
