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

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
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
public class DefaultTaskAction<O extends Order> extends TaskAction<O> {
    private volatile boolean killAltTaskState = true;

    public DefaultTaskAction() {
        super();
    }


    public DefaultTaskAction( final Class<O> orderClass ) {

        super( orderClass );
    }


    public DefaultTaskAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    protected void onTransit(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        switch(state) {
            case CREATED:
                onCreated(wid, state, isRestartedTask, altTaskState);
                break;
            case INITIALIZED:
                onInitialized(wid, state, isRestartedTask, altTaskState);
                break;
            case IN_PROGRESS:
                onInProgress(wid, state, isRestartedTask, altTaskState);
                break;
            case FINISHED:
                onFinished(wid, state, isRestartedTask, altTaskState);
                break;
            case FAILED:
                onFailed(wid, state, isRestartedTask, altTaskState);
                break;
        }
    }

    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        super.autoTransit();
        if (killAltTaskState && altTaskState)
            removeAltTaskState();
    }

    protected void onInitialized(@NotNull String wid,
                                 @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception {
        super.autoTransit();
        if (killAltTaskState && altTaskState)
            removeAltTaskState();
    }

    protected void onInProgress(@NotNull String wid,
                                @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception {
        super.autoTransit();
        if (killAltTaskState && altTaskState)
            removeAltTaskState();
    }

    protected void onFinished(@NotNull String wid,
                              @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception {
        super.autoTransit();
        if (killAltTaskState && altTaskState)
            removeAltTaskState();
    }

    protected void onFailed(@NotNull String wid,
                            @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        super.autoTransit();
        if (killAltTaskState && altTaskState)
            removeAltTaskState();
    }

    public boolean isKillAltTaskState() {
        return killAltTaskState;
    }

    protected void setKillAltTaskState(boolean shouldKill) {
        killAltTaskState = shouldKill;
    }
}
