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

import de.zib.gndms.logic.taskflow.TaskAction;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:44
 * @brief
 */
public abstract class AbstractTaskAction<T extends AbstractTF> implements TaskAction<T> {

    private Task<T> task;


    protected AbstractTaskAction( ) {
    }


    protected AbstractTaskAction( Task<T> task ) {
        this.task = task;
    }


    public Task<T> getTask() {
        return task;
    }


    public void setTask( Task<T> tf ) {
        this.task = tf;
    }


    public void run() throws Exception{

        try {
            onInit();
            onProgress();
        } catch ( Exception e ) {
            onFailed( e );
            throw e;
        }
    }


    public void onFailed( Exception e ) {
        setFailure( e );
    }


    protected void setFailure ( Exception e ) {

        TaskFailureImpl tff = new TaskFailureImpl( );

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

        task.setFailure( tff );
    }
}
