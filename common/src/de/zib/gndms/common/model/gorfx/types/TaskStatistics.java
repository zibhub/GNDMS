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
 * @date 02.03.11  11:58
 * @brief Class for task and taskflow statistics:
 */
public class TaskStatistics {

    private String type;  ///< The type of the task(flow)
    private int active;   ///< The number of active task(flows)
    private int finished; ///< The number of finished task(flows)
    private int failed;   ///< The number of failed task(flows)
    private double avgRuntime; ///< The average runtime of a task(flow)


    public String getType() {
        return type;
    }


    public void setType( String type ) {
        this.type = type;
    }


    public int getActive() {
        return active;
    }


    public void setActive( int active ) {
        this.active = active;
    }


    public int getFinished() {
        return finished;
    }


    public void setFinished( int finished ) {
        this.finished = finished;
    }


    public int getFailed() {
        return failed;
    }


    public void setFailed( int failed ) {
        this.failed = failed;
    }


    public double getAvgRuntime() {
        return avgRuntime;
    }


    public void setAvgRuntime( double avgRuntime ) {
        this.avgRuntime = avgRuntime;
    }
}
