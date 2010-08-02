package de.zib.gndms.logic.action;

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



import de.zib.gndms.logic.model.EntityUpdateListener;
import de.zib.gndms.model.common.GridResource;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.10.2008, Time: 14:56:09
 */
public class FakeEntityUpdateListener<M extends GridResource> implements EntityUpdateListener<M> {

    public void onModelChange( M model ) {
        System.out.println( "onModelChange called with model id: "+ model.getId() );
    }
}
