package de.zib.gndms.logic.model;

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



import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;


/**
 * An abstract entity action on a given persistence model.
 * 
 * The first template parameter is the model for this action, the second is the return type of this action.
 *
 * @see AbstractEntityAction
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 16:36:20
 */
public abstract class AbstractModelAction<M extends ModelEntity, R> extends AbstractEntityAction<R>
	  implements ModelAction<M, R> {

    private volatile M model;

    public M getModel() {
        return model;
    }

    public void setModel(final @NotNull M mdl) {
        model = mdl;
    }


    @Override
    public void initialize() {
        preInitialize();
        postInitialize();
    }


    protected void preInitialize() {
         super.initialize();    // Overridden method
     }

    protected void postInitialize() {
        requireParameter("model", getModel());
    }
}
