package de.zib.gndms.logic.model;

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



import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * A CreateGridResourceAction with a termination time.
 *
 * A termination time has to be denoted before {@link #initialize()} is called.
 *
 * The first template parameter is the model type. The second parameter is the return type of this action.
 *
 * @see de.zib.gndms.logic.model.CreateGridResourceAction
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * 
 * User: mjorra, Date: 13.08.2008, Time: 14:57:06
 */
public abstract class CreateTimedGridResourceAction<M extends GridResource, R>
        extends CreateGridResourceAction<M, R> {

    private Calendar terminationTime;

    protected CreateTimedGridResourceAction( ) {

    }

    
    protected CreateTimedGridResourceAction( @NotNull String id,
                                             @NotNull Calendar terminationTimeParam ) {
        super( id );
        terminationTime = terminationTimeParam;
    }

    
    @Override
    public void initialize( ) {
        requireParameter("terminationTime", getTerminationTime());
        super.initialize( );
    }
    

    public Calendar getTerminationTime() {
        return terminationTime;
    }


    public void setTerminationTime( Calendar terminationTimeParam ) {
        terminationTime = terminationTimeParam;
    }
}
