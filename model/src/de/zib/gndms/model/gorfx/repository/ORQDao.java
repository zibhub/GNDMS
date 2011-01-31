package de.zib.gndms.model.gorfx.repository;

import de.zib.gndms.model.common.repository.QueuedTransientDao;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
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
 *          Date: 05.01.2011, Time: 18:25:49
 */
public class ORQDao<M extends AbstractORQ> extends QueuedTransientDao<TypedUUId, M, String> {


    protected M newModel( TypedUUId key ) {
        return null;  // not required here
    }


    public TypedUUId create() {
        throw new UnsupportedOperationException();
    }


    public TypedUUId create( String descriptor ) {
    }
}
