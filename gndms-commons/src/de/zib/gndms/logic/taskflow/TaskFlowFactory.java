package de.zib.gndms.logic.taskflow;
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

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.types.TaskFlow;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:27
 * @brief An interface for taskflow factories.
 *
 * Implementations of this interface should be able to compute quotes, and generate task instances.
 *
 * \note maybe switch this interface to use generics instead of polymorphism, if you know what I mean...
 */
public interface TaskFlowFactory<T extends TaskFlow > {

    /** 
     * @brief Delivers a calculator for quotes of the taskflow.
     * 
     * @return A quote calculator.
     */
    AbstractQuoteCalculator getQuoteCalculator(  );

    /** 
     * @brief Delivers information about the taskflow in general. 
     * 
     * The information should contain at least a description of the
     * task.
     *
     * @return A task info object.
     */
    TaskFlowInfo getInfo( );

    /**
     * @brief Creates a new taskflow instance.
     *
     * The created task is registered in its factory.
     *
     * @return A taskflow object.
     */
    T create( );

    /**
     * @brief Finds an existing taskflow.
     *
     * @param id The id of the taskflow.
     * @return The taskflow or null if it doesn't exist.
     */
    T find( String id );

    /**
     * @brief Removes a taskflow.
     *
     * @param id The id of the taskflow.
     */
    void delete( String id );

    /**
     * @brief Delivers the class of the order type.
     *
     * @return The class of the taskflows order type.
     */
    Class getOrderClass( );

    /**
     * @brief Creates a task action.
     *
     *
     * @return The newly created action.
     */
    TaskAction createAction( );
}
