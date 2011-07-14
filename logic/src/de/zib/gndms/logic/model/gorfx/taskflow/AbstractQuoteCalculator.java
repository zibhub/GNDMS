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
import de.zib.gndms.model.gorfx.types.DelegatingOrder;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:43
 * @brief A task flow calculator interface.
 *
 * This will be merged with the orq calculator of the old GNDMS.
 *
 * It computes quotes and checks the satisfiability of of a order.
 */
public interface AbstractQuoteCalculator<T extends Order> {

    /** 
     * @brief Sets the order 
     * 
     * @param order The order of the task flow.
     */
    void setOrder( DelegatingOrder<T> order );

    /** 
     * @brief Validates if a order is satisfiable.
     *
     * @return \c true if that's the case.
     */
    boolean validate( );

    /** 
     * @brief Computes quotes fo the order.
     * 
     * @return A list of quotes for the order.
     */
    List<Quote> createQuotes( ) throws UnsatisfiableOrderException;


    /** 
     * @brief Computes Quotes for an order, pays respect to a
     * preferred taskflow execution.
     * 
     * @param preferred The preferred taskflow execution.
     * 
     * @return A list of quotes for the order.
     */
    List<Quote> createQuotes( Quote preferred ) throws UnsatisfiableOrderException;
}
