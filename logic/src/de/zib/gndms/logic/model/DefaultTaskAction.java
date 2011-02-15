package de.zib.gndms.logic.model;

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

import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import de.zib.gndms.neomodel.gorfx.NeoTaskAccessor;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * DefaultTaskAction
 *
 * Utility class to subclass from when implementing TaskAction. Instead of having to switch on the task state,
 * subclassees may simply override the provided callbacks as they need.
 *
 * @see TaskAction
 * @author  try ste fan pla nti kow zib
 *
 * User: stepn Date: 15.02.2011 Time: 14:11
 */
public class DefaultTaskAction extends TaskAction {
    public DefaultTaskAction() {
        super();
    }

    public DefaultTaskAction(@NotNull EntityManager em, @NotNull NeoDao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    protected void onTransit(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        final TaskState state = snapshot.getTaskState();
        switch(state) {
            case CREATED:
                onCreated(snapshot, isRestartedTask);
                break;
            case INITIALIZED:
                onInitialized(snapshot, isRestartedTask);
                break;
            case IN_PROGRESS:
                onInProgress(snapshot, isRestartedTask);
                break;
            case FINISHED:
                onFinished(snapshot, isRestartedTask);
            case FAILED:
                onFailed(snapshot, isRestartedTask);
        }
    }

    protected void onCreated(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        super.autoTransit();
    }

    protected void onInitialized(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        super.autoTransit();
    }

    protected void onInProgress(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        super.autoTransit();
    }

    protected void onFinished(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        super.autoTransit();
    }


    protected void onFailed(@NotNull NeoTaskAccessor snapshot, boolean isRestartedTask) {
        super.autoTransit();
    }

    protected @NotNull NeoTaskAccessor getTaskSnapshot() {
        final NeoSession session = getDao().beginSession();
        try {
            final NeoTask task = getModel().getTask(session);
            final NeoTaskAccessor result = task.getSnapshot();
            session.success();
            return result;
        }
        finally { session.finish(); }
    }
}
