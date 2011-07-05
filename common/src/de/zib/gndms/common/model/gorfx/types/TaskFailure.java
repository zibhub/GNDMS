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
 *         Date: 09.02.11, Time: 17:45
 *
 * @brief Interface for a task failure.
 *
 * Implementations commonly encapsulating an exception.
 */
public interface TaskFailure {

    /** 
     * @brief The message for the failure.
     *  
     * Should be human readable.
     *
     * @return Human readable description of the failure.
     */
    String getMessage(); 

    /** 
     * @brief Stack trace of the failure.
     * 
     * @return Stack trace as string.
     */
    String getFaultTrace();

    /** 
     * @brief The kind of the failure.
     * 
     * @return  The failure kind, possible the class name of the
     * exception.
     */
    String getFaultClass();

    /** 
     * @brief The location of the failure.
     * 
     * @return A string representation of the failure location, e.g.
     * Class:line
     */
    String getFaultLocation();


    /**
     * @brief Checks if this failure carries another one.
     *
     * @return \c true if there are more failure objects.
     */
    boolean hasNext();

    /**
     * @brief Allows changing of failures messages.
     *
     * @return The next failure object if existing.
     */
    TaskFailure getNext();
}
