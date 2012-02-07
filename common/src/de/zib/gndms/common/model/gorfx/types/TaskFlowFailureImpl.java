package de.zib.gndms.common.model.gorfx.types;
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

/**
 * @author try ma ik jo rr a zib
 * @date 02.03.11  11:22
 * @brief Default implementation of a the task flow failure.
 */
public class TaskFlowFailureImpl implements TaskFlowFailure {

    String message = null;
    TaskFailure taskFailure=null;

    public boolean hasFailed() {
        return message != null;
    }


    public String getFailureMessage() {
        return message;
    }


    public boolean hasTaskFailure() {
        return taskFailure != null;
    }


    public TaskFailure getTaskFailure() {
        return taskFailure;
    }


    public void setMessage( String message ) {
        this.message = message;
    }


    public void setTaskFailure( TaskFailure taskFailure ) {
        this.taskFailure = taskFailure;
    }
}
