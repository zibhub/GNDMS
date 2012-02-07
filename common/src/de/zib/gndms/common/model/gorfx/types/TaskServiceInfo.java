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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 02.03.11  12:03
 * @brief Object  with common task service information.
 */
public class TaskServiceInfo {

    private List<TaskStatistics> statistics; ///< Statistics over all task types
    private List<String> taskTypes; ///< List of all task types

    List<TaskStatistics> getStatistics( ) {
        return statistics;
    }


    public void setStatistics( List<TaskStatistics> statistics ) {
        this.statistics = statistics;
    }


    public List<String> getTaskTypes() {
        return taskTypes;
    }


    public void setTaskTypes( List<String> taskTypes ) {
        this.taskTypes = taskTypes;
    }
}
