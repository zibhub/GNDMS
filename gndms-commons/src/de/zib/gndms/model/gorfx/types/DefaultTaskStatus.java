package de.zib.gndms.model.gorfx.types;
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
 * @date 14.02.11  18:40
 * @brief Default implementation of TaskStatus.
 *
 * Provided a status enum, and numeric progress information.
 */
public class DefaultTaskStatus implements TaskStatus {

    private TaskStatus.Status status = Status.WAITING; ///< The current status of the task.
    private int progress = 0;  ///< The current progress.
    private int maxProgress = 100;  ///< The maximum progress.

    /**
     * @brief Delivers the value of ::status.
     * 
     * @return The value of ::status.
     */
    public Status getStatus() {
        return status;
    }


    /**
     * @brief Delivers the value of ::progress.
     * 
     * @return The value of ::progress.
     */
    public int getProgress() {
        return progress;
    }


    /**
     * @brief Delivers the value of ::maxProgress.
     * 
     * @return The value of ::maxProgress.
     */
    public int getMaxProgress() {
        return maxProgress;
    }


    /**
     * @brief Sets the value of ::status to \e status.
     * 
     * @param status The new value of ::status.
     */
    public void setStatus( Status status ) {
        this.status = status;
    }


    /**
     * @brief Sets the value of ::progress to \e progress.
     * 
     * @param progress The new value of ::progress.
     */
    public void setProgress( int progress ) {
        this.progress = progress;
    }


    /**
     * @brief Sets the value of ::maxProgress to \e maxProgress.
     * 
     * @param maxProgress The new value of ::maxProgress.
     */
    public void setMaxProgress( int maxProgress ) {
        this.maxProgress = maxProgress;
    }
}
