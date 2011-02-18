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



import de.zib.gndms.logic.action.CompositeAction;
import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;


/**
 *
 * A BatchUpdateAction will executed several actions and can inform an ModelUpdateListener about a change on the model.
 * 
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 13.08.2008 Time: 10:35:07
 */
public interface BatchUpdateAction<M extends ModelEntity & GridResourceItf, R> extends CompositeAction<R, Void> {

    ModelUpdateListener<M> getListener();

    void setListener(final @NotNull ModelUpdateListener<M> listenerParam);

}
