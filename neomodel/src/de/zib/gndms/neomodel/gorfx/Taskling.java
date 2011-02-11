package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.GridResourceItf;
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
public class Taskling extends GridEntity implements GridResourceItf {
    final @NotNull NeoDao dao;
    @NotNull String id;

    public Taskling(@NotNull NeoDao dao, @NotNull String taskId) {
        this.id = taskId;
        this.dao    = dao;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    @NotNull NeoDao getDao() {
        return dao;
    }

    @NotNull NeoTask getTask(@NotNull NeoSession neoSession) {
        return neoSession.findTask(getId());
    }
}
