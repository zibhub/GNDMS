package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * Dummy action that finishes with probability SUCCESS_RATE.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:06:35
 */
public class DummyTaskAction extends TaskAction {
    private double successRate = 1.0d;
    private long sleepInProgress;


    public DummyTaskAction() {
        super();
    }

    public DummyTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    protected void onTransit(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {

        switch(state) {
            case IN_PROGRESS:
                try {
                    Thread.sleep(sleepInProgress);
                }
                catch (InterruptedException e) {
                    transit(TaskState.FINISHED);
                    if (altTaskState)
                        getDao().removeAltTaskState(getModel().getId());
                    return;
                }

                if (Math.random() < successRate) {
                    transitWithPayload(1, TaskState.FINISHED);
                    if (altTaskState)
                        getDao().removeAltTaskState(getModel().getId());
                    return;
                } else {
                    if (altTaskState)
                        getDao().removeAltTaskState(getModel().getId());
                    throw new IllegalStateException("Random failure");
                }
            default:
                super.onTransit(wid, state, isRestartedTask, altTaskState);
        }
    }

    public double getSuccessRate() {
        return successRate;
    }


    public long getSleepInProgress() {
        return sleepInProgress;
    }


    public void setSuccessRate(final double successRateParam) {
        if (successRateParam < 0.0d)
            throw new IllegalArgumentException("succesRate must not be < 0.0d");
        if (successRateParam > 1.0d)
            throw new IllegalArgumentException("succesRate must not be > 1.0d");
        successRate = successRateParam;
    }


    public void setSleepInProgress(final long sleepInProgressParam) {
        if (sleepInProgressParam < 0L)
            throw new IllegalArgumentException("sleepInProgress must be >= 0L");
        sleepInProgress = sleepInProgressParam;
    }
}
