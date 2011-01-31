package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.Task;

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
 * @version $Id$
 *          <p/>
 *          Date: 17.01.2011, Time: 10:33:40
 */
public class TaskFlow {

    private AbstractORQ order;
    private List<TransientContract> quotes;
    private Task task;


    public AbstractORQ getOrder() {
        return order;
    }


    public void setOrder( AbstractORQ order ) {
        this.order = order;
    }


    public boolean hasOrder( ) {
        return order != null;
    }


    public List<TransientContract> getQuotes() {
        return quotes;
    }


    public void setQuotes( List<TransientContract> quotes ) {
        this.quotes = quotes;
    }


    public boolean hasQuotes( ) {
        return quotes != null && quotes.size() > 0;
    }


    public TaskFlow addQuote( TransientContract quote ) {
        if( quotes == null )
            quotes = new ArrayList<TransientContract>( 1 );

        quotes.add( quote );
        return this;
    }


    public Task getTask() {
        return task;
    }


    public void setTask( Task task ) {
        this.task = task;
    }


    public boolean hasTask( ) {
        return task != null;
    }
}
