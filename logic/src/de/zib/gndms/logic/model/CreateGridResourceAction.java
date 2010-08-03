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



import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

/**
 * An AbstractModelAction with an ID.
 * An ID has to be denoted before {@link #initialize()} is called.
 *
 * The first template parameter describes the model type. It must be a {@link GridResource}.
 * The second parameter is the return type of this action.
 *
 *
 * @see de.zib.gndms.logic.model.AbstractModelAction
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.08.2008, Time: 14:51:10
 */
public abstract class CreateGridResourceAction<M extends GridResource, R> extends AbstractModelAction<M, R> {

    private String id;


    public CreateGridResourceAction( ) {
        super();

    }


    public CreateGridResourceAction( @NotNull String idParam ) {
        super();
        id = idParam;
    }


    @Override
    public void initialize( ) {
        requireParameter("id", getId());
        super.initialize();
    }


    public String getId() {
        return id;
    }


    public void setId( @NotNull String idParam ) {
        id = idParam;
    }
}
