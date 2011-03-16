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
 * @brief
 */
public class DefaultTaskStatus implements TaskStatus {

    private TaskStatus.Status status = Status.WAITING;
    private int progress = 0;
    private int maxProgress = 100;

    public Status getStatus() {
        return status;
    }


    public int getProgress() {
        return progress;
    }


    public int getMaxProgress() {
        return maxProgress;
    }


    public void setStatus( Status status ) {
        this.status = status;
    }


    public void setProgress( int progress ) {
        this.progress = progress;
    }


    public void setMaxProgress( int maxProgress ) {
        this.maxProgress = maxProgress;
    }
}
