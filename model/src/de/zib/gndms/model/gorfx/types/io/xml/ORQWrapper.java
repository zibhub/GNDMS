package de.zib.gndms.model.gorfx.types.io.xml;

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



import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;


/**
 * An ORQWrapper wrapper.
 *
 * Bundles an Offerrequest with its corresponding TransientContract.
 *
 *
 * @see AbstractORQ
 * @see TransientContract
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:26:08
 */
public class ORQWrapper {

    private AbstractORQ orq;
    private TransientContract contract;


    public ORQWrapper( ) {
        
    }


    public ORQWrapper( AbstractORQ orq, TransientContract contract ) {
        this.orq = orq;
        this.contract = contract;
    }


    public AbstractORQ getOrq() {
        return orq;
    }


    public void setOrq( AbstractORQ orq ) {
        this.orq = orq;
    }


    public TransientContract getContract() {
        return contract;
    }


    public void setContract( TransientContract contract ) {
        this.contract = contract;
    }
}
