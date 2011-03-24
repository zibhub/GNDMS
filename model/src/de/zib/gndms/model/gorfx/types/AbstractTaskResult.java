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



import java.io.Serializable;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 12:35:03
 */
public class AbstractTaskResult implements Serializable {

    private String offerType;
    
    private static final long serialVersionUID = 8410587166706272881L;

    
    protected AbstractTaskResult() {
    }


    protected AbstractTaskResult( String offerType ) {
        this.offerType = offerType;
    }


    public String getOfferType() {
        return offerType;
    }


    protected void setOfferType( String uri ) {
        this.offerType = uri;
    }

 }
