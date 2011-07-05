package de.zib.gndms.logic.model.gorfx.taskflow;
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

import de.zib.gndms.common.model.gorfx.types.DefaultTaskStatus;
import de.zib.gndms.common.model.gorfx.types.TaskFailure;
import de.zib.gndms.common.model.gorfx.types.TaskFailureImpl;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.neomodel.gorfx.Task;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author try ma ik jo rr a zib
 * @date 08.04.11  17:33
 * @brief Set of converters types related to Task.
 *
 *
 */
public final class TaskTypeConverter {


    public static TaskStatus statusFormTask( Task task ) {

        boolean inTime = false;
        Calendar tt =  task.getTerminationTime();
        if( tt != null )
            inTime = new GregorianCalendar(  ).compareTo( tt ) != -1 ;

        DefaultTaskStatus status = new DefaultTaskStatus( );
        switch ( task.getTaskState() ) {
            case CREATED:
            case CREATED_UNKNOWN:
            case INITIALIZED:
            case INITIALIZED_UNKNOWN:
            case IN_PROGRESS:
            case IN_PROGRESS_UNKNOWN:
                // todo check executor if task is currently executed
                if( inTime )
                    status.setStatus( TaskStatus.Status.KILLED );
                else
                    status.setStatus( TaskStatus.Status.RUNNING );
            case FAILED:
                status.setStatus( TaskStatus.Status.FAILED );
                break;
            case FINISHED:
                status.setStatus( TaskStatus.Status.FINISHED );
                break;
        }
        status.setProgress( task.getProgress() );
        status.setProgress( task.getMaxProgress() );

        return status;
    }


    public static TaskFailure failStackToList( LinkedList<Exception> exceptions ) {

        Iterator<Exception> it = exceptions.iterator();
        TaskFailureImpl last = null;
        while ( it.hasNext() ) {
            TaskFailureImpl cur = ( TaskFailureImpl ) TaskFailureImpl.failureFromException( it.next() );
            cur.setNext( last );
            last = cur;
        }

        return last;
    }




    private TaskTypeConverter() { }
}
