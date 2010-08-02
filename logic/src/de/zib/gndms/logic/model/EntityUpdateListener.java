package de.zib.gndms.logic.model;

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



import de.zib.gndms.model.common.GridResource;


/**
 * An EntityUpdateListener will be informed by an <tt>BatchUpdateAction</tt> on a model.
 *
 * @see BatchUpdateAction
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:33:47
 */
public interface EntityUpdateListener<M extends GridResource> {

    /**
     * A class waiting for changes on the model must implement this method.
     * It will be notified about a change, using this method with the new model.
     *
     * @param model the new model
     */
    void onModelChange( M model );
}
