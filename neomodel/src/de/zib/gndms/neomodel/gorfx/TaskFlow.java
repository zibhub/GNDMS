package de.zib.gndms.neomodel.gorfx;


import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;

import de.zib.gndms.model.gorfx.types.DelegatingOrder;

import java.util.ArrayList;
import java.util.List;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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
 * @date: 17.01.2011 10:33:40
 *
 * @brief A class which aggregates all objects which belong to a single task flow.
 */
public abstract class TaskFlow<T extends Order> {

    private String id; ///< Taskflow id
    private DelegatingOrder<T> order;  ///< The order
    private List<Quote> quotes; ///< The quotes, if computed.
    private Quote preferredQuote; ///< A quote provided by the client.
    private Taskling taskling; ///< The taskling object, if created.
    private boolean unfulfillableOrder = false; ///< Remembers if the order was unfulfillable.


    /**
     * No, you are not allowed to instantiate this. Go and ask a factory.
     */
    protected TaskFlow( ) { }


    /**
     * Sets the tf id on creation.
     * @param id the id of the task flow
     */
    protected TaskFlow( final String id ) {
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
    protected void setId( final String id ) {
        this.id = id;
    }


    /**
     * @brief Delivers the value of ::order.
     * 
     * @return The value of ::order.
     */
    public DelegatingOrder<T> getOrder() {
        return order;
    }


    /**
     * @brief Sets the value of ::order to \e order.
     * 
     * @param order The new value of ::order.
     */
    public void setOrder( final DelegatingOrder<T> order ) {
        this.order = order;
    }


    /**
     * @brief Delivers the value of ::order.
     * 
     * @return The value of ::order.
     */
    public boolean hasOrder( ) {
        return order != null;
    }


    /**
     * @brief Delivers the value of ::quotes.
     * 
     * @return The value of ::quotes.
     */
    public List<Quote> getQuotes() {
        return quotes;
    }


    /**
     * @brief Sets the value of ::quotes to \e quotes.
     * 
     * @param quotes The new value of ::quotes.
     */
    public void setQuotes( final List<Quote> quotes ) {
        this.quotes = quotes;
    }


    /**
     * @brief Delivers the value of ::quotes.
     * 
     * @return The value of ::quotes.
     */
    public boolean hasQuotes( ) {
        return quotes != null && quotes.size() > 0;
    }


    public TaskFlow addQuote( final Quote quote ) {
        if( quotes == null )
            quotes = new ArrayList<Quote>( 1 );

        quotes.add( quote );
        return this;
    }


    /**
     * @brief Delivers the value of ::taskling.
     * 
     * @return The value of ::taskling.
     */
    public Taskling getTaskling() {
        return taskling;
    }


    /**
     * @brief Sets the value of ::taskling to \e taskling.
     * 
     * @param taskling The new value of ::taskling.
     */
    public void setTaskling( final Taskling taskling ) {
        this.taskling = taskling;
    }


    /**
     * @brief Delivers the value of ::taskling.
     * 
     * @return The value of ::taskling.
     */
    public boolean hasTaskling( ) {
        return taskling != null;
    }


    /**
     * @brief Delivers the value of ::preferredQuote.
     * 
     * @return The value of ::preferredQuote.
     */
    public boolean hasPreferredQuote() {
        return preferredQuote != null;
    }


    /**
     * @brief Delivers the value of ::preferredQuote.
     * 
     * @return The value of ::preferredQuote.
     */
    public Quote getPreferredQuote() {
        return preferredQuote;
    }


    /**
     * @brief Sets the value of ::preferredQuote to \e preferredQuote.
     * 
     * @param preferredQuote The new value of ::preferredQuote.
     */
    public void setPreferredQuote( final Quote preferredQuote ) {
        this.preferredQuote = preferredQuote;
    }


    /**
     * @brief Sets the value of ::unfulfillableOrder to \e b.
     * 
     * @param isUnfulfillable The new value of ::unfulfillableOrder.
     */
    public void setUnfulfillableOrder( final boolean isUnfulfillable ) {
        unfulfillableOrder = isUnfulfillable;
    }


    /**
     * @brief Delivers the value of ::unfulfillableOrder.
     * 
     * @return The value of ::unfulfillableOrder.
     */
    public boolean isUnfulfillableOrder() {
        return unfulfillableOrder;
    }
}
