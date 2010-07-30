package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
 * An AbstractXSDTypeWriter is used to convert a gndms class to the corresponding axis type.
 * It should be used in conjunction with {@link de.zib.gndms.model.gorfx.types.io.ORQWriter}
 * It contains the created axis type.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:10:31
 */
public abstract class AbstractXSDTypeWriter<M> {

    private M product;


    public M getProduct ( ) {
        return product;
    }


    protected void setProduct( M p ) {
        product = p;
    }
}
