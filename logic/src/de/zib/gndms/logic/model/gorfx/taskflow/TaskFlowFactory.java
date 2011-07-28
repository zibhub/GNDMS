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

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:27
 * @brief An interface for taskflow factories.
 * <p/>
 * Implementations of this interface should be able to compute quotes, and generate task instances.
 */
public interface TaskFlowFactory<O extends Order, C extends AbstractQuoteCalculator<O>> {


    /**
     * @return The taskflow key.
     * @brief Delivers the key of this task flow.
     */
    String getTaskFlowKey();

    /**
     * @return An integer representing the version.
     * @brief Delivers the version of the plugin.
     */
    int getVersion();

    /**
     * @return A quote calculator.
     * @brief Delivers a calculator for quotes of the taskflow.
     */
    C getQuoteCalculator();

    /**
     * @return A task info object.
     * @brief Delivers information about the taskflow in general.
     * <p/>
     * The information should contain at least a description of the
     * task.
     */
    TaskFlowInfo getInfo();

    /**
     * @return A taskflow object.
     * @brief Creates a new taskflow instance.
     * <p/>
     * The created task is registered in its factory.
     */
    TaskFlow<O> create();

    /**
     * @return A taskflow object.
     * @brief Creates a new taskflow instance.
     * <p/>
     * The created task flow \bnot is registered in its factory.
     */
    TaskFlow<O> createOrphan();

    /**
     * @param taskflow The taskflow to add. Note the taskflow must have a unique id.
     * @return \c true if the taskflow was successfully added. If the task is already registered this will fail.
     * @brief Adds an orphan task flow to the factory.
     */
    boolean adopt( TaskFlow<O> taskflow );

    /**
     * @param id The id of the taskflow.
     * @return The taskflow or null if it doesn't exist.
     * @brief Finds an existing taskflow.
     */
    TaskFlow<O> find( String id );

    /**
     * @param id The id of the taskflow.
     * @brief Removes a taskflow.
     */
    void delete( String id );

    /**
     * @return The class of the taskflows order type.
     * @brief Delivers the class of the order type.
     */
    Class<O> getOrderClass();

    /**
     * @return The newly created action.
     * @brief Creates a task action.
     */
    TaskAction createAction();

    /**
     * @return A list of taskflow keys or an empty list if it has no dependencies. Note it \b must \b not return null.
     * @brief Delivers a list of keys of taskflows, this taskflow depends on.
     */
    Iterable<String> depends();

    DelegatingOrder<O> getOrderDelegate( O orq );
}
