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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  18:14
 * @brief Default implementation of TaskFailure.
 *
 * @see de.zib.gndms.common.model.gorfx.types.TaskFailure
 */
public class TaskFailureImpl implements TaskFailure {

    private String message;
    private String trace;
    private String name;
    private String frame;
    private TaskFailure failure = null; // for chaining


    public void setMessage( String message ) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public void setFaultTrace( String trace ) {
        this.trace = trace;
    }


    public String getFaultTrace() {
        return trace;
    }


    public void setFaultClass( String name ) {
        this.name = name;
    }


    public String getFaultClass() {
        return name;
    }


    public String getFaultLocation() {
        return frame;
    }


    public boolean hasNext() {
        return failure != null;
    }


    public TaskFailure getNext() {
        return failure;
    }


    public void setNext( TaskFailure failure ) {
        this.failure = failure;
    }


    public void setFaultLocation( String frame ) {
        this.frame = frame;
    }


    public static TaskFailure failureFromException( Throwable t ) {
        
        if( t == null )
            return null;

        TaskFailureImpl fault = new TaskFailureImpl();
        fault.setFaultClass( t.getClass().getName() );
        fault.setMessage( t.getMessage() );
        StringWriter sw = new StringWriter( );
        PrintWriter pw  = new PrintWriter( sw );
        t.printStackTrace( pw );
        pw.close( );
        fault.setFaultTrace( sw.toString() );

        StackTraceElement[] se = t.getStackTrace();
        if( se.length > 0 )
            fault.setFaultLocation( se[0].toString() );

        fault.setNext( failureFromException( t.getCause() ) );

        return fault;
    }

}
