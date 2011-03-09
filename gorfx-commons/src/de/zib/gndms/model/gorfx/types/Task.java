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
 * @date 04.03.11  12:14
 * @brief
 */
public class Task<T> {

    private String id;
    private T model;
    private TaskStatus status;
    private TaskResult result;
    private TaskFailure failure;


    public Task() {
    }


    public Task( String id ) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


    public void setId( String id ) {
        this.id = id;
    }


    public synchronized T getModel() {
        return model;
    }


    public synchronized void setModel( T model ) {
        this.model = model;
    }


    public synchronized TaskStatus getStatus() {
        return status;
    }


    public synchronized void setStatus( TaskStatus taskStatus ) {
        this.status = taskStatus;
    }


    public synchronized boolean hasStatus() {
        return status != null;
    }


    public synchronized boolean hasError() {
        return failure != null;
    }


    public synchronized TaskFailure getError() {
        return failure;
    }


    public synchronized boolean hasResult() {
        return result != null;
    }


    public synchronized TaskResult getResult() {
        return result;
    }


    public synchronized void setResult( TaskResult result ) {
        this.result = result;
    }


    public synchronized void setFailure( TaskFailure failure ) {
        this.failure = failure;
    }
}
