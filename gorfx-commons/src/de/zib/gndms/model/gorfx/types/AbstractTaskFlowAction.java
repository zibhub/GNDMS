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

import de.zib.gndms.logic.taskflow.TaskFlowAction;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:44
 * @brief
 */
public abstract class AbstractTaskFlowAction<T extends AbstractTF> implements TaskFlowAction<T> {

    private TaskFlow<T> taskFlow;
    private TaskStatus status;
    private TaskResult result;
    private TaskFlowFailure failure;


    public TaskFlow<T> getTaskFlow() {
        return taskFlow;
    }


    public void setTaskFlow( TaskFlow<T> tf ) {
        this.taskFlow = tf;
    }


    public boolean hasStatus() {
        return status != null;
    }


    public TaskStatus getStatus() {
        return status;
    }


    public boolean hasError() {
        return failure != null;
    }


    public TaskFlowFailure getError() {
        return failure;
    }


    public boolean hasResult() {
        return result != null;
    }


    public TaskResult getResult() {
        return result;
    }


    protected void setStatus( TaskStatus status ) {
        this.status = status;
    }




    protected void setResult( TaskResult result ) {
        this.result = result;
    }


    protected void setFailure( TaskFlowFailure failure ) {
        this.failure = failure;
    }


    protected void setFailure ( Exception e ) {

        TaskFlowFailureImpl tff = new TaskFlowFailureImpl( );

        tff.setMessage( e.getMessage() );
        StringWriter sw = new StringWriter( );
        PrintWriter pw  = new PrintWriter( sw );
        e.printStackTrace( pw );
        pw.close( );
        tff.setFaultTrace( sw.toString() );
        tff.setFaultClass( e.getClass().getName() );

        StackTraceElement[] se = e.getStackTrace();
        if( se.length > 0 )
            tff.setFaultLocation( se[0].toString() );

        setFailure( tff );
    }
}
