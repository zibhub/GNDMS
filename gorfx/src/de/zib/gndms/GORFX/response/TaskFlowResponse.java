package de.zib.gndms.GORFX.response;

import java.util.List;
import java.util.ArrayList;
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

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 14.01.2011, Time: 17:51:49
 */
public class TaskFlowResponse extends ChildResponse {

    private List<String> taskTypes;


    public TaskFlowResponse() {
    }


    public TaskFlowResponse( String selfUrl, String parentUrl ) {
        super( selfUrl, parentUrl );
    }


    public TaskFlowResponse( String selfUrl, String parentUrl, List<String> taskTypes ) {
        super( selfUrl, parentUrl );
        this.taskTypes = this.taskTypes;
    }


    public List<String> getTaskTypes() {
        return taskTypes;
    }


    public void setTaskTypes( List<String> taskTypes ) {
        this.taskTypes = taskTypes;
    }


    public TaskFlowResponse addTaskType( String task )  {
        if( taskTypes == null )
            taskTypes = new ArrayList<String>( );
        taskTypes.add( task );

        return this;
    }
}
