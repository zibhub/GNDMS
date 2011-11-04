package de.zib.gndms.taskflows.dummy;/*
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

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import org.jetbrains.annotations.NotNull;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:58
 * @brief The order class for a dummy taskflow.
 *
 * The dummy taskflow echos a given message a given number of times ^^ with some delay.
 * It can also fail on demand;
 */
public class DummyOrder extends AbstractOrder {

    private static final String TASK_FLOW_TYPE = "dummyTaskFlow";
    private boolean justEstimate = false;

    // some great attributes for this task-flow
    private String message; ///< The message to echo.
    private int times;      ///< The number of times the message will be repeated.
    private int delay;     ///< the delay in ms for each round;
    private boolean failIntentionally;///< Denotes if the task execution should fail.



    @Override
    public String getTaskFlowType() {
        return TASK_FLOW_TYPE;
    }


    public boolean isJustEstimate() {
        return justEstimate;
    }


    @NotNull
    @Override
    public String getDescription() {
        return "A dummy for TaskFlow processing";
    }


    public void setJustEstimate( boolean justEstimate ) {
        this.justEstimate = justEstimate;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage( String message ) {
        this.message = message;
    }


    public int getTimes() {
        return times;
    }


    public void setTimes( int times ) {
        this.times = times;
    }


    public int getDelay() {
        return delay;
    }


    public void setDelay( int delay ) {
        this.delay = delay;
    }


    public boolean isFailIntentionally() {
        return failIntentionally;
    }


    public void setFailIntentionally( boolean failIntentionally ) {
        this.failIntentionally = failIntentionally;
    }


    @Override
    public String toString() {
        return "DummyOrder{" +
            "justEstimate=" + justEstimate +
            ", message='" + message + '\'' +
            ", times=" + times +
            ", delay=" + delay +
            ", failIntentionally=" + failIntentionally +
            '}';
    }
}
