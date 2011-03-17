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
 * @brief Provisional task object (you know they already exist in the old GNDMS)
 *
 * Task is the data-object of a task action, it's used store results, additional states and provides infos
 * required for the task execution.
 *
 * In the real GNDMS it is persistent.
 */
public class Task<T> {

    private String id; ///< id of the task
    private T model;   ///< Computation model for the task, e.g. an order
    private TaskStatus status; ///< The current task status.
    private TaskResult result; ///< The result, iff available.
    private TaskFailure failure; ///< The error object, iff available.


    public Task() {
    }


    public Task( String id ) {
        this.id = id;
    }


    /**
     * @brief Delivers the value of ::id.
     * 
     * @return The value of ::id.
     */
    public String getId() {
        return id;
    }


    /**
     * @brief Sets the value of ::id to \e id.
     * 
     * @param id The new value of ::id.
     */
    public void setId( String id ) {
        this.id = id;
    }


    /**
     * @brief Delivers the value of ::model.
     * 
     * @return The value of ::model.
     */
    public synchronized T getModel() {
        return model;
    }


    /**
     * @brief Sets the value of ::model to \e model.
     * 
     * @param model The new value of ::model.
     */
    public synchronized void setModel( T model ) {
        this.model = model;
    }


    /**
     * @brief Delivers the value of ::status.
     * 
     * @return The value of ::status.
     */
    public synchronized TaskStatus getStatus() {
        return status;
    }


    /**
     * @brief Sets the value of ::status to \e taskStatus.
     * 
     * @param taskStatus The new value of ::status.
     */
    public synchronized void setStatus( TaskStatus taskStatus ) {
        this.status = taskStatus;
    }


    /**
     * @brief Delivers the value of ::status.
     * 
     * @return The value of ::status.
     */
    public synchronized boolean hasStatus() {
        return status != null;
    }


    /**
     * @brief Delivers the value of ::error.
     * 
     * @return The value of ::error.
     */
    public synchronized boolean hasError() {
        return failure != null;
    }


    /**
     * @brief Delivers the value of ::error.
     * 
     * @return The value of ::error.
     */
    public synchronized TaskFailure getError() {
        return failure;
    }


    public synchronized boolean hasResult() {
        return result != null;
    }


    /**
     * @brief Delivers the value of ::result.
     * 
     * @return The value of ::result.
     */
    public synchronized TaskResult getResult() {
        return result;
    }


    /**
     * @brief Sets the value of ::result to \e result.
     * 
     * @param result The new value of ::result.
     */
    public synchronized void setResult( TaskResult result ) {
        this.result = result;
    }


    /**
     * @brief Sets the value of ::failure to \e failure.
     * 
     * @param failure The new value of ::failure.
     */
    public synchronized void setFailure( TaskFailure failure ) {
        this.failure = failure;
    }
}
