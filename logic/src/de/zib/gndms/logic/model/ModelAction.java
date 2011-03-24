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



import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;


/**
 *
 * A ModelAction extends an EntityAction by a {@code Model} field.
 *
 * The first template parameter is the model for this action, the second is the return type.
 *
 * @see GridEntity
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 16:24:56
 */
public interface ModelAction<M extends GridEntity, R> extends EntityAction<R> {

	M getModel( );

    void setModel( final @NotNull M mdl );


}
