package de.zib.gndms.model.gorfx.types.io.xml;

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



import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.common.model.gorfx.types.Quote;


/**
 * An ORQWrapper wrapper.
 *
 * Bundles an Offerrequest with its corresponding Quote.
 *
 *
 * @see de.zib.gndms.common.model.gorfx.types.AbstractOrder
 * @see Quote
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:26:08
 */
public class ORQWrapper {

    private AbstractOrder order;
    private Quote contract;


    public ORQWrapper( ) {
        
    }


    public ORQWrapper( AbstractOrder order, Quote contract ) {
        this.order = order;
        this.contract = contract;
    }


    public AbstractOrder getOrder() {
        return order;
    }


    public void setOrder( AbstractOrder order ) {
        this.order = order;
    }


    public Quote getContract() {
        return contract;
    }


    public void setContract( Quote contract ) {
        this.contract = contract;
    }
}
